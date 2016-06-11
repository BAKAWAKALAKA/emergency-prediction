package ru.spbstu.dis.ui.emergency;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class EmergencyPredictionWindowRunner {
  static LinkedList<DynamicDataChart> charts = new LinkedList<DynamicDataChart>();
  public static void main(String[] args) {
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Windows".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      // If Nimbus is not available, you can set the GUI to another look and feel.
    }

    JFrame frame = new JFrame("Типы НС");

    JPanel panel = new JPanel(new GridBagLayout());
GridBagConstraints c =new GridBagConstraints();

    JLabel label = new JLabel("Типы нештатных ситуаций:");
    label.setHorizontalAlignment(SwingConstants.CENTER);
    c.gridwidth=2;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx=0;
    c.gridy=0;
    panel.add(label,c);
    JButton button = new JButton();
    button.setText("Симуляция блокировки выпускного клапана");
    button.addActionListener(e -> FilterStationEmergencyPredictionFilterDestructionAfterOuterValve
        .main(args));
    c.gridwidth=1;
    c.gridx=0;
    c.gridy=1;
    panel.add(button,c);
    c.gridx=1;
    c.gridy=1;
    DynamicDataChart situationChart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=2;
    JButton button1 = new JButton();
    button1.setText("Симуляция эксплуатационного засора");
    button1.addActionListener(e -> FilterStationEmergencyPredictionOldFilterBlockage.main(args));
    panel.add(button1,c);
    c.gridx=1;
    c.gridy=2;
    DynamicDataChart situation2Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=3;
    JButton button2 = new JButton();
    button2.setText("Симуляция механического засора");
    button2.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button2,c);
    c.gridx=1;
    c.gridy=3;
    DynamicDataChart situation3Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=4;
    JButton button3 = new JButton();
    button3.setText("Симуляция блокировки входного клапана");
    button3.addActionListener(e -> FilterStationEmergencyPredictionFilterDestructionAfterInnerValve
        .main(args));
    panel.add(button3,c);
    c.gridx=1;
    c.gridy=4;
    DynamicDataChart situation4Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=5;
    JButton button4 = new JButton();
    button4.setText("Симуляция сбоя температурного режима");
    button4.addActionListener(e -> MixingStationEmergencyPredictionTempMode.main(args));
    panel.add(button4,c);
    button4.setEnabled(false);
    c.gridx=1;
    c.gridy=5;
    DynamicDataChart situation5Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=6;
    JButton button6 = new JButton();
    button6.setText("Симуляция неисправности нагревателя");
    button6.addActionListener(e -> MixingStationEmergencyPredictionTempMode.main(args));
    panel.add(button6,c);
    button6.setEnabled(false);
    c.gridx=1;
    c.gridy=6;
    DynamicDataChart situation6Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=7;
    JButton button7 = new JButton();
    button7.setText("Симуляция неисправности датчика давления");
    button7.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button7,c);
    button7.setEnabled(false);
    c.gridx=1;
    c.gridy=7;
    DynamicDataChart situation7Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=8;
    JButton button8 = new JButton();
    button8.setText("Симуляция сбоя системы охлаждения");
    button8.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button8,c);
    button8.setEnabled(false);
    c.gridx=1;
    c.gridy=8;
    DynamicDataChart situation8Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=9;
    JButton button9 = new JButton();
    button9.setText("Симуляция воздушной пробки");
    button9.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button9.setEnabled(false);
    panel.add(button9,c);
    c.gridx=1;
    c.gridy=9;
    DynamicDataChart situation9Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=10;
    JButton button10 = new JButton();
    button10.setText("Симуляция неисправности задвижек");
    button10.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button10.setEnabled(false);
    panel.add(button10,c);
    c.gridx=1;
    c.gridy=10;
    DynamicDataChart situation10Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=11;
    JButton button11 = new JButton();
    button11.setText("Симуляция отказа клапанов (reactor)");
    button11.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button11.setEnabled(false);
    panel.add(button11,c);
    c.gridx=1;
    c.gridy=11;
    DynamicDataChart situation11Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=12;
    JButton button12 = new JButton();
    button12.setText("Симуляция переполнения бака (mixing)");
    button12.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button12.setEnabled(false);
    panel.add(button12,c);
    c.gridx=1;
    c.gridy=12;
    DynamicDataChart situation12Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=13;
    JButton button13 = new JButton();
    button13.setText("Симуляция переполнения бака (reactor)");
    button13.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button13.setEnabled(false);
    panel.add(button13,c);
    c.gridx=1;
    c.gridy=13;
    DynamicDataChart situation13Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=14;
    JButton button14 = new JButton();
    button14.setText("Симуляция блокировки выпускного клапана");
    button14.addActionListener(e -> FilterStationEmergencyPredictionFilterDestructionAfterOuterValve
        .main(args));
    panel.add(button14,c);
    c.gridx=1;
    c.gridy=14;
    DynamicDataChart situation14Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=15;
    JButton button15 = new JButton();
    button15.setText("Симуляция отказа расходомера");
    button15.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button15.setEnabled(false);
    panel.add(button15,c);
    c.gridx=1;
    c.gridy=15;
    DynamicDataChart situation15Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=16;
    JButton button16 = new JButton();
    button16.setText("Симуляция нарушения дозирования");
    button16.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button16.setEnabled(false);
    panel.add(button16,c);
    c.gridx=1;
    c.gridy=16;
    DynamicDataChart situation17Chart = getDynamicDataChart(panel, c);
    JButton button17 = new JButton();
    button17.setText("Симуляция нарушения движения ленты");
    button17.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button17.setEnabled(false);
    panel.add(button17,c);
    c.gridx=0;
    c.gridy=18;

    JButton button18 = new JButton();
    button18.setText("Симуляция отказа детекторов емкости");
    button18.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button18.setEnabled(false);
    panel.add(button18,c);

    c.gridx=1;
    c.gridy=18;
    DynamicDataChart situation18Chart = getDynamicDataChart(panel, c);
    c.gridx=0;
    c.gridy=19;
    JButton button5 = new JButton();
    button5.setText("Симуляция неисправности нагревателя");
    button5.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button5,c);
    button5.setEnabled(false);
    c.gridx=1;
    c.gridy=19;
    DynamicDataChart situation19Chart = getDynamicDataChart(panel, c);

    Thread th = new Thread(() -> {
      while (true) {
        charts.forEach(chart -> {
          Random random = new Random();
          chart.setLastValue(random.nextInt(50)/100d);
          chart.getSeries().add(new Millisecond(), chart.getLastValue());

        });

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    th.start();

    JScrollPane panelPane = new JScrollPane(panel);
    frame.add(panelPane);
    frame.setSize(new Dimension(430,900));
    frame.setLocation(1030,0);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

  }

  private static DynamicDataChart getDynamicDataChart(final JPanel panel,
      final GridBagConstraints c) {
    DynamicDataChart dynamicDataChart = new DynamicDataChart(
        "");
    dynamicDataChart.getChartPanel().setPreferredSize(new Dimension(80, 60));
    panel.add(dynamicDataChart.getChartPanel(),c);
    charts.add(dynamicDataChart);
    XYPlot plot = (XYPlot) dynamicDataChart.getChartPanel().getChart().getPlot();
    XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
    renderer0.setBaseShapesVisible(false);
    plot.setRenderer(0, renderer0);
    Random rand = new Random();
    float r = rand.nextFloat();
    float g = rand.nextFloat();
    float b = rand.nextFloat();
    Color color = new Color(r,g,b);
    plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, color);
    plot.getRenderer().setSeriesPaint(0, color);
    plot.getRenderer().setSeriesStroke(0, new BasicStroke(3.0f));
    return dynamicDataChart;
  }
}