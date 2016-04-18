package ru.spbstu.dis.ui.reactor;

import com.fuzzylite.variable.InputVariable;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.dcom.da.OPCSERVERSTATUS;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;
import popup.ssn.NotificationPopup;
import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ui.DecisionSupportList;
import ru.spbstu.dis.ui.DynamicDataChart;
import static ru.spbstu.dis.ui.KnowledgeBaseRuleGenerator.*;
import ru.spbstu.dis.ui.MeterChart;
import ru.spbstu.dis.ui.PopupTester;
import ru.spbstu.dis.ui.Thermometer;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OverheatViewPanel {

	static double temperatureGrowthVal;

	static double overflowRiskVal;

	static double tankOverheatClosenessValue;

	static Item reactorTempWriterOPC;

	static Item reactorCoolerWriterOPC;

	static Item reactorTempSensorOPC;

	public final static Double MAX_TEMPERATURE = 35d;

	static DecisionSupportList decisionSupportList;

	static ArrayList notifications = new ArrayList();

	static {
		try {
			JISystem.setAutoRegisteration(true);
			JISystem.setInBuiltLogHandler(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}

		initGrowthValueChart();
		initRiskValueChart();
		initThermometerChart();
		initMeterChart();
		initClosenessValueChart();
		SwingUtilities.invokeLater(() -> decisionSupportList = new DecisionSupportList("Прогноз развития НС"));
		runSimulation();
	}

	private static void initThermometerChart() {
		final Thermometer demo = new Thermometer("Температура реактора");

		Thread th = new Thread(() -> {
			while (true) {
				demo.setValue(tankOverheatClosenessValue);
				demo.getDataset().setValue(tankOverheatClosenessValue);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		th.start();
		final JPanel titlePanel = new JPanel(new FlowLayout());
		titlePanel.add(demo.getChartPanel());
		closenessChartFrame.add(titlePanel);
	}

	private static void initMeterChart() {
		final MeterChart demo = new MeterChart("Вероятность НС на станциях");

		Thread th = new Thread(() -> {
			while (true) {
				demo.setValue(overflowRiskVal);
				demo.getDataset().setValue(overflowRiskVal);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		th.start();
		final JPanel titlePanel = new JPanel(new FlowLayout());
		titlePanel.add(demo.getChartPanel());
		closenessChartFrame.add(titlePanel);
	}

	private static DynamicDataChart closenessChartFrame = new DynamicDataChart("Вероятность переполнения бака");

	private static void initClosenessValueChart() {

		Thread th = new Thread(() -> {
			while (true) {
				closenessChartFrame.setLastValue((MAX_FLOW_SPEED - overflowRiskVal) / MAX_FLOW_SPEED);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		th.start();

		SwingUtilities.invokeLater(() -> {
			closenessChartFrame.addButton();
			closenessChartFrame.pack();
			closenessChartFrame.setVisible(true);
		});
	}

	private static void initGrowthValueChart() {
		final DynamicDataChart demo = new DynamicDataChart("Вероятность перегрева");

		Thread th = new Thread(() -> {
			while (true) {
				demo.setLastValue((MAX_TEMPERATURE - temperatureGrowthVal) / MAX_TEMPERATURE);
				final Millisecond now = new Millisecond();
				System.out.println("Now = " + now.toString());
				demo.getSeries().add(new Millisecond(), demo.getLastValue());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		th.start();

		XYPlot plot = (XYPlot) closenessChartFrame.getChartPanel().getChart().getPlot();
		plot.setDataset(plot.getDatasetCount(), ((XYPlot) demo.getChartPanel().getChart().getPlot()).getDataset(0));
	}

	private static void initRiskValueChart() {
		final DynamicDataChart demo = new DynamicDataChart("Вероятность переполнения бака станции смешивания");

		Thread th = new Thread(() -> {
			while (true) {
				demo.setLastValue(tankOverheatClosenessValue);
				final Millisecond now = new Millisecond();
				System.out.println("Now = " + now.toString());
				demo.getSeries().add(new Millisecond(), demo.getLastValue());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		th.start();

		XYPlot plot = (XYPlot) closenessChartFrame.getChartPanel().getChart().getPlot();
		plot.setDataset(plot.getDatasetCount(), ((XYPlot) demo.getChartPanel().getChart().getPlot()).getDataset(0));
	}

	private static void runSimulation() {
		try {
			generateRulesForOverflowOfTank();
			final ConnectionInformation ci = new ConnectionInformation();
			ci.setHost("seal-machine1");
			ci.setUser("Administrator");
			ci.setPassword("seal");
			ci.setClsid("6F17505C-4351-46AC-BC1E-CDE34BB53FAA");

			final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
			server.connect();
			OPCSERVERSTATUS serverState = server.getServerState();
			System.out.println("serverState = " + serverState.getBuildNumber());
			String reactorTemperature = Tag.TAG_TO_ID_MAPPING.get(Tag.REACTOR_Controlled_TEMPERATURE);
			String downstream = Tag.TAG_TO_ID_MAPPING.get(Tag.REACTOR_DOWNSTREAM_ON);
			String reactorTemperatureSensor = Tag.TAG_TO_ID_MAPPING.get(Tag.REACTOR_Current_Process_Temperature);
			String reactorCooler = Tag.TAG_TO_ID_MAPPING.get(Tag.REACTOR_ControlPanel_Mixing_on);
			String reactorHeater = Tag.TAG_TO_ID_MAPPING.get(Tag.REACTOR_ControlPanel_mixing_pump_P201_on);
			String mixerValve1 = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_valve_V201_ToMainTank_on);
			String mixerValve2 = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_valve_V202_ToMainTank_on);
			String mixerValve3 = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_valve_V203_ToMainTank_on);
			String mixerSpeed = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_ControlPanel_FLOW_SPEED);
			String mixerRealSpeed = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_TANK_MAN_FLOW_SPEED);
			String mixerValveSensor1 = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_valve_V201_ToMainTank_SENSOR);
			String mixerValveSensor2 = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_valve_V202_ToMainTank_SENSOR);
			String mixerValveSensor3 = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_valve_V203_ToMainTank_SENSOR);
			String mixerToMaintTankPump = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_ControlPanel_PumpToMainTank_P201_on);
			String mixerTopWaterLvlSensor = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_tank_B204_top);
			String mixerBottomWaterLvlSensor = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_tank_B204_water_bottom_level_sensor);
			final Group serverObject = server.addGroup("test");

			reactorTempWriterOPC = serverObject.addItem(reactorTemperature);
			reactorTempSensorOPC = serverObject.addItem(reactorTemperatureSensor);
			reactorCoolerWriterOPC = serverObject.addItem(reactorCooler);
			reactorHeaterItemOPC = serverObject.addItem(reactorHeater);
			reactorDownStream = serverObject.addItem(downstream);
			mixerPump1 = serverObject.addItem(mixerValve1);
			mixerPump2 = serverObject.addItem(mixerValve2);
			mixerPump3 = serverObject.addItem(mixerValve3);
			mixerPump1Sens = serverObject.addItem(mixerValveSensor1);
			mixerPump2Sens = serverObject.addItem(mixerValveSensor2);
			mixerPump3Sens = serverObject.addItem(mixerValveSensor3);
			mixingSpeed = serverObject.addItem(mixerSpeed);
			mixingTopLevel = serverObject.addItem(mixerTopWaterLvlSensor);
			mixingBottomLevel = serverObject.addItem(mixerBottomWaterLvlSensor);
			mixerMainPump = serverObject.addItem(mixerToMaintTankPump);
			mixerMainPumpRealSpeed = serverObject.addItem(mixerRealSpeed);
		} catch (AlreadyConnectedException e) {
			e.printStackTrace();
		} catch (JIException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		} catch (DuplicateGroupException e) {
			e.printStackTrace();
		} catch (AddFailedException e) {
			e.printStackTrace();
		}
		Thread th = new Thread(() -> {

			try {
				getDataFromOPC(tGrowth, tCloseness, tankOverflowRisk);
			} catch (AlreadyConnectedException e) {
				e.printStackTrace();
			} catch (JIException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (NotConnectedException e) {
				e.printStackTrace();
			} catch (DuplicateGroupException e) {
				e.printStackTrace();
			} catch (AddFailedException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		th.start();
	}

	private static void getDataFromOPC(final InputVariable tGrowth, final InputVariable tCloseness,
			final InputVariable tankOverHeatRisk) throws AlreadyConnectedException, JIException, UnknownHostException,
					NotConnectedException, DuplicateGroupException, AddFailedException, InterruptedException {

		reactorHeaterItemOPC.write(new JIVariant(true));
		while (true) {
			Thread.sleep(2000);
			tankOverheatClosenessValue = reactorTempSensorOPC.read(true).getValue().getObjectAsFloat();
			temperatureGrowthVal = reactorTempWriterOPC.read(true).getValue().getObjectAsFloat();
			decisionSupportList.getSeries().add(new Millisecond(), tankOverheatClosenessValue);
			overflowRiskVal = mixerMainPumpRealSpeed.read(true).getValue().getObjectAsFloat();
			if (tankOverheatClosenessValue >= MAX_TEMPERATURE) {
				coolDownReactor();
			}
			if (overflowRiskVal > MAX_FLOW_SPEED && mixingBottomLevel.read(true).getValue().getObjectAsBoolean()) {
				stopWaterFlowToMainTank();
			}
			notifier(
					String.format(
							"Температура реактора=%s,\nВероятность перегрева=%s,\n"
									+ "Вероятность переполнения бака=%s " + "->\n" + " RECOMMENDED " + actionName
									+ "=%s,\nACTIONS=%s",
							tankOverheatClosenessValue,
							(tankOverheatClosenessValue - MAX_TEMPERATURE) / MAX_TEMPERATURE,
							(overflowRiskVal - MAX_FLOW_SPEED) / MAX_FLOW_SPEED, "User action", 0.1),
					tankOverheatClosenessValue);
		}
	}

	private static void stopWaterFlowToMainTank() throws JIException, InterruptedException {
		mixerMainPumpRealSpeed.write(new JIVariant(0f));
		mixerMainPump.write(new JIVariant(false));
		reactorDownStream.write(new JIVariant(true));
		Thread.sleep(1000);
		reactorDownStream.write(new JIVariant(false));
	}

	private static void coolDownReactor() throws JIException, InterruptedException {
		reactorTempWriterOPC.write(new JIVariant(0f));
		reactorHeaterItemOPC.write(new JIVariant(false));
		boolean pump1Val = mixerPump1Sens.read(true).getValue().getObjectAsBoolean();
		mixerPump1.write(new JIVariant(true));
		boolean pump2val = mixerPump2Sens.read(true).getValue().getObjectAsBoolean();
		mixerPump2.write(new JIVariant(true));
		boolean pump3val = mixerPump3Sens.read(true).getValue().getObjectAsBoolean();
		mixerPump3.write(new JIVariant(true));
		mixingSpeed.write(new JIVariant(40f));
		reactorDownStream.write(new JIVariant(true));
		Thread.sleep(1000);
		reactorDownStream.write(new JIVariant(false));
		reactorCoolerWriterOPC.write(new JIVariant(true));
	}

	private static boolean showUserDecisionDialog() {
		// show a joptionpane dialog using showMessageDialog
		Object[] options = { "Turn on coller", "Turn on downstream for 5 sec", "Minus 20 degree from actual temp",
				"Stop Heating" };
		int n = JOptionPane.showOptionDialog(closenessChartFrame, "Принятие решений?", "Вывод из НС",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
		if (n == 1) {

			try {
				reactorDownStream.write(new JIVariant(true));
			} catch (JIException e) {
				e.printStackTrace();
			}
		}
		if (n == 2) {
		}
		if (n == 3) {
			try {
				reactorCoolerWriterOPC.write(new JIVariant(false));
				reactorHeaterItemOPC.write(new JIVariant(false));
			} catch (JIException e) {
				e.printStackTrace();
			}
			return true;
		}
		if (n == 0) {
			try {

				reactorCoolerWriterOPC.write(new JIVariant(true));
			} catch (JIException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private static void notifier(String term, double dangerLevel) {

		if (dangerLevel > 0.5d) {
			ImageIcon icon = new ImageIcon(OverheatViewPanel.class.getResource("/alarm.png"));
			showErrorNotif(term, new Color(249, 78, 30), icon);
		}
		if (dangerLevel > 0.0d && dangerLevel <= 0.3d) {
			ImageIcon icon = new ImageIcon(OverheatViewPanel.class.getResource("/info.png"));
			showErrorNotif(term, new Color(127, 176, 72), icon);
		}
		if (dangerLevel > 0.3d && dangerLevel <= 0.5d) {
			ImageIcon icon = new ImageIcon(OverheatViewPanel.class.getResource("/warning.png"));
			showErrorNotif(term, new Color(249, 236, 100), icon);
		}
	}

	private static void showErrorNotif(String term, Color color, ImageIcon icon) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ex) {
			Logger.getLogger(PopupTester.class.getName()).log(Level.SEVERE, null, ex);
		}
		notifications.add(term);
		if (decisionSupportList != null) {
			decisionSupportList.getList().setListData(notifications.toArray());
			decisionSupportList.getList().updateUI();
			decisionSupportList.getFrame().repaint();
		}
		NotificationPopup nf = new NotificationPopup(term);
		nf.setIcon(icon);
		nf.setWIDTH(650);
		nf.setHEIGHT(100);
		nf.setLocation(10, 10);
		nf.setFont(new Font("Tachoma", Font.LAYOUT_LEFT_TO_RIGHT, 12));
		nf.setAlwaysOnTop(true);

		nf.setTitle("Ошибка");
		nf.setDisplayTime(3000);
		nf.setBackgroundColor1(Color.white);
		nf.setBackGroundColor2(color);
		nf.setForegroundColor(java.awt.Color.darkGray);
		nf.display();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Logger.getLogger(PopupTester.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static Item reactorHeaterItemOPC;

	private static Item reactorDownStream;

	private static Item mixerPump1;

	private static Item mixerPump2;

	private static Item mixerPump3;

	private static Item mixingSpeed;

	private static Item mixerPump1Sens;

	private static Item mixerPump2Sens;

	private static Item mixerPump3Sens;

	private static final int MAX_FLOW_SPEED = 50;

	private static Item mixingTopLevel;

	private static Item mixingBottomLevel;

	private static Item mixerMainPump;

	private static Item mixerMainPumpRealSpeed;
}