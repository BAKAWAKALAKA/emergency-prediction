package ru.spbstu.dis.ui;

import static ru.spbstu.dis.ui.KnowledgeBaseRuleGenerator.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import org.jfree.data.time.Millisecond;

import popup.ssn.NotificationPopup;
import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ep.data.opc.OPCDataReader;

import com.fuzzylite.FuzzyLite;
import com.fuzzylite.Op;
import com.fuzzylite.variable.InputVariable;

public class MainPanel {

	static double growthValue;

	static double closenessValue;

	static double tankOverflowRiskValue;

	static DecisionSupportList decisionSupportList;

	static ArrayList notifications = new ArrayList();

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
		initClosenessValueChart();
		SwingUtilities.invokeLater(() -> decisionSupportList = new DecisionSupportList("Decisions List"));
		runSimulation();
	}

	private static void initThermometerChart() {
		final Thermometer demo = new Thermometer("Heat station temperature");

		Thread th = new Thread(() -> {
			while (true) {
				demo.value = growthValue;
				demo.dataset.setValue(growthValue * 10);
				final Millisecond now = new Millisecond();
				System.out.println("Now = " + now.toString());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		th.start();
		final JPanel titlePanel = new JPanel(new FlowLayout());
		titlePanel.add(new JLabel("Heating Station control"));
		closenessChartFrame.add(titlePanel);
		closenessChartFrame.addChart(demo.chartPanel);
	}

	private static DynamicDataChart closenessChartFrame = new DynamicDataChart("Closeness Value");

	private static void initClosenessValueChart() {

		Thread th = new Thread(() -> {
			while (true) {
				closenessChartFrame.lastValue = closenessValue;
				final Millisecond now = new Millisecond();
				System.out.println("Now = " + now.toString());
				closenessChartFrame.series.add(new Millisecond(), closenessChartFrame.lastValue);
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
		final DynamicDataChart demo = new DynamicDataChart("Growth value");

		Thread th = new Thread(() -> {
			while (true) {
				demo.lastValue = growthValue;
				final Millisecond now = new Millisecond();
				System.out.println("Now = " + now.toString());
				demo.series.add(new Millisecond(), demo.lastValue);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		th.start();

		closenessChartFrame.addChart(demo.chartPanel);
	}

	private static void initRiskValueChart() {
		final DynamicDataChart demo = new DynamicDataChart("Tank Overflow Risk");

		Thread th = new Thread(() -> {
			while (true) {
				demo.lastValue = tankOverflowRiskValue;
				final Millisecond now = new Millisecond();
				System.out.println("Now = " + now.toString());
				demo.series.add(new Millisecond(), demo.lastValue);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		th.start();

		closenessChartFrame.addChart(demo.chartPanel);
	}

	private static void runSimulation() {

		Thread th = new Thread(() -> {

			final OPCDataReader opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			getDataFromOPC(tGrowth, tCloseness, tankOverflowRisk, opcDataReader);
		});
		th.start();

	}

	private static void getDataFromOPC(final InputVariable tGrowth, final InputVariable tCloseness,
			final InputVariable tankOverflowRisk, final OPCDataReader opcDataReader) {
		while (true) {
			final Map<Tag, Double> actualValues = opcDataReader.getActualValues();
			System.out.println(actualValues);
			growthValue = actualValues.get(Tag.PRESSURE) / 2;
			tGrowth.setInputValue(growthValue);

			closenessValue = actualValues.get(Tag.LOWER_PRESSURE) / 2;
			tCloseness.setInputValue(closenessValue);
			// TODO change to formula of water tank filling
			tankOverflowRiskValue = 0.2;
			tCloseness.setInputValue(tankOverflowRiskValue);
			decisionSupportList.getSeries().add(new Millisecond(), tankOverflowRiskValue);
			engine.process();
			if (action.highestMembershipTerm(action.getOutputValue()) != null
					&& action.highestMembershipTerm(action.getOutputValue()).getName().equals(userDecisionAction)) {
				if (showUserDecisionDialog()) {
					return;
				}
			}
			if (action.highestMembershipTerm(action.getOutputValue()) == null
					|| action.highestMembershipTerm(action.getOutputValue()).getName().equals(emergencyStopAction)) {
				// show a joptionpane dialog using showMessageDialog
				JOptionPane.showMessageDialog(closenessChartFrame, "Water Overflow",
						"Emergency situation, Station " + "stopped", JOptionPane.ERROR_MESSAGE);
				closenessValue = 0;
				growthValue = 0;
				tankOverflowRiskValue = 0;
				return;
			}
			FuzzyLite.logger()
					.info(String.format(
							"growth=%s, closeness=%s, tankOverflowRisk=%s -> " + actionName + ".output=%s, action=%s",
							Op.str(growthValue), Op.str(closenessValue), Op.str(tankOverflowRiskValue),
							Op.str(action.getOutputValue()), action.fuzzyOutputValue()));
			notifier(
					String.format(
							"GROWTH=%s,\nCLOSENESS=%s,\nOVERF" + lowLevelName + "_RISK=%s " + "->\n" + " RECOMMENDED "
									+ actionName + "=%s,\nACTIONS=%s",
							tGrowth.highestMembershipTerm(tGrowth.getInputValue()).getName(),
							tCloseness.highestMembershipTerm(closenessValue).getName(),
							tankOverflowRisk.highestMembershipTerm(tankOverflowRiskValue).getName(),
							action.highestMembershipTerm(action.getOutputValue()).getName(), action.fuzzyOutputValue()),
					tankOverflowRiskValue);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean showUserDecisionDialog() {
		// show a joptionpane dialog using showMessageDialog
		Object[] options = { "Turn off First Pump", "Turn off Second Pump", "Turn off Third Pump", "Close all pumps",
				"Stop Station" };
		int n = JOptionPane.showOptionDialog(closenessChartFrame, "Would you like to close " + "pumps?",
				"User decision", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				options[2]);
		if (n == 1) {
			closenessValue -= closenessValue / 2;
			growthValue -= growthValue / 2;
			tankOverflowRiskValue -= tankOverflowRiskValue / 2;
		}
		if (n == 2) {
			closenessValue -= closenessValue / 2;
			growthValue -= growthValue / 2;
			tankOverflowRiskValue -= tankOverflowRiskValue / 2;
		}
		if (n == 3) {
			closenessValue = 0;
			growthValue = 0;
			tankOverflowRiskValue = 0;
			return true;
		}
		if (n == 4) {
			closenessValue = 0;
			growthValue = 0;
			tankOverflowRiskValue = 0;
			return true;
		}
		if (n == 0) {
			closenessValue -= closenessValue / 2;
			growthValue -= growthValue / 2;
			tankOverflowRiskValue -= tankOverflowRiskValue / 2;
		}
		return false;
	}

	private static void notifier(String term, double dangerLevel) {

		if (dangerLevel > 0.5d) {
			ImageIcon icon = new ImageIcon(MainPanel.class.getResource("/alarm.png"));
			showErrorNotif(term, new Color(249, 78, 30), icon);
		}
		if (dangerLevel > 0.0d && dangerLevel <= 0.3d) {
			ImageIcon icon = new ImageIcon(MainPanel.class.getResource("/info.png"));
			showErrorNotif(term, new Color(127, 176, 72), icon);
		}
		if (dangerLevel > 0.3d && dangerLevel <= 0.5d) {
			ImageIcon icon = new ImageIcon(MainPanel.class.getResource("/warning.png"));
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
			Thread.sleep(2000);
		} catch (InterruptedException ex) {
			Logger.getLogger(PopupTester.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}