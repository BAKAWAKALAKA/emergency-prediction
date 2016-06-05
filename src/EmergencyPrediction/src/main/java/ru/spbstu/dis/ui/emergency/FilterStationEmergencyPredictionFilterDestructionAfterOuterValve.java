package ru.spbstu.dis.ui.emergency;

import com.google.common.net.HostAndPort;
import com.typesafe.config.Config;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.slf4j.LoggerFactory;
import ru.spbstu.dis.ConfigProvider;
import ru.spbstu.dis.opc.client.api.OpcClientApiFactory;
import ru.spbstu.dis.opc.client.api.opc.access.OpcAccessApi;
import ru.spbstu.dis.opc.client.api.opc.access.Tag;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    JLabel esType = new JLabel("<html><div style='text-align: center;'>Блокировка клапана<br>" + "(выпускного)</html>");
    Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
    fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
    Font boldUnderline = new Font("Tachoma", Font.BOLD, 22).deriveFont(fontAttributes);
    esType.setFont(boldUnderline);
    final JPanel titlePanel = new JPanel(new FlowLayout());
    titlePanel.add(esType, BorderLayout.CENTER);
    closenessChartFrame.add(titlePanel, 0);

    esType = new JLabel("Вероятность блокировки клапана:");
    boldUnderline = new Font("Tachoma", Font.PLAIN, 19).deriveFont(fontAttributes);
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
      JLabel actionRecommLabel = new JLabel("Рекомендуемые действия");
      Font boldUnderlineAction = new Font("Tachoma", Font.BOLD, 15).deriveFont(fontAttributes);
      actionRecommLabel.setFont(boldUnderlineAction);
      actionRecoms.add(actionRecommLabel);
      closenessChartFrame.add(actionRecoms);

      final String[] actions = {"1. Отключить подачу жидкости", "2. Включить продув в обратном " +
          "направлении",
          "3. Включить резервный насос", "4. Задать прямое направление течения жидкости",
          "5. Включить продув в прямом направлении"};
      JList recomActions = new JList(actions);

      final JPanel actionsPanel = new JPanel(new FlowLayout());
      actionsPanel.setBorder(BorderFactory.createCompoundBorder());
      actionsPanel.add(recomActions, BorderLayout.CENTER);
      closenessChartFrame.add(actionsPanel);

      final JPanel actionOutput = new JPanel(new FlowLayout());
      JLabel esTypeAction = new JLabel("<html><div style='text-align: center;'>ОТРАБОТКА НС:<br>Очистка клапана</html>",
          SwingConstants.CENTER);
      Font boldUnderlineBig = new Font("Tachoma", Font.BOLD, 18).deriveFont(fontAttributes);
      esTypeAction.setFont(boldUnderlineBig);
      actionOutput.add(esTypeAction);
      closenessChartFrame.add(actionOutput);

      final JPanel statePanel = new JPanel(new FlowLayout());
      JLabel stateLbl = new JLabel("Текущее состояние выпускного клапана:");
      stateLbl.setFont(new Font("Tachoma", Font.PLAIN, 10));
      statePanel.add(stateLbl);
      closenessChartFrame.add(statePanel);

      JPanel finishedActionsPnl = new JPanel(new FlowLayout());
      BufferedImage myPicture = null;
      try {
        myPicture = ImageIO.read(
            FilterStationEmergencyPredictionFoulBlockage.class.getResource("/filter_outpu_error.png"));
      } catch (IOException e) {
        e.printStackTrace();
      }
      picLabel = new JLabel(new ImageIcon(myPicture));
      finishedActionsPnl.add(picLabel);
      picLabel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
      picLabel.add(new JLabel("<html><br><br><br><br><br><br><br><br><br><br><br><br><br>      " +
          "    " +
          "   </html>"));
      picLabel.add(new JLabel("            "));
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
      opcAccessApi.writeValueForTag(filter_p102, Boolean.TRUE); //water filtering

      Thread.sleep(1000);
      if (filter_fake_risk_value > 0.3d) {
        notifier(String.format("Вероятность НС на ст.фильтр. =%s " + "->\n" +
                    "Рекомендуемое действие=%s",
                "СРЕДНЯЯ", "Проверка станции", 0.1),
            0.3);
        opcAccessApi.writeValueForTag(filter_TP_1M7, Boolean.TRUE); //Warning
      } else {
        opcAccessApi.writeValueForTag(filter_TP_1M7, Boolean.FALSE); //warning
      }

      if (filter_fake_risk_value > 0.8d) {
        filter_fake_risk_value = 0.3d;
        filter_fake_active_flag = false;
        opcAccessApi.writeValueForTag(filter_p102, Boolean.FALSE); //water filtering

        progressText.setText("0%");
        listModel.addElement("<html>1.Отключить подачу<br> жидкости</html>");
        notifier(String.format("Вероятность НС на ст.фильтр. =%s " + "->\n" +
                    "Рекомендуемое действие=%s",
                "ВЫСОКАЯ", "Сброс давления в фильтре, отключение насосов", 0.1),
            filter_fake_risk_value);
        picLabel.updateUI();
        closenessChartFrame.revalidate();
        closenessChartFrame.repaint();
        progressText.setText("20%");
        listModel.addElement("<html>1.Отключить подачу<br>жидкости</html>");

        opcAccessApi.writeValueForTag(filter_open_rev_valve, Boolean.TRUE);
        opcAccessApi.writeValueForTag(filter_p101, Boolean.TRUE);
        Thread.sleep(5000);
        listModel.addElement("<html>2.Включить продув<br>в обратном<br>направлении</html>");
        progressText.setText("50%");
        opcAccessApi.writeValueForTag(filter_p101, Boolean.FALSE); //turn off blowing
        opcAccessApi.writeValueForTag(filter_open_rev_pump, Boolean.TRUE);
        Thread.sleep(10000);
        listModel.addElement("<html>3. Включить резервный<br>насос</html>");
        opcAccessApi.writeValueForTag(filter_open_rev_pump, Boolean.FALSE);
        opcAccessApi.writeValueForTag(filter_open_rev_valve, Boolean.FALSE);
        Thread.sleep(2000);
        listModel.addElement("<html>4.Задать прямое<br>направление течения<br>жидкости</html>");

        progressText.setText("70%");
        opcAccessApi.writeValueForTag(filter_p101, Boolean.TRUE);
        Thread.sleep(5000);
        opcAccessApi.writeValueForTag(filter_p101, Boolean.FALSE);
        listModel.addElement("<html>5. Включить продув в<br>прямом направлении</html>");

        opcAccessApi.writeValueForTag(filter_p102, Boolean.TRUE);

        Thread.sleep(5000);
        progressText.setText("100%");

        opcAccessApi.writeValueForTag(filter_p102, Boolean.FALSE);
        notifier(String.format("Вероятность НС на ст.фильтр. =%s " + "->\n" +
                    "Рекомендуемое действие=%s",
                "НИЗКАЯ", "Штатный режим", 0.1),
            0.1);

        opcAccessApi.writeValueForTag(filter_TP_1M7, Boolean.FALSE); //warning
        Thread.sleep(1000);
        return;
      }
    }
  }
}