package ru.spbstu.dis.ui.emergency;

/**
 * Created by a.fedorov on 27.03.2016.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
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

  private boolean emergencyGrowth;

  private JList list;

  final JPanel content = new JPanel(new FlowLayout());

  /**
   * Constructs a new demonstration application.
   * @param title the frame title.
   */
  public DynamicDataChart(final String title) {
    super(title);
    super.setDefaultCloseOperation(HIDE_ON_CLOSE);
    setDefaultCloseOperation(HIDE_ON_CLOSE);
    content.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1), Color.black));
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

    this.setSeries(new TimeSeries(title, Millisecond.class));
    final TimeSeriesCollection dataset = new TimeSeriesCollection(this.getSeries());
    final JFreeChart chart = createChart(dataset, "");
    chart.setNotify(true);
    chart.setTextAntiAlias(true);
    chart.setBorderVisible(false);
    setChartPanel(new ChartPanel(chart));

    content.add(getChartPanel(), BorderLayout.NORTH);

    getChartPanel().setPreferredSize(new java.awt.Dimension(300, 170));
    setContentPane(content);
    chart.setAntiAlias(true);
    content.setSize(300, 900);
    setSize(300, 900);
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
    plot.setRangeGridlinesVisible(true);
    ValueAxis axis = plot.getDomainAxis();
    axis.setAutoRange(true);
    axis.setFixedAutoRange(60000.0);  // 60 seconds
    axis = plot.getRangeAxis();
    axis.setAutoRange(true);
    LegendTitle legend = new LegendTitle(this.plot);
//    legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
    legend.setBorder(new BlockBorder());
    legend.setBackgroundPaint(Color.white);
    legend.setPosition(RectangleEdge.LEFT);
    return result;
  }

  /** The time series data. */
  public TimeSeries getSeries() {
    return series;
  }

  public void setSeries(final TimeSeries series) {
    this.series = series;
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

  public ChartPanel getChartPanel() {
    return chartPanel;
  }

  public void setChartPanel(final ChartPanel chartPanel) {
    this.chartPanel = chartPanel;
  }

  public void addChart(ChartPanel chart) {
    content.add(chart);
  }

  public void addDecisionsAndCloseButton() {
    //        setContentPane(panel);
    list = new JList();
    list.setCellRenderer(new WhiteYellowCellRenderer());
    Object[] data = new Object[10];

    list.setListData(data);
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
    p.add(new JLabel("Сообщения о близости к НС"));
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setSize(50, 50);
    list.setFixedCellHeight(20);
    list.setFixedCellWidth(50);
    scrollPane.setViewportView(list);
    p.add(scrollPane);
    content.add(p);

    final JButton button = new JButton("Exit");
    button.setBackground(new Color(223, 203, 201));
    button.setSize(300, 80);
    button.setActionCommand("ADD_DATA");
    button.addActionListener(this);
    final JPanel titlePanel = new JPanel(new FlowLayout());
    titlePanel.add(button);
    content.add(titlePanel);
    setSize(480, 900);
    setPreferredSize(new Dimension(430,900));
    setLocation(1030,0);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
  }

  /**
   * Handles a click on the button by adding new (random) data.
   * @param e the action event.
   */
  public void actionPerformed(final ActionEvent e) {
    if (e.getActionCommand().equals("ADD_DATA")) {
      this.hide();
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

  private static class WhiteYellowCellRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
      Component c = super.getListCellRendererComponent(list,
          value,
          index,
          isSelected,
          cellHasFocus);

      if (value != null) {
        if (value.toString().toLowerCase()
            .contains("низкая")) {
          c.setBackground(new Color(144, 198, 37));
        }
        if (value.toString().toLowerCase()
            .contains("средняя")) {
          c.setBackground(new Color(244, 246, 29));
        } if (value.toString().toLowerCase()
            .contains("высокая")) {
          c.setBackground(new Color(246, 100, 8));
        }
      }
      return c;
    }
  }

  public XYPlot getPlot() {
    return plot;
  }

  public JList getList() {
    return list;
  }

  public void setList(JList list) {
    this.list = list;
  }

  public boolean isEmergencyGrowth() {
    return emergencyGrowth;
  }

  public void setEmergencyGrowth(boolean emergencyGrowth) {
    this.emergencyGrowth = emergencyGrowth;
  }

  public void setPlot(final XYPlot plot) {
    this.plot = plot;
  }
}




