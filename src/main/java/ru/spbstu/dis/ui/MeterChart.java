package ru.spbstu.dis.ui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.time.DateRange;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import java.awt.*;

/**
 * A demonstration application for the thermometer plot.
 * @author Bryan Scott
 */
public class MeterChart extends ApplicationFrame {

  Double value = 0d;

  final ChartPanel chartPanel;

  final DefaultValueDataset dataset;

  /**
   * Creates a new demo.
   * @param title the frame title.
   */
  public MeterChart(final String title) {

    super(title);

    // create a dataset...
    dataset = new DefaultValueDataset(value);

    // create the chart...

    final org.jfree.chart.plot.MeterPlot plot = new org.jfree.chart.plot.MeterPlot(dataset);
    final JFreeChart chart = new JFreeChart(title,  // chart title
        JFreeChart.DEFAULT_TITLE_FONT,
        plot,                 // plot
        false);               // include legend
    // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
    plot.setInsets(new RectangleInsets(5, 5, 5, 5));
    plot.setRange(new DateRange(0, 100));
    // OPTIONAL CUSTOMISATION COMPLETED.
    // add the chart to a panel...
    chartPanel = new ChartPanel(chart);
    chartPanel.setSize(150, 150);
    chartPanel.setPreferredSize(new Dimension(300, 300));
    setContentPane(chartPanel);
  }

  // ****************************************************************************
  // * JFREECHART DEVELOPER GUIDE                                               *
  // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
  // * to purchase from Object Refinery Limited:                                *
  // *                                                                          *
  // * http://www.object-refinery.com/jfreechart/guide.html                     *
  // *                                                                          *
  // * Sales are used to provide funding for the JFreeChart project - please    *
  // * support us so that we can continue developing free software.             *
  // ****************************************************************************

  /**
   * Starting point for the demonstration application.
   * @param args ignored.
   */
  public static void main(final String[] args) {

    final MeterChart demo = new MeterChart("Thermometer Demo 2");
    demo.pack();
    demo.setVisible(true);
  }

  public Double getValue() {
    return this.value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public ChartPanel getChartPanel() {
    return this.chartPanel;
  }

  public DefaultValueDataset getDataset() {
    return dataset;
  }
}

