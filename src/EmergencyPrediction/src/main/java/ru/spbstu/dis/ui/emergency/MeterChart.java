package ru.spbstu.dis.ui.emergency;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.ApplicationFrame;
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
    MeterPlot meterplot = new MeterPlot(dataset);
    meterplot.setRange(new Range(0.0D, 1D));
    meterplot.addInterval(new MeterInterval(Messages.getString("MeterChart.0"), new Range(0.0D,0.3D), //$NON-NLS-1$
        Color.green, new BasicStroke(2.0F),new Color(0, 255, 0, 64) ));
    meterplot.addInterval(new MeterInterval(Messages.getString("MeterChart.1"), new Range(0.3D, 0.7D), //$NON-NLS-1$
        Color.yellow, new BasicStroke(2.0F), new Color(255, 255, 0, 64)));
    meterplot.addInterval(new MeterInterval(Messages.getString("MeterChart.2"), new Range(0.7D, 1D), //$NON-NLS-1$
        Color.red, new BasicStroke(2.0F),new Color(255, 0, 0, 128) ));

    meterplot.setNeedlePaint(Color.darkGray);
    meterplot.setDialBackgroundPaint(Color.white);
    meterplot.setDialOutlinePaint(Color.black);
    meterplot.setDialShape(DialShape.CHORD);
    meterplot.setMeterAngle(180);
    meterplot.setTickLabelsVisible(true);
    meterplot.setTickLabelFont(new Font("Tachoma", 1, 12)); //$NON-NLS-1$
    meterplot.setTickLabelPaint(Color.black);
    meterplot.setTickSize(5D);
    meterplot.setTickPaint(Color.gray);
    meterplot.setValuePaint(Color.black);
    meterplot.setValueFont(new Font("Tachoma", 1, 12)); //$NON-NLS-1$
    meterplot.setUnits(Messages.getString("MeterChart.5")); //$NON-NLS-1$
    JFreeChart jfreechart = new JFreeChart(title,
        JFreeChart.DEFAULT_TITLE_FONT, meterplot, true);
    // OPTIONAL CUSTOMISATION COMPLETED.
    // add the chart to a panel...
    chartPanel = new ChartPanel(jfreechart);
    chartPanel.setSize(150, 100);
    chartPanel.setPreferredSize(new java.awt.Dimension(150, 100));
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

    final MeterChart demo = new MeterChart("Thermometer Demo 2"); //$NON-NLS-1$
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

