package ru.spbstu.dis.ui;

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
import org.jfree.data.time.Millisecond;
import org.jfree.ui.RefineryUtilities;
import popup.ssn.NotificationPopup;
import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ep.data.opc.OPCDataReader;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

  static DecisionSupportList decisionSupportList;

  static String[] notifications = new String[10];

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
    SwingUtilities.invokeLater(
        () -> decisionSupportList = new DecisionSupportList("Decisions List"));
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

    engine.configure(new AlgebraicProduct(), new Maximum(), new Minimum(), new Maximum(),
        new Bisector());

    StringBuilder status = new StringBuilder();
    if (!engine.isReady(status)) {
      throw new RuntimeException(
          "Engine not ready. " + "The following errors were encountered:\n" + status.toString());
    }
  }

  private static void generateRulesForOverflowOfTank() {
    InputVariable tGrowth = new InputVariable(growthName);
    generateTriangularTerm(tGrowth);
    engine.addInputVariable(tGrowth);

    InputVariable tCloseness = new InputVariable(closenessName);
    generateTriangularTerm(tCloseness);
    engine.addInputVariable(tCloseness);

    InputVariable tankOverflowRisk = new InputVariable(riskName);
    generateTriangularTerm(tankOverflowRisk);
    engine.addInputVariable(tankOverflowRisk);

    RuleBlock ruleBlock = new RuleBlock("firstBlock", new Minimum(), new Maximum(),
        new Minimum());

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

    ruleBlock.addRule(
        Rule.parse("if " + riskName + " is " + highLevelName + " and " + closenessName + " is "
            + normalLevelName + " and " + growthName + " is " + highLevelName + " " + "then "
            + actionName
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
      growthValue = actualValues.get(Tag.PRESSURE) / 2 ;
      tGrowth.setInputValue(growthValue);

      closenessValue = actualValues.get(Tag.LOWER_PRESSURE) / 2 ;
      tCloseness.setInputValue(closenessValue);
      //TODO change to formula of water tank filling
      tankOverflowRiskValue = 0.2;
      tCloseness.setInputValue(tankOverflowRiskValue);
      decisionSupportList.getSeries().add(new Millisecond(),
          tankOverflowRiskValue);
      engine.process();
      if (action.highestMembershipTerm(action.getOutputValue()) != null
          && action.highestMembershipTerm(action.getOutputValue()).getName()
          .equals(userDecisionAction)) {
        if (showUserDecisionDialog()) {
          return;
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
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private static boolean showUserDecisionDialog() {
    // show a joptionpane dialog using showMessageDialog
    Object[] options = {"Turn off First Pump", "Turn off Second Pump",
        "Turn off Third Pump", "Close all pumps", "Stop Station"};
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
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ex) {
      Logger.getLogger(PopupTester.class.getName()).log(Level.SEVERE, null, ex);
    }
    for (int i = 0; i < notifications.length; i++) {
      if (notifications[i] == null) {
        notifications[i] = term;
        break;
      }
    }
    if (notifications[9] != null) {
      notifications = Arrays.copyOf(Arrays.copyOfRange(notifications,
              0,
              9),
          10);
      notifications[9] = term;
    }
    if (decisionSupportList != null) {
      decisionSupportList.getList().setListData(notifications);
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