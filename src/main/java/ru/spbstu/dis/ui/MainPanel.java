package ru.spbstu.dis.ui;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import org.jfree.data.time.Millisecond;
import org.jfree.ui.RefineryUtilities;

import popup.ssn.NotificationPopup;

import com.fuzzylite.Engine;
import com.fuzzylite.FuzzyLite;
import com.fuzzylite.Op;
import com.fuzzylite.defuzzifier.Bisector;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.AlgebraicProduct;
import com.fuzzylite.norm.t.Minimum;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Triangle;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;

public class MainPanel {
    static String actionName = "ACTION";
    static String highLevelName = "HIGH";
    static String emergencyStopAction = "EMERGENCY_STOP";
    static String normalLevelName = "NORMAL";
    static final String growthName = "GROWTH";
    static final String closenessName = "CLOSENESS";
    static final String riskName = "RISK";
    static String userDecisionAction = "USER_DECISION";
    static String lowLevelName = "LOW";
    static String doNothingAction = "DO_NOTHING";

	public static final Triangle LOW = new Triangle(lowLevelName, 0.000, 0.250, 0.500);

	public static final Triangle NORMAL = new Triangle(normalLevelName, 0.250, 0.500, 0.750);

	public static final Triangle HIGHT = new Triangle(highLevelName, 0.500, 0.750, 1.000);

