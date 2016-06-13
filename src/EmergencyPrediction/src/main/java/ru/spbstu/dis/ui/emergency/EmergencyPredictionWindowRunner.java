package ru.spbstu.dis.ui.emergency;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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

        JFrame frame = new JFrame("ШТАТНЫЙ РЕЖИМ РАБОТЫ ИСУ ТП");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel label = new JLabel("Состояние системы:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridwidth = 2;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.5;
        c.weighty = 0.0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints
                .CENTER;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        panel.add(label, c);
        JButton button = new JButton();
        button.setText("Симуляция блокировки выпускного клапана");
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        addButtonToPanel(panel, c, button);
        c.gridx = 1;
        c.gridy = 1;
        DynamicDataChart situationChart = getDynamicDataChart(panel, c);
        waitForEmergency(button, e -> {
            situationChart.setEmergencyGrowth(true);
            Thread th = new Thread(() -> {
                while (situationChart.getLastValue() < 0.3d) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                FilterStationEmergencyPredictionFilterDestructionAfterOuterValve
                        .main(args);
                situationChart.setEmergencyGrowth(false);
            });
            th.start();

        });
        c.gridx = 0;
        c.gridy = 2;
        JButton button1 = new JButton();
        button1.setText("Симуляция эксплуатационного засора");
        addButtonToPanel(panel, c, button1);
        c.gridx = 1;
        c.gridy = 2;
        DynamicDataChart situation2Chart = getDynamicDataChart(panel, c);
        button1.addActionListener(e -> {
            situation2Chart.setEmergencyGrowth(true);
            Thread th = new Thread(() -> {
                while (situation2Chart.getLastValue() < 0.3d) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                FilterStationEmergencyPredictionOldFilterBlockage
                        .main(args);
                situation2Chart.setEmergencyGrowth(false);
            });
            th.start();
        });
        c.gridx = 0;
        c.gridy = 3;
        JButton button2 = new JButton();
        button2.setText("Симуляция механического засора");

        addButtonToPanel(panel, c, button2);
        c.gridx = 1;
        c.gridy = 3;
        DynamicDataChart situation3Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button2, e -> {
            situation3Chart.setEmergencyGrowth(true);
            Thread th = new Thread(() -> {
                while (situation3Chart.getLastValue() < 0.3d) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                FilterStationEmergencyPredictionFoulBlockage
                        .main(args);
                situation3Chart.setEmergencyGrowth(false);
            });
            th.start();
        });
        c.gridx = 0;
        c.gridy = 4;
        JButton button3 = new JButton();
        button3.setText("Симуляция блокировки входного клапана");
        addButtonToPanel(panel, c, button3);
        c.gridx = 1;
        c.gridy = 4;
        DynamicDataChart situation4Chart = getDynamicDataChart(panel, c);
        button3.addActionListener(e -> {
            situation4Chart.setEmergencyGrowth(true);
            Thread th = new Thread(() -> {
                while (situation4Chart.getLastValue() < 0.3d) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                FilterStationEmergencyPredictionFilterDestructionAfterInnerValve
                        .main(args);
                situation4Chart.setEmergencyGrowth(false);
            });
            th.start();

        });
        c.gridx = 0;
        c.gridy = 5;
        JButton button4 = new JButton();
        button4.setText("Симуляция сбоя температурного режима");
        addButtonToPanel(panel, c, button4);
        // button4.setEnabled(false);
        c.gridx = 1;
        c.gridy = 5;
        DynamicDataChart situation5Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button4, e -> {
            situation5Chart.setEmergencyGrowth(true);
            //MixingStationEmergencyPredictionTempMode.main(args);
            Thread th = new Thread(() -> {
                while (situation5Chart.getLastValue() < 0.3d) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                MixingStationEmergencyPredictionTempMode
                        .main(args);
                situation5Chart.setEmergencyGrowth(false);
            });
            th.start();
        });
        c.gridx = 0;
        c.gridy = 6;
        JButton button6 = new JButton();
        button6.setText("Симуляция переполнения дозировочного бака");

        addButtonToPanel(panel, c, button6);
        // button6.setEnabled(false);
        c.gridx = 1;
        c.gridy = 6;
        DynamicDataChart situation6Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button6, e -> {
            situation6Chart.setEmergencyGrowth(true);
            Thread th = new Thread(() -> {
                while (situation6Chart.getLastValue() < 0.3d) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                MixingStationEmergencyPredictionWaterOverflow
                        .main(args);
                situation6Chart.setEmergencyGrowth(false);
            });
            th.start();
        });
        c.gridx = 0;
        c.gridy = 7;
        JButton button7 = new JButton();
        button7.setText("Симуляция перегрева реактора");

        addButtonToPanel(panel, c, button7);
        //button7.setEnabled(false);
        c.gridx = 1;
        c.gridy = 7;
        DynamicDataChart situation7Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button7, e -> {
            situation7Chart.setEmergencyGrowth(true);
            Thread th = new Thread(() -> {
                while (situation7Chart.getLastValue() < 0.3d) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                ReactorStationEmergencyPredictionTempMode
                        .main(args);
                situation7Chart.setEmergencyGrowth(false);
            });
            th.start();
        });
        c.gridx = 0;
        c.gridy = 8;
        JButton button8 = new JButton();
        button8.setText("Симуляция сбоя системы охлаждения");
        addButtonToPanel(panel, c, button8);
        button8.setEnabled(false);
        c.gridx = 1;
        c.gridy = 8;
        DynamicDataChart situation8Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button8, e -> {
            situation8Chart.setEmergencyGrowth(true);
            Thread th = new Thread(() -> {
                while (situation8Chart.getLastValue() < 0.3d) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                FilterStationEmergencyPredictionFoulBlockage
                        .main(args);
                situation8Chart.setEmergencyGrowth(false);
            });
            th.start();
        });
        c.gridx = 0;
        c.gridy = 9;
        JButton button9 = new JButton();
        button9.setText("Симуляция воздушной пробки");

        button9.setEnabled(false);
        addButtonToPanel(panel, c, button9);
        c.gridx = 1;
        c.gridy = 9;
        DynamicDataChart situation9Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button9, e -> {
            situation9Chart.setEmergencyGrowth(true);
            Thread th = new Thread(() -> {
                while (situation9Chart.getLastValue() < 0.3d) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                FilterStationEmergencyPredictionFoulBlockage
                        .main(args);
                situation9Chart.setEmergencyGrowth(false);
            });
            th.start();
        });
        c.gridx = 0;
        c.gridy = 10;
        JButton button10 = new JButton();
        button10.setText("Симуляция неисправности задвижек");

        button10.setEnabled(false);
        addButtonToPanel(panel, c, button10);
        c.gridx = 1;
        c.gridy = 10;
        DynamicDataChart situation10Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button10, e -> {
            situation10Chart.setEmergencyGrowth(true);
            FilterStationEmergencyPredictionFoulBlockage.main(args);
        });
        c.gridx = 0;
        c.gridy = 11;
        JButton button11 = new JButton();
        button11.setText("Симуляция отказа клапанов (reactor)");

        button11.setEnabled(false);
        addButtonToPanel(panel, c, button11);
        c.gridx = 1;
        c.gridy = 11;
        DynamicDataChart situation11Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button11, e -> {
            situation11Chart.setEmergencyGrowth(true);
            FilterStationEmergencyPredictionFoulBlockage.main(args);
        });
        c.gridx = 0;
        c.gridy = 12;
        JButton button12 = new JButton();
        button12.setText("Симуляция переполнения бака (mixing)");

        button12.setEnabled(false);
        addButtonToPanel(panel, c, button12);
        c.gridx = 1;
        c.gridy = 12;
        DynamicDataChart situation12Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button12, e -> {
            situation12Chart.setEmergencyGrowth(true);
            FilterStationEmergencyPredictionFoulBlockage.main(args);
        });
        c.gridx = 0;
        c.gridy = 13;
        JButton button13 = new JButton();
        button13.setText("Симуляция переполнения бака (reactor)");

        button13.setEnabled(false);
        addButtonToPanel(panel, c, button13);
        c.gridx = 1;
        c.gridy = 13;
        DynamicDataChart situation13Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button13, e -> {
            situation13Chart.setEmergencyGrowth(true);
            FilterStationEmergencyPredictionFoulBlockage.main(args);
        });
        c.gridx = 0;
        c.gridy = 14;
        JButton button14 = new JButton();
        button14.setText("Симуляция блокировки выпускного клапана");

        addButtonToPanel(panel, c, button14);
        c.gridx = 1;
        c.gridy = 14;
        DynamicDataChart situation14Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button14, e -> {
            situation14Chart.setEmergencyGrowth(true);
            Thread th = new Thread(() -> {
                while (situation14Chart.getLastValue() < 0.3d) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                FilterStationEmergencyPredictionFilterDestructionAfterOuterValve
                        .main(args);
                situation14Chart.setEmergencyGrowth(false);
            });
            th.start();
        });
        c.gridx = 0;
        c.gridy = 15;
        JButton button15 = new JButton();
        button15.setText("Симуляция отказа расходомера");

        button15.setEnabled(false);
        addButtonToPanel(panel, c, button15);
        c.gridx = 1;
        c.gridy = 15;
        DynamicDataChart situation15Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button15, e -> {
            situation15Chart.setEmergencyGrowth(true);
            FilterStationEmergencyPredictionFoulBlockage.main(args);
        });
        c.gridx = 0;
        c.gridy = 16;
        JButton button16 = new JButton();
        button16.setText("Симуляция нарушения дозирования");

        button16.setEnabled(false);
        addButtonToPanel(panel, c, button16);
        c.gridx = 1;
        c.gridy = 16;
        DynamicDataChart situation16Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button16, e -> {
            situation16Chart.setEmergencyGrowth(true);
            FilterStationEmergencyPredictionFoulBlockage.main(args);
        });
        c.gridx = 0;
        c.gridy = 17;
        JButton button17 = new JButton();
        button17.setText("Симуляция нарушения движения ленты");

        button17.setEnabled(false);
        addButtonToPanel(panel, c, button17);
        c.gridx = 1;
        c.gridy = 17;
        DynamicDataChart situation17Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button17, e -> {
            situation17Chart.setEmergencyGrowth(true);
            FilterStationEmergencyPredictionFoulBlockage.main(args);
        });
        c.gridx = 0;
        c.gridy = 18;

        JButton button18 = new JButton();
        button18.setText("Симуляция отказа детекторов емкости");

        button18.setEnabled(false);
        addButtonToPanel(panel, c, button18);

        c.gridx = 1;
        c.gridy = 18;
        DynamicDataChart situation18Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button18, e -> {
            situation18Chart.setEmergencyGrowth(true);
            FilterStationEmergencyPredictionFoulBlockage.main(args);
        });
        c.gridx = 0;
        c.gridy = 19;
        JButton button5 = new JButton();
        button5.setText("Симуляция неисправности нагревателя");

        addButtonToPanel(panel, c, button5);
        button5.setEnabled(false);
        c.gridx = 1;
        c.gridy = 19;
        DynamicDataChart situation19Chart = getDynamicDataChart(panel, c);
        waitForEmergency(button5, e -> {
            situation19Chart.setEmergencyGrowth(true);
            FilterStationEmergencyPredictionFoulBlockage.main(args);
        });
        Thread th = new Thread(() -> {
            while (true) {
                charts.forEach(chart -> {
                    Random random = new Random();

                    chart.setLastValue(Math.abs(chart.getLastValue() + random.nextInt(20) / 100d - random
                            .nextInt(10)
                            / 100d));
                    if(!chart.isEmergencyGrowth() && chart.getLastValue() >=0.1) chart.setLastValue(0.05d + random.nextInt(4) / 100d);
                    else if(chart.isEmergencyGrowth() && chart.getLastValue() >=0.2)chart.setLastValue(chart.getLastValue() );
                    chart.getSeries().add(new Millisecond(), chart.getLastValue());
                    if (chart.getLastValue() > 0.15 && chart.getLastValue() <= 0.20) {
                        XYPlot plot = (XYPlot) chart.getChartPanel().getChart().getPlot();
                        XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
                        renderer0.setBaseShapesVisible(false);
                        plot.setRenderer(0, renderer0);
                        plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.orange);
                        plot.getRenderer().setSeriesPaint(0, Color.orange);
                        plot.getRenderer().setSeriesStroke(0, new BasicStroke(4.0f));
                    }
                    if (chart.getLastValue() >= 0.0 && chart.getLastValue() <= 0.15) {
                        XYPlot plot = (XYPlot) chart.getChartPanel().getChart().getPlot();
                        XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
                        renderer0.setBaseShapesVisible(false);
                        plot.setRenderer(0, renderer0);
                        plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.green);
                        plot.getRenderer().setSeriesPaint(0, Color.green);
                        plot.getRenderer().setSeriesStroke(0, new BasicStroke(4.0f));
                    }
                    if (chart.getLastValue() > 0.20 && chart.getLastValue() < 0.3d) {
                        XYPlot plot = (XYPlot) chart.getChartPanel().getChart().getPlot();
                        XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
                        renderer0.setBaseShapesVisible(false);
                        plot.setRenderer(0, renderer0);
                        plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red);
                        plot.getRenderer().setSeriesPaint(0, Color.red);
                        plot.getRenderer().setSeriesStroke(0, new BasicStroke(5.0f));
                    }
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
        frame.setSize(new Dimension(430, 900));
        frame.setLocation(1030, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    private static void waitForEmergency(JButton button, ActionListener actionListener) {
        button.addActionListener(actionListener);
    }

    private static void addButtonToPanel(final JPanel panel, final GridBagConstraints c,
                                         final JButton button2) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints
                .CENTER;
        panel.add(button2, c);
    }

    private static DynamicDataChart getDynamicDataChart(final JPanel panel,
                                                        final GridBagConstraints c) {
        c.fill = GridBagConstraints.NONE;
        DynamicDataChart dynamicDataChart = new DynamicDataChart(
                "");
        dynamicDataChart.getChartPanel().setPreferredSize(new Dimension(110, 50));
        panel.add(dynamicDataChart.getChartPanel(), c);
        charts.add(dynamicDataChart);
        XYPlot plot = (XYPlot) dynamicDataChart.getChartPanel().getChart().getPlot();
        XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
        renderer0.setBaseShapesVisible(false);
        plot.setRenderer(0, renderer0);
        plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.green);
        plot.getRenderer().setSeriesPaint(0, Color.green);
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(3.0f));
        return dynamicDataChart;
    }
}