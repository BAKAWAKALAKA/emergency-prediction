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

public class FilterStationEmergencyPredictionFilterDestructionAfterInnerValve
    extends EmergencyPrediction {
  private static final org.slf4j.Logger LOGGER = LoggerFactory
      .getLogger(FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.class);

  public FilterStationEmergencyPredictionFilterDestructionAfterInnerValve(final OpcAccessApi
      opcAccessApi) {
    super(opcAccessApi);
  }

  public static void main(String[] args) {

    setLookAndFeelType();
    FilterStationEmergencyPredictionFilterDestructionAfterInnerValve emergencyPredictionWindow =
        new FilterStationEmergencyPredictionFilterDestructionAfterInnerValve(createOpcApi());
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

    JLabel esType = new JLabel(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.0") + Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.1")); //$NON-NLS-1$ //$NON-NLS-2$
    esType.setHorizontalAlignment(SwingConstants.CENTER);
    Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
    fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
    Font boldUnderline = new Font("Tachoma", Font.BOLD, 22).deriveFont(fontAttributes); //$NON-NLS-1$
    esType.setFont(boldUnderline);
    final JPanel titlePanel = new JPanel(new FlowLayout());
    titlePanel.add(esType, BorderLayout.CENTER);
    closenessChartFrame.add(titlePanel, 0);

    esType = new JLabel(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.3")); //$NON-NLS-1$
    boldUnderline = new Font("Tachoma", Font.PLAIN, 19).deriveFont(fontAttributes); //$NON-NLS-1$
    esType.setFont(boldUnderline);
    final JPanel chartPanel = new JPanel(new FlowLayout());
    chartPanel.add(esType, BorderLayout.CENTER);
    closenessChartFrame.add(chartPanel, 1);

    XYPlot plot = (XYPlot) closenessChartFrame.getChartPanel().getChart().getPlot();
    XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
    renderer0.setBaseShapesVisible(false);
    plot.setRenderer(0, renderer0);
    plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.BLUE);
    plot.getRenderer().setSeriesPaint(0, Color.BLUE);
    plot.getRenderer().setSeriesStroke(0, new BasicStroke(3.0f));
    SwingUtilities.invokeLater(() -> {

      final JPanel actionRecoms = new JPanel(new FlowLayout());
      JLabel actionRecommLabel = new JLabel(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.5")); //$NON-NLS-1$
      Font boldUnderlineAction = new Font("Tachoma", Font.BOLD, 15).deriveFont(fontAttributes); //$NON-NLS-1$
      actionRecommLabel.setFont(boldUnderlineAction);
      actionRecoms.add(actionRecommLabel);
      closenessChartFrame.add(actionRecoms);

      final String[] actions = {Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.7"), Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.8") + //$NON-NLS-1$ //$NON-NLS-2$
          Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.9"), //$NON-NLS-1$
          Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.10"), Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.11"), //$NON-NLS-1$ //$NON-NLS-2$
          Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.12")}; //$NON-NLS-1$
      JList recomActions = new JList(actions);

      final JPanel actionsPanel = new JPanel(new FlowLayout());
      actionsPanel.setBorder(BorderFactory.createCompoundBorder());
      actionsPanel.add(recomActions, BorderLayout.CENTER);
      closenessChartFrame.add(actionsPanel);
      final JPanel actionOutput = new JPanel(new FlowLayout());
      JLabel esTypeAction = new JLabel(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.13"), //$NON-NLS-1$
          SwingConstants.CENTER);
      Font boldUnderlineBig = new Font(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.14"), Font.BOLD, 18).deriveFont(fontAttributes); //$NON-NLS-1$
      esTypeAction.setFont(boldUnderlineBig);
      actionOutput.add(esTypeAction);
      actionOutput.setBorder(BorderFactory.createMatteBorder(
              4, 0, 0, 0, Color.black));
      closenessChartFrame.add(actionOutput);

      final JPanel statePanel = new JPanel(new FlowLayout());
      JLabel stateLbl = new JLabel(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.15")); //$NON-NLS-1$
      stateLbl.setFont(new Font(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.16"), Font.PLAIN, 10)); //$NON-NLS-1$
      statePanel.add(stateLbl);
      closenessChartFrame.add(statePanel);

      JPanel finishedActionsPnl = new JPanel(new FlowLayout());
      BufferedImage myPicture = null;
      try {
        myPicture = ImageIO.read(
            FilterStationEmergencyPredictionFoulBlockage.class.getResource("/filter_input_error.png")); //$NON-NLS-1$
      } catch (IOException e) {
        e.printStackTrace();
      }
      picLabel = new JLabel(new ImageIcon(myPicture));
      finishedActionsPnl.add(picLabel);
      picLabel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
      picLabel.add(new JLabel("<html><br><br><br><br><br><br><br><br><br><br><br><br><br><br>    " + //$NON-NLS-1$
          " " + //$NON-NLS-1$
          "      " + //$NON-NLS-1$
          "    " + //$NON-NLS-1$
          "   </html>")); //$NON-NLS-1$
      picLabel.add(new JLabel("                 ")); //$NON-NLS-1$
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
        notifier(String.format(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.24") + "->\n" + //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.26"), //$NON-NLS-1$
                Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.27"), Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.28"), 0.1), //$NON-NLS-1$ //$NON-NLS-2$
            0.3);
        opcAccessApi.writeValueForTag(FILT_Fault_in, !opcAccessApi.readBoolean(FILT_Fault_in)
            .value); //Warning
      }
      else {
        opcAccessApi.writeValueForTag(FILT_Fault_in, Boolean.FALSE); //warning
        opcAccessApi.writeValueForTag(FILT_Green_in, Boolean.TRUE);
      }

      if (filter_fake_risk_value > 0.8d) {
        filter_fake_risk_value = 0.3d;
        filter_fake_active_flag = false;
        opcAccessApi.writeValueForTag(filter_p102, Boolean.FALSE); //water filtering

        progressText.setText("0%"); //$NON-NLS-1$
        listModel.addElement(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.30")); //$NON-NLS-1$
        notifier(String.format(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.31") + "->\n" + //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.33"), //$NON-NLS-1$
                Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.34"), Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.35"), 0.1), //$NON-NLS-1$ //$NON-NLS-2$
            filter_fake_risk_value);
        picLabel.updateUI();
        closenessChartFrame.revalidate();
        closenessChartFrame.repaint();
        progressText.setText("20%"); //$NON-NLS-1$

        opcAccessApi.writeValueForTag(filter_open_rev_valve, Boolean.TRUE);
        listModel.addElement(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.37")); //$NON-NLS-1$
        progressText.setText("50%"); //$NON-NLS-1$
        opcAccessApi.writeValueForTag(filter_open_rev_pump, Boolean.TRUE);
        Thread.sleep(10000);
        opcAccessApi.writeValueForTag(filter_open_rev_pump, Boolean.FALSE);
        listModel.addElement(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.39")); //$NON-NLS-1$
        opcAccessApi.writeValueForTag(filter_p101, Boolean.TRUE); //turn on blowing
        Thread.sleep(5000);
        listModel.addElement(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.40")); //$NON-NLS-1$
        opcAccessApi.writeValueForTag(filter_p101, Boolean.FALSE);//turn off blowing
        opcAccessApi.writeValueForTag(filter_open_rev_valve, Boolean.FALSE);
        Thread.sleep(2000);
        progressText.setText(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.41")); //$NON-NLS-1$

        listModel.addElement(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.42")); //$NON-NLS-1$

        opcAccessApi.writeValueForTag(filter_p102, Boolean.TRUE);

        Thread.sleep(5000);
        progressText.setText("100%"); //$NON-NLS-1$

        opcAccessApi.writeValueForTag(filter_p102, Boolean.FALSE);
        notifier(String.format(Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.44") + "->\n" + //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.46"), //$NON-NLS-1$
                Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.47"), Messages.getString("FilterStationEmergencyPredictionFilterDestructionAfterInnerValve.48"), 0.1), //$NON-NLS-1$ //$NON-NLS-2$
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