	private static boolean alertDialogShown = false;
	private static boolean emergencyStop = false;
	static double growthValue;
	static double closenessValue;
	static double tankOverflowRiskValue;

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
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				final CombinedXYPlotDemo5 demo = new CombinedXYPlotDemo5("Combined XY Plot Demo 5");
				demo.pack();
				RefineryUtilities.centerFrameOnScreen(demo);
				demo.setVisible(true);
			}
		});
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

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				closenessChartFrame.addButton();
				closenessChartFrame.pack();
				closenessChartFrame.setVisible(true);
			}
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

	private static Engine engine = new Engine();
	static OutputVariable action;

	private static void runSimulation() {

		engine.setName("EmergencyPredictor");

		action = new OutputVariable();
		action.setName("ACTION");
		action.setRange(0.000, 1.000);
		action.setDefaultValue(Double.NaN);
		action.addTerm(new Triangle("DO_NOTHING", 0.000, 0.250, 0.500));
		action.addTerm(new Triangle("USER_DECISION", 0.250, 0.500, 0.750));
		action.addTerm(new Triangle("EMERGENCY_STOP", 0.500, 0.750, 1.000));
		engine.addOutputVariable(action);

		generateRulesForOverflowOfTank();

		engine.configure(new AlgebraicProduct(), new Maximum(), new Minimum(), new Maximum(), new Bisector());

		StringBuilder status = new StringBuilder();
		if (!engine.isReady(status)) {
			throw new RuntimeException(
					"Engine not ready. " + "The following errors were encountered:\n" + status.toString());
		}

	}

	private static void generateRulesForOverflowOfTank() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				InputVariable tGrowth = new InputVariable(growthName);
				generateTriangularTerm(tGrowth);
				engine.addInputVariable(tGrowth);

				InputVariable tCloseness = new InputVariable(closenessName);
				generateTriangularTerm(tCloseness);
				engine.addInputVariable(tCloseness);

				InputVariable tankOverflowRisk = new InputVariable(riskName);
				generateTriangularTerm(tankOverflowRisk);
				engine.addInputVariable(tankOverflowRisk);

				RuleBlock ruleBlock = new RuleBlock("firstBlock", new Minimum(), new Maximum(), new Minimum());

				ruleBlock
						.addRule(
								Rule.parse(
										"if " + growthName + " is " + highLevelName + " and " + closenessName + " is "
												+ highLevelName + " then " + actionName + " is " + emergencyStopAction,
										engine));

				ruleBlock
						.addRule(
								Rule.parse(
										"if " + growthName + " is " + highLevelName + " and " + closenessName + " is "
												+ normalLevelName + " then " + actionName + " is " + userDecisionAction,
										engine));

				ruleBlock
						.addRule(
								Rule.parse(
										"if " + growthName + " is " + highLevelName + " and " + closenessName + " is "
												+ lowLevelName + " then " + actionName + " is " + userDecisionAction,
										engine));
				ruleBlock
						.addRule(
								Rule.parse(
										"if " + growthName + " is " + normalLevelName + " and " + closenessName + " is "
												+ highLevelName + " then " + actionName + " is " + userDecisionAction,
										engine));

				ruleBlock
						.addRule(
								Rule.parse(
										"if " + growthName + " is " + normalLevelName + " and " + closenessName + " is "
												+ normalLevelName + " then " + actionName + " is " + doNothingAction,
										engine));
				ruleBlock
						.addRule(
								Rule.parse(
										"if " + growthName + " is " + normalLevelName + " and " + closenessName + " is "
												+ lowLevelName + " then" + " " + actionName + " is " + doNothingAction,
										engine));
				ruleBlock
						.addRule(
								Rule.parse(
										"if " + growthName + " is " + lowLevelName + " and " + closenessName + " is "
												+ highLevelName + " then " + actionName + " is " + userDecisionAction,
										engine));
				ruleBlock
						.addRule(
								Rule.parse(
										"if " + growthName + " is " + lowLevelName + " and " + closenessName + " is "
												+ normalLevelName + " then " + actionName + " is " + doNothingAction,
										engine));
				ruleBlock
						.addRule(
								Rule.parse(
										"if " + growthName + " is " + lowLevelName + " and " + closenessName + " is "
												+ lowLevelName + " then " + actionName + " is " + doNothingAction,
										engine));

				ruleBlock.addRule(Rule.parse("if " + riskName + " is " + highLevelName + " and " + closenessName + " is "
						+ normalLevelName + " and " + growthName + " is " + highLevelName + " " + "then " + actionName
						+ " is " + emergencyStopAction, engine));

				ruleBlock
						.addRule(
								Rule.parse(
										"if " + riskName + " is " + highLevelName + " and " + closenessName + " is "
												+ highLevelName + " then " + actionName + " is " + userDecisionAction,
										engine));
				ruleBlock
						.addRule(Rule.parse(
								"if " + riskName + " is " + highLevelName + " and " + closenessName + " is "
										+ normalLevelName + " then " + actionName + " is " + userDecisionAction,
								engine));
				ruleBlock
						.addRule(
								Rule.parse(
										"if " + riskName + " is " + highLevelName + " and " + closenessName + " is "
												+ lowLevelName + " then " + actionName + " is " + emergencyStopAction,
										engine));
				ruleBlock
						.addRule(
								Rule.parse(
										"if " + riskName + " is " + normalLevelName + " and " + closenessName + " is "
												+ highLevelName + " then " + actionName + " is " + doNothingAction,
										engine));
				ruleBlock
						.addRule(Rule.parse(
								"if " + riskName + " is " + normalLevelName + " and " + closenessName + " is "
										+ normalLevelName + " then " + actionName + " is " + userDecisionAction,
								engine));
				ruleBlock
						.addRule(
								Rule.parse(
										"if " + riskName + " is " + normalLevelName + " and " + closenessName + " is "
												+ lowLevelName + " then " + actionName + " is " + userDecisionAction,
										engine));
				ruleBlock
						.addRule(
								Rule.parse(
										"if " + riskName + " is " + lowLevelName + " and " + growthName + " is "
												+ highLevelName + " then " + actionName + " is " + userDecisionAction,
										engine));
				engine.addRuleBlock(ruleBlock);
				for (int i = 1; !emergencyStop; i++) {
					growthValue = tGrowth.getMinimum() + i * (tGrowth.range() / 5);
					tGrowth.setInputValue(growthValue);
                    //TODO подставить настоящие теги
					for (int j = 1; j < 3; j++) {

						closenessValue = tCloseness.getMinimum() + i * (tCloseness.range() / 3);
						tCloseness.setInputValue(closenessValue);

						for (int k = 1; k < 3; k++) {

							tankOverflowRiskValue = tankOverflowRisk.getMinimum() + i * (tankOverflowRisk.range() / 3);
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							tCloseness.setInputValue(tankOverflowRiskValue);
							engine.process();
							if (action.highestMembershipTerm(action.getOutputValue()) != null
									&& action.highestMembershipTerm(action.getOutputValue()).getName()
											.equals(userDecisionAction)) {
								// show a joptionpane dialog using showMessageDialog
								Object[] options = { "Turn off First Pump", "Turn off Second Pump",
										"Turn off Third Pump", "Close all pumps", "Stop Station" };
								int n = JOptionPane.showOptionDialog(closenessChartFrame,
										"Would you like to close " + "pumps?", "User decision",
										JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
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
									return;
								}
								if (n == 4) {
									closenessValue = 0;
									growthValue = 0;
									tankOverflowRiskValue = 0;
									return;
								}
								if (n == 0) {
									closenessValue -= closenessValue / 2;
									growthValue -= growthValue / 2;
									tankOverflowRiskValue -= tankOverflowRiskValue / 2;
								}
							}
							if (action.highestMembershipTerm(action.getOutputValue()) == null
									|| action.highestMembershipTerm(action.getOutputValue()).getName()
											.equals(emergencyStopAction)) {
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
											"growth=%s, closeness=%s, tankOverflowRisk=%s -> " + actionName
													+ ".output=%s, action=%s",
											Op.str(growthValue), Op.str(closenessValue), Op.str(tankOverflowRiskValue),
											Op.str(action.getOutputValue()), action.fuzzyOutputValue()));
							notifier(String.format(
									"GROWTH=%s,\nCLOSENESS=%s,\nOVERF" + lowLevelName + "_RISK=%s " + "->\n"
											+ " RECOMMENDED " + actionName + "=%s,\nACTIONS=%s",
									tGrowth.highestMembershipTerm(tGrowth.getInputValue()).getName(),
									tCloseness.highestMembershipTerm(closenessValue).getName(),
									tankOverflowRisk.highestMembershipTerm(tankOverflowRiskValue).getName(),
									action.highestMembershipTerm(action.getOutputValue()).getName(),
									action.fuzzyOutputValue()), tankOverflowRiskValue);
						}

					}

				}

			}
		});
		th.start();

	}

	private static void generateTriangularTerm(InputVariable tankOverflowRisk) {
		tankOverflowRisk.setRange(0.000, 1.000);
		tankOverflowRisk.addTerm(LOW);
		tankOverflowRisk.addTerm(NORMAL);
		tankOverflowRisk.addTerm(HIGHT);
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