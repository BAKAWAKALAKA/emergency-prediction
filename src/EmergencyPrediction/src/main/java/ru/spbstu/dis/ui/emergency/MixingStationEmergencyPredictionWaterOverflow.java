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

public class MixingStationEmergencyPredictionWaterOverflow extends EmergencyPrediction{
  private static final org.slf4j.Logger LOGGER = LoggerFactory
      .getLogger(MixingStationEmergencyPredictionWaterOverflow.class);
  public MixingStationEmergencyPredictionWaterOverflow(final OpcAccessApi opcAccessApi) {
    super(opcAccessApi);
  }

  public static void main(String[] args) {

    setLookAndFeelType();
    MixingStationEmergencyPredictionWaterOverflow emergencyPredictionWindow =
        new MixingStationEmergencyPredictionWaterOverflow(createOpcApi());
    emergencyPredictionWindow.initMeterChart();
    //need to be called last!
    emergencyPredictionWindow.composeAllChartsIntoOne();

    emergencyPredictionWindow.runSimulation();
  }

  private void composeAllChartsIntoOne() {

    Thread th = new Thread(() -> {
      while (true) {
        if (filter_fake_active_flag) {
          filter_fake_risk_value += 0.2 * new Random().nextDouble();
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
    JLabel esType = new JLabel(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.0") + //$NON-NLS-1$
        Messages.getString("MixingStationEmergencyPredictionWaterOverflow.1") + //$NON-NLS-1$
        Messages.getString("MixingStationEmergencyPredictionWaterOverflow.2")); //$NON-NLS-1$
    Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
    fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
    Font boldUnderline = new Font("Tachoma", Font.BOLD, 22).deriveFont(fontAttributes); //$NON-NLS-1$
    esType.setFont(boldUnderline);
    final JPanel titlePanel = new JPanel(new FlowLayout());
    titlePanel.add(esType, BorderLayout.CENTER);
    closenessChartFrame.add(titlePanel, 0);

    esType = new JLabel(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.4")); //$NON-NLS-1$
    boldUnderline = new Font("Tachoma", Font.PLAIN, 19).deriveFont(fontAttributes); //$NON-NLS-1$
    esType.setFont(boldUnderline);
    final JPanel chartPanel = new JPanel(new FlowLayout());
    chartPanel.add(esType, BorderLayout.CENTER);
    closenessChartFrame.add(chartPanel, 1);


    XYPlot plot = (XYPlot) closenessChartFrame.getChartPanel().getChart().getPlot();
    XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
    renderer0.setBaseShapesVisible(false);
    XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
    renderer1.setBaseShapesVisible(false);
    plot.setRenderer(0, renderer0);
    plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red);
    plot.getRenderer().setSeriesPaint(0, Color.red);

    SwingUtilities.invokeLater(() -> {

      final JPanel actionRecoms = new JPanel(new FlowLayout());
      JLabel actionRecommLabel = new JLabel(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.6")); //$NON-NLS-1$
      Font boldUnderlineAction = new Font("Tachoma", Font.BOLD, 15).deriveFont(fontAttributes); //$NON-NLS-1$
      actionRecommLabel.setFont(boldUnderlineAction);
      actionRecoms.add(actionRecommLabel);
      closenessChartFrame.add(actionRecoms);

      final String[] actions = {Messages.getString("MixingStationEmergencyPredictionWaterOverflow.8"), Messages.getString("MixingStationEmergencyPredictionWaterOverflow.9"), //$NON-NLS-1$ //$NON-NLS-2$
          Messages.getString("MixingStationEmergencyPredictionWaterOverflow.10"), Messages.getString("MixingStationEmergencyPredictionWaterOverflow.11"), //$NON-NLS-1$ //$NON-NLS-2$
          Messages.getString("MixingStationEmergencyPredictionWaterOverflow.12")}; //$NON-NLS-1$
      JList recomActions = new JList(actions);

      final JPanel actionsPanel = new JPanel(new FlowLayout());
      actionsPanel.setBorder(BorderFactory.createCompoundBorder());
      actionsPanel.add(recomActions, BorderLayout.CENTER);
      closenessChartFrame.add(actionsPanel);

      final JPanel actionOutput = new JPanel(new FlowLayout());
      JLabel esTypeAction = new JLabel(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.13") + //$NON-NLS-1$
                                           Messages.getString("MixingStationEmergencyPredictionWaterOverflow.14"), //$NON-NLS-1$
                                       SwingConstants.CENTER);
      Font boldUnderlineBig = new Font("Tachoma", Font.BOLD, 22).deriveFont(fontAttributes); //$NON-NLS-1$
      esTypeAction.setFont(boldUnderlineBig);
      actionOutput.add(esTypeAction);
      actionOutput.setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, Color.black));
      closenessChartFrame.add(actionOutput);

      final JPanel statePanel = new JPanel(new FlowLayout());
      JLabel stateLbl = new JLabel(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.16")); //$NON-NLS-1$
      stateLbl.setFont(new Font("Tachoma", Font.PLAIN, 10)); //$NON-NLS-1$
      statePanel.add(stateLbl);
      closenessChartFrame.add(statePanel);

      JPanel finishedActionsPnl = new JPanel(new FlowLayout());
      BufferedImage myPicture = null;
      try {
        myPicture = ImageIO.read(
            MixingStationEmergencyPredictionWaterOverflow.class.getResource("/water_middle.png")); //$NON-NLS-1$
      } catch (IOException e) {
        e.printStackTrace();
      }
      picLabel = new JLabel(new ImageIcon(myPicture));
      finishedActionsPnl.add(picLabel);
      picLabel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
      picLabel.add(new JLabel("         ")); //$NON-NLS-1$
      picLabel.add(new JLabel("         ")); //$NON-NLS-1$
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
      if(opcAccessApi.readBoolean(MIX_2B2_sensor).value ==Boolean.FALSE) {
        opcAccessApi.writeValueForTag(FILT_1M6, Boolean.TRUE);
        opcAccessApi.writeValueForTag(filter_open_rev_pump, Boolean.TRUE);
      }
      Thread.sleep(1000);

      if (filter_fake_risk_value > 0.8d) {
        notifier(String.format(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.21") + "->\n" + //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString("MixingStationEmergencyPredictionWaterOverflow.23"), //$NON-NLS-1$
                Messages.getString("MixingStationEmergencyPredictionWaterOverflow.24"), Messages.getString("MixingStationEmergencyPredictionWaterOverflow.25"), 0.1), //$NON-NLS-1$ //$NON-NLS-2$
            0.3);
        BufferedImage myPicture = null;
        try {
          myPicture = ImageIO.read(MixingStationEmergencyPredictionWaterOverflow
              .class.getResource("/water_bad.png")); //$NON-NLS-1$
        } catch (IOException e) {
          e.printStackTrace();
        }
        picLabel.setIcon(new ImageIcon(myPicture));

        opcAccessApi.writeValueForTag(MIX_Fault_in, !opcAccessApi.readBoolean(MIX_Fault_in)
            .value); //Warning
      }
      else {
        opcAccessApi.writeValueForTag(MIX_Fault_in, Boolean.FALSE); //warning
        opcAccessApi.writeValueForTag(MIX_Green_in, Boolean.TRUE);
      }


      if (filter_fake_risk_value > 0.8d || opcAccessApi.readBoolean(MIX_2B2_sensor).value
          ==Boolean.TRUE) {
        filter_fake_risk_value = 0.1d;
        filter_fake_active_flag = false;

        opcAccessApi.writeValueForTag(FILT_1M6, Boolean.FALSE);
        opcAccessApi.writeValueForTag(filter_open_rev_pump, Boolean.FALSE);
        BufferedImage myPicture = null;
        try {
          myPicture = ImageIO.read(
              FilterStationEmergencyPredictionOldFilterBlockage.class
                  .getResource("/water_middle.png")); //$NON-NLS-1$
        } catch (IOException e) {
          e.printStackTrace();
        }
        picLabel.setIcon(new ImageIcon(myPicture));
        progressText.setText("0%"); //$NON-NLS-1$
        listModel.addElement(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.29")); //$NON-NLS-1$
        notifier(String.format(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.30") + "->\n" + //$NON-NLS-1$ //$NON-NLS-2$
                                   Messages.getString("MixingStationEmergencyPredictionWaterOverflow.32"), //$NON-NLS-1$
                               Messages.getString("MixingStationEmergencyPredictionWaterOverflow.33"), Messages.getString("MixingStationEmergencyPredictionWaterOverflow.34") + //$NON-NLS-1$ //$NON-NLS-2$
                         Messages.getString("MixingStationEmergencyPredictionWaterOverflow.35"), 0.1), //$NON-NLS-1$
                 filter_fake_risk_value);
        picLabel.updateUI();
        closenessChartFrame.revalidate();
        closenessChartFrame.repaint();
        progressText.setText("20%"); //$NON-NLS-1$
        opcAccessApi.writeValueForTag(FILT_1M6, Boolean.FALSE);
        opcAccessApi.writeValueForTag(filter_open_rev_pump, Boolean.FALSE);
        listModel.addElement(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.37")); //$NON-NLS-1$
        progressText.setText("50%"); //$NON-NLS-1$
        opcAccessApi.writeValueForTag(MIX_2M3, Boolean.TRUE);
        listModel.addElement(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.39")); //$NON-NLS-1$

        opcAccessApi.writeValueForTag(MIX_set_point_man, Float.valueOf(50)); //mixing pump p201
        // speed

        listModel.addElement(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.40")); //$NON-NLS-1$
        opcAccessApi.writeValueForTag(MIX_2M1, Boolean.TRUE);
        progressText.setText("70%"); //$NON-NLS-1$
        listModel.addElement(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.42")); //$NON-NLS-1$

        Thread.sleep(10000);
        progressText.setText("100%"); //$NON-NLS-1$
        try {
          myPicture = ImageIO.read(MixingStationEmergencyPredictionWaterOverflow
              .class.getResource("/water_good.png")); //$NON-NLS-1$
        } catch (IOException e) {
          e.printStackTrace();
        }
        picLabel.setIcon(new ImageIcon(myPicture));


        opcAccessApi.writeValueForTag(MIX_2M3, Boolean.FALSE);//2M3
        opcAccessApi.writeValueForTag(MIX_2M1, Boolean.FALSE);//2M1
        notifier(String.format(Messages.getString("MixingStationEmergencyPredictionWaterOverflow.45") + "->\n" + //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString("MixingStationEmergencyPredictionWaterOverflow.47"), //$NON-NLS-1$
                Messages.getString("MixingStationEmergencyPredictionWaterOverflow.48"), Messages.getString("MixingStationEmergencyPredictionWaterOverflow.49"), 0.1), //$NON-NLS-1$ //$NON-NLS-2$
            0.1);
        opcAccessApi.writeValueForTag(MIX_2M2, Boolean.TRUE); //mixing pump p202
        Thread.sleep(5000);
        opcAccessApi.writeValueForTag(MIX_2M2, Boolean.FALSE); //mixing pump p202
        opcAccessApi.writeValueForTag(MIX_Fault_in, Boolean.FALSE); //warning
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