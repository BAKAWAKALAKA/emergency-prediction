package ru.spbstu.dis.ui.emergency;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.slf4j.LoggerFactory;
import ru.spbstu.dis.opc.client.api.opc.access.OpcAccessApi;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class FilterStationEmergencyPredictionFilterDestructionAfterOuterValve
    extends EmergencyPrediction {
  private static final org.slf4j.Logger LOGGER = LoggerFactory
      .getLogger(FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.class);

  public FilterStationEmergencyPredictionFilterDestructionAfterOuterValve(
      final OpcAccessApi opcAccessApi) {
    super(opcAccessApi);
  }

  public static void main(String[] args) {
    setLookAndFeelType();
    FilterStationEmergencyPredictionFilterDestructionAfterOuterValve emergencyPredictionWindow =
        new FilterStationEmergencyPredictionFilterDestructionAfterOuterValve(createOpcApi());
    emergencyPredictionWindow.initMeterChart();
    //need to be called last!
    emergencyPredictionWindow.composeAllChartsIntoOne();

    emergencyPredictionWindow.runSimulation();
  }

  private void composeAllChartsIntoOne() {

    Thread th = new Thread(() -> {
      while (true) {
        if (filter_fake_active_flag) {
          filter_fake_risk_value += 0.1 * new Random().nextDouble();
        }
        closenessChartFrame.setLastValue(filter_fake_risk_value);
        closenessChartFrame.getSeries().add(new Millisecond(), closenessChartFrame.getLastValue());
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    th.start();

    JLabel esType = new JLabel(
        Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.0")
            + Messages.getString(
            "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.1")); //$NON-NLS-1$ //$NON-NLS-2$
    Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
    fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
    Font boldUnderline = new Font("Tachoma", Font.BOLD, 22)
        .deriveFont(fontAttributes); //$NON-NLS-1$
    esType.setFont(boldUnderline);
    final JPanel titlePanel = new JPanel(new FlowLayout());
    titlePanel.add(esType, BorderLayout.CENTER);
    closenessChartFrame.add(titlePanel, 0);

    esType = new JLabel(Messages.getString(
        "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.3")); //$NON-NLS-1$
    boldUnderline = new Font("Tachoma", Font.PLAIN, 19).deriveFont(fontAttributes); //$NON-NLS-1$
    esType.setFont(boldUnderline);
    final JPanel chartPanel = new JPanel(new FlowLayout());
    chartPanel.add(esType, BorderLayout.CENTER);
    closenessChartFrame.add(chartPanel, 1);

    XYPlot plot = (XYPlot) closenessChartFrame.getChartPanel().getChart().getPlot();
    XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
    renderer0.setBaseShapesVisible(false);
    plot.setRenderer(0, renderer0);
    plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.RED);
    plot.getRenderer().setSeriesPaint(0, Color.RED);
    plot.getRenderer().setSeriesStroke(0, new BasicStroke(3.0f));
    SwingUtilities.invokeLater(() -> {

      final JPanel actionRecoms = new JPanel(new FlowLayout());
      JLabel actionRecommLabel = new JLabel(Messages.getString(
          "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.5")); //$NON-NLS-1$
      Font boldUnderlineAction = new Font("Tachoma", Font.BOLD, 15)
          .deriveFont(fontAttributes); //$NON-NLS-1$
      actionRecommLabel.setFont(boldUnderlineAction);
      actionRecoms.add(actionRecommLabel);
      closenessChartFrame.add(actionRecoms);

      final String[] actions = {
          Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.7"),
          Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.8") +
              //$NON-NLS-1$ //$NON-NLS-2$
              Messages
                  .getString("FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.9"),
          //$NON-NLS-1$
          Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.10"),
          Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.11"),
          //$NON-NLS-1$ //$NON-NLS-2$
          Messages.getString(
              "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.12")}; //$NON-NLS-1$
      JList recomActions = new JList(actions);

      final JPanel actionsPanel = new JPanel(new FlowLayout());
      actionsPanel.setBorder(BorderFactory.createCompoundBorder());
      actionsPanel.add(recomActions, BorderLayout.CENTER);
      closenessChartFrame.add(actionsPanel);

      final JPanel actionOutput = new JPanel(new FlowLayout());
      JLabel esTypeAction = new JLabel(
          Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.13"),
          //$NON-NLS-1$
          SwingConstants.CENTER);
      Font boldUnderlineBig = new Font("Tachoma", Font.BOLD, 18)
          .deriveFont(fontAttributes); //$NON-NLS-1$
      esTypeAction.setFont(boldUnderlineBig);
      actionOutput.add(esTypeAction);
      actionOutput.setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, Color.black));
      closenessChartFrame.add(actionOutput);

      final JPanel statePanel = new JPanel(new FlowLayout());
      JLabel stateLbl = new JLabel(Messages.getString(
          "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.15")); //$NON-NLS-1$
      stateLbl.setFont(new Font("Tachoma", Font.PLAIN, 10)); //$NON-NLS-1$
      statePanel.add(stateLbl);
      closenessChartFrame.add(statePanel);

      JPanel finishedActionsPnl = new JPanel(new FlowLayout());
      BufferedImage myPicture = null;
      try {
        myPicture = ImageIO.read(
            FilterStationEmergencyPredictionFoulBlockage.class.getResource(
                "/filter_outpu_error.png"));
      } catch (IOException e) {
        e.printStackTrace();
      }
      picLabel = new JLabel(new ImageIcon(myPicture));
      finishedActionsPnl.add(picLabel);
      picLabel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
      picLabel.add(new JLabel(
          Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.18")
              + //$NON-NLS-1$
              "    " + //$NON-NLS-1$
              "   </html>")); //$NON-NLS-1$
      picLabel.add(new JLabel("            ")); //$NON-NLS-1$
      picLabel.add(progressText);
      actionsFinishedList = new JList(listModel);
      actionsFinishedList.setSize(60, 80);
      finishedActionsPnl.add(actionsFinishedList);

      closenessChartFrame.add(finishedActionsPnl);
      closenessChartFrame.addDecisionsAndCloseButton();

      closenessChartFrame.pack();
      closenessChartFrame.setVisible(true);
    });
  }

  void getDataFromOPC()
  throws InterruptedException {
    while (true) {
      DynamicDataChart.exit.setEnabled(false);
      opcAccessApi.writeValueForTag(filter_p102, Boolean.TRUE); //water filtering
      opcAccessApi.writeValueForTag(FILT_Green_in, Boolean.FALSE);
      Thread.sleep(1000);
      if (filter_fake_risk_value > 0.3d) {
        notifier(String.format(Messages.getString(
                    "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.22")
                    + Messages.getString(
                    "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.23") +
                    //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString(
                        "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.24"),
                //$NON-NLS-1$
                Messages.getString(
                    "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.25"), Messages
                    .getString(
                        "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.26"),
                0.1), //$NON-NLS-1$ //$NON-NLS-2$
            0.3);
        opcAccessApi.writeValueForTag(FILT_Fault_in, !opcAccessApi.readBoolean(FILT_Fault_in)
            .value); //Warning
      } else {
        opcAccessApi.writeValueForTag(FILT_Fault_in, Boolean.FALSE); //warning
        opcAccessApi.writeValueForTag(FILT_Green_in, Boolean.TRUE);
      }
      if (filter_fake_risk_value > 0.8d) {
        filter_fake_risk_value = 0.3d;
        filter_fake_active_flag = false;
        opcAccessApi.writeValueForTag(filter_p102, Boolean.FALSE); //water filtering

        progressText.setText("0%"); //$NON-NLS-1$
        listModel.addElement(Messages.getString(
            "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.28")); //$NON-NLS-1$
        notifier(String.format(Messages.getString(
                    "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.29") + "->\n"
                    + //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString(
                        "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.31"),
                //$NON-NLS-1$
                Messages.getString(
                    "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.32"), Messages
                    .getString(
                        "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.33"),
                0.1), //$NON-NLS-1$ //$NON-NLS-2$
            filter_fake_risk_value);
        picLabel.updateUI();
        closenessChartFrame.revalidate();
        closenessChartFrame.repaint();
        progressText.setText(Messages.getString(
            "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.34")); //$NON-NLS-1$

        opcAccessApi.writeValueForTag(filter_open_rev_valve, Boolean.TRUE);
        opcAccessApi.writeValueForTag(filter_p101, Boolean.TRUE);
        Thread.sleep(5000);
        listModel.addElement(Messages.getString(
            "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.35")); //$NON-NLS-1$
        progressText.setText(Messages.getString(
            "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.36")); //$NON-NLS-1$
        opcAccessApi.writeValueForTag(filter_p101, Boolean.FALSE); //turn off blowing
        opcAccessApi.writeValueForTag(filter_open_rev_pump, Boolean.TRUE);
        Thread.sleep(10000);
        listModel.addElement(Messages.getString(
            "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.37")); //$NON-NLS-1$
        opcAccessApi.writeValueForTag(filter_open_rev_pump, Boolean.FALSE);
        opcAccessApi.writeValueForTag(filter_open_rev_valve, Boolean.FALSE);
        Thread.sleep(2000);
        listModel.addElement(Messages.getString(
            "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.38")); //$NON-NLS-1$

        progressText.setText("70%"); //$NON-NLS-1$
        opcAccessApi.writeValueForTag(filter_p101, Boolean.TRUE);
        Thread.sleep(5000);
        opcAccessApi.writeValueForTag(filter_p101, Boolean.FALSE);
        listModel.addElement(Messages.getString(
            "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.40")); //$NON-NLS-1$

        opcAccessApi.writeValueForTag(filter_p102, Boolean.TRUE);

        Thread.sleep(5000);
        progressText.setText(Messages.getString(
            "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.41")); //$NON-NLS-1$

        opcAccessApi.writeValueForTag(filter_p102, Boolean.FALSE);
        notifier(String.format(Messages.getString(
                    "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.42") + "->\n"
                    + //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString(
                        "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.44"),
                //$NON-NLS-1$
                Messages.getString(
                    "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.45"), Messages
                    .getString(
                        "FilterStationEmergencyPredictionFilterDestructionAfterOuterValve.46"),
                0.1), //$NON-NLS-1$ //$NON-NLS-2$
            0.1);

        opcAccessApi.writeValueForTag(FILT_Fault_in, Boolean.FALSE); //warning
        Thread.sleep(1000);
        DynamicDataChart.exit.setEnabled(true);
        opcAccessApi.writeValueForTag(FILT_Green_in, Boolean.TRUE);
        opcAccessApi.writeValueForTag(MIX_Green_in, Boolean.TRUE);
        opcAccessApi.writeValueForTag(REACTOR_Green_in, Boolean.TRUE);
        return;
      }
    }
  }
}