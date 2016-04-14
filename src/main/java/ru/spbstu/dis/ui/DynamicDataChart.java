package ru.spbstu.dis.ui;

/**
 * Created by a.fedorov on 27.03.2016.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demonstration application showing a time series chart where you can dynamically add (random)
 * data by clicking on a button.
 */
public class DynamicDataChart extends ApplicationFrame implements ActionListener {

  private XYPlot plot;

  private TimeSeries series;

  private ChartPanel chartPanel;

  private double lastValue = 0;

  final JPanel content = new JPanel(new FlowLayout());

  /**
   * Constructs a new demonstration application.
   * @param title the frame title.
   */
  public DynamicDataChart(final String title) {
    super(title);
    content.setLayout(new FlowLayout());
    final JPanel titlePanel = new JPanel();

    this.setSeries(new TimeSeries(title, Millisecond.class));
    final TimeSeriesCollection dataset = new TimeSeriesCollection(this.getSeries());
    final JFreeChart chart = createChart(dataset, title);
    chart.setNotify(true);
    chart.setTextAntiAlias(true);
    chart.setBorderVisible(false);
    setChartPanel(new ChartPanel(chart));

    content.add(titlePanel);
    content.add(getChartPanel());

    getChartPanel().setPreferredSize(new java.awt.Dimension(300, 300));
    setContentPane(content);
    final Random random = new Random();
    chart.setBackgroundPaint(new GradientPaint(0, 0, Color.white, 0, 1000,
        new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))));
    chart.setAntiAlias(true);
    content.setSize(1200, 900);
    setSize(1200, 900);
  }

  public void addChart(ChartPanel chart) {
    content.add(chart);
  }

  public void addButton() {
    final JButton button = new JButton("Exit");
    button.setBackground(new Color(180, 11, 0));
    button.setSize(300, 80);
    button.setActionCommand("ADD_DATA");
    button.addActionListener(this);
    content.add(button);
  }

  /**
   * Creates a sample chart.
   * @param dataset the dataset.
   * @return A sample chart.
   */
  private JFreeChart createChart(final XYDataset dataset, String valueTitle) {
    final JFreeChart result = ChartFactory.createTimeSeriesChart(
        valueTitle,
        "Время",
        valueTitle,
        dataset,
        true,
        true,
        false
    );
    plot = result.getXYPlot();
    ValueAxis axis = plot.getDomainAxis();
    axis.setAutoRange(true);
    axis.setFixedAutoRange(60000.0);  // 60 seconds
    axis = plot.getRangeAxis();
    axis.setAutoRange(true);
    return result;
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
   * Handles a click on the button by adding new (random) data.
   * @param e the action event.
   */
  public void actionPerformed(final ActionEvent e) {
    if (e.getActionCommand().equals("ADD_DATA")) {
      System.exit(0);
    }
  }

  /**
   * Starting point for the demonstration application.
   * @param args ignored.
   */
  public static void main(final String[] args) {

    final DynamicDataChart demo = new DynamicDataChart("Dynamic Data Demo");

    Thread th = new Thread(() -> {
      while (true) {
        final double factor = 0.90 + 0.2 * Math.random();
        demo.setLastValue(demo.getLastValue() * factor);
        final Millisecond now = new Millisecond();
        System.out.println("Now = " + now.toString());
        demo.getSeries().add(new Millisecond(), demo.getLastValue());
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
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
      }
    });
  }

  /** The most recent value added. */
  public double getLastValue() {
    return lastValue;
  }

  public void setLastValue(final double lastValue) {
    this.lastValue = lastValue;
  }

  /** The time series data. */
  public TimeSeries getSeries() {
    return series;
  }

  public void setSeries(final TimeSeries series) {
    this.series = series;
  }

  public ChartPanel getChartPanel() {
    return chartPanel;
  }

  public void setChartPanel(final ChartPanel chartPanel) {
    this.chartPanel = chartPanel;
  }

  public XYPlot getPlot() {
    return plot;
  }

  public void setPlot(final XYPlot plot) {
    this.plot = plot;
  }
}




