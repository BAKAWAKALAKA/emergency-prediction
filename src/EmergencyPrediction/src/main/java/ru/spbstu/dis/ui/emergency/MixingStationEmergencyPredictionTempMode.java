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

public class MixingStationEmergencyPredictionTempMode extends EmergencyPrediction{
  private static final org.slf4j.Logger LOGGER = LoggerFactory
      .getLogger(MixingStationEmergencyPredictionTempMode.class);
  public MixingStationEmergencyPredictionTempMode(final OpcAccessApi opcAccessApi) {
    super(opcAccessApi);
  }

  public static void main(String[] args) {

    setLookAndFeelType();
    MixingStationEmergencyPredictionTempMode emergencyPredictionWindow =
        new MixingStationEmergencyPredictionTempMode(createOpcApi());
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
    JLabel esType = new JLabel("<html><div style='text-align: center;'>Сбой режима<br>" +
        "температуры" +
        " на станциях)</html>");
    Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
    fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
    Font boldUnderline = new Font("Tachoma", Font.BOLD, 22).deriveFont(fontAttributes);
    esType.setFont(boldUnderline);
    final JPanel titlePanel = new JPanel(new FlowLayout());
    titlePanel.add(esType, BorderLayout.CENTER);
    closenessChartFrame.add(titlePanel, 0);

    esType = new JLabel("Вероятность сбоя температурного режима:");
    boldUnderline = new Font("Tachoma", Font.PLAIN, 19).deriveFont(fontAttributes);
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
      JLabel actionRecommLabel = new JLabel("Рекомендуемые действия");
      Font boldUnderlineAction = new Font("Tachoma", Font.BOLD, 15).deriveFont(fontAttributes);
      actionRecommLabel.setFont(boldUnderlineAction);
      actionRecoms.add(actionRecommLabel);
      closenessChartFrame.add(actionRecoms);

      final String[] actions = {"1. Отключить фильтрацию", "2. Отключить смешивание",
          "3. Отключить соседние станции", "4. Включить циркуляцию в реакторе",
          "5. Включить циркуляцию в фильтре"};
      JList recomActions = new JList(actions);

      final JPanel actionsPanel = new JPanel(new FlowLayout());
      actionsPanel.setBorder(BorderFactory.createCompoundBorder());
      actionsPanel.add(recomActions, BorderLayout.CENTER);
      closenessChartFrame.add(actionsPanel);

      final JPanel actionOutput = new JPanel(new FlowLayout());
      JLabel esTypeAction = new JLabel("<html><div style='text-align: center;'>ОТРАБОТКА " +
                                           "НС:<br>Охлаждение оборудования</html>",
                                       SwingConstants.CENTER);
      Font boldUnderlineBig = new Font("Tachoma", Font.BOLD, 22).deriveFont(fontAttributes);
      esTypeAction.setFont(boldUnderlineBig);
      actionOutput.add(esTypeAction);
      closenessChartFrame.add(actionOutput);

      final JPanel statePanel = new JPanel(new FlowLayout());
      JLabel stateLbl = new JLabel("Текущее состояние оборудования:");
      stateLbl.setFont(new Font("Tachoma", Font.PLAIN, 10));
      statePanel.add(stateLbl);
      closenessChartFrame.add(statePanel);

      JPanel finishedActionsPnl = new JPanel(new FlowLayout());
      BufferedImage myPicture = null;
      try {
        myPicture = ImageIO.read(
            MixingStationEmergencyPredictionTempMode.class.getResource("/pump_inactive.png"));
      } catch (IOException e) {
        e.printStackTrace();
      }
      picLabel = new JLabel(new ImageIcon(myPicture));
      finishedActionsPnl.add(picLabel);
      picLabel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
      picLabel.add(new JLabel("         "));
      picLabel.add(new JLabel("         "));
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
      opcAccessApi.writeValueForTag(MIX_2M3, Boolean.TRUE); //mixing valve 2m3
      opcAccessApi.writeValueForTag(MIX_2M1, Boolean.TRUE); //mixing pump p201
      opcAccessApi.writeValueForTag(MIX_set_point_man, Float.valueOf(50)); //mixing pump p201
      opcAccessApi.writeValueForTag(MIX_2M2, Boolean.TRUE); //mixing pump p202

      Thread.sleep(5000);
      if (filter_fake_risk_value > 0.8d) {
        notifier(String.format("Вероятность НС на станциях =%s " + "->\n" +
                    "Рекомендуемое действие=%s",
                "СРЕДНЯЯ", "Проверка станций", 0.1),
            0.3);
        opcAccessApi.writeValueForTag(filter_TP_1M7, Boolean.TRUE); //Warning
      }
      else {
        opcAccessApi.writeValueForTag(filter_TP_1M7,  Boolean.FALSE); //warning
      }


      if (filter_fake_risk_value > 0.8d) {
        filter_fake_risk_value = 0.1d;
        filter_fake_active_flag = false;
        opcAccessApi.writeValueForTag(MIX_2M3, Boolean.FALSE); //mixing valve 2m3
        BufferedImage myPicture = null;
        try {
          myPicture = ImageIO.read(
              FilterStationEmergencyPredictionOldFilterBlockage.class
                  .getResource("/pump_active.png"));
        } catch (IOException e) {
          e.printStackTrace();
        }
        picLabel.setIcon(new ImageIcon(myPicture));
        progressText.setText("0%");
        listModel.addElement("<html>1.Отключить фильтрацию</html>");
        notifier(String.format("Вероятность НС на станциях =%s " + "->\n" +
                                   "Рекомендуемое действие=%s",
                               "ВЫСОКАЯ", "Сброс давления в фильтре, отключение насосов", 0.1),
                 filter_fake_risk_value);
        picLabel.updateUI();
        closenessChartFrame.revalidate();
        closenessChartFrame.repaint();
        progressText.setText("20%");
        opcAccessApi.writeValueForTag(MIX_2M1, Boolean.FALSE); //mixing pump p201
        listModel.addElement("<html>2.Отключить смешивание</html>");
        progressText.setText("50%");

        opcAccessApi.writeValueForTag(MIX_2M2, Boolean.FALSE); //mixing pump p202
        listModel.addElement("<html>3.Открыть соседние<br>станции</html>");
        opcAccessApi.writeValueForTag(REAC_3M2, Boolean.TRUE);//3M3

        listModel.addElement("<html>4.Включить циркуляцию<br>в реакторе</html>");
        opcAccessApi.writeValueForTag(FILT_1M6, Boolean.FALSE);
        opcAccessApi.writeValueForTag(filter_open_rev_valve, Boolean.FALSE);
        opcAccessApi.writeValueForTag(filter_open_rev_pump, Boolean.TRUE);//1M3
        progressText.setText("70%");
        listModel.addElement("<html>5.Включить циркуляцию<br>в фильтре</html>");

        Thread.sleep(10000);
        progressText.setText("100%");
        try {
          myPicture = ImageIO.read(MixingStationEmergencyPredictionTempMode
              .class.getResource("/pump_inactive.png"));
        } catch (IOException e) {
          e.printStackTrace();
        }
        picLabel.setIcon(new ImageIcon(myPicture));


        opcAccessApi.writeValueForTag(REAC_3M2, Boolean.FALSE);//3M3
        opcAccessApi.writeValueForTag(filter_open_rev_pump, Boolean.FALSE);//1M3
        notifier(String.format("Вероятность НС на станциях =%s " + "->\n" +
                    "Рекомендуемое действие=%s",
                "НИЗКАЯ", "Штатный режим", 0.1),
            0.1);

        opcAccessApi.writeValueForTag(filter_TP_1M7,  Boolean.FALSE); //warning
        Thread.sleep(1000);
        return;
      }
    }
  }

}