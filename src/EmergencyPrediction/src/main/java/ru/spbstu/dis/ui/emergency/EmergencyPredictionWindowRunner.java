package ru.spbstu.dis.ui.emergency;

import javax.swing.*;
import java.awt.*;

public class EmergencyPredictionWindowRunner {
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

    JPanel panel = new JPanel(new GridLayout(20,1));

    JLabel label = new JLabel("Типы нештатных ситуаций:");
    label.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(label);
    JButton button = new JButton();
    button.setText("Симуляция блокировки выпускного клапана");
    button.addActionListener(e -> FilterStationEmergencyPredictionFilterDestructionAfterOuterValve
        .main(args));
    panel.add(button);

    JButton button1 = new JButton();
    button1.setText("Симуляция эксплуатационного засора");
    button1.addActionListener(e -> FilterStationEmergencyPredictionOldFilterBlockage.main(args));
    panel.add(button1);

    JButton button2 = new JButton();
    button2.setText("Симуляция механического засора");
    button2.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button2);

    JButton button3 = new JButton();
    button3.setText("Симуляция блокировки входного клапана");
    button3.addActionListener(e -> FilterStationEmergencyPredictionFilterDestructionAfterInnerValve
        .main(args));
    panel.add(button3);

    JButton button4 = new JButton();
    button4.setText("Симуляция сбоя направления течения");
    button4.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button4);
    button4.setEnabled(false);
    JButton button5 = new JButton();
    button5.setText("Симуляция неисправности нагревателя");
    button5.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button5);
    button5.setEnabled(false);
    JButton button6 = new JButton();
    button6.setText("Симуляция неисправности основного насоса");
    button6.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button6);
    button6.setEnabled(false);
    JButton button7 = new JButton();
    button7.setText("Симуляция неисправности датчика давления");
    button7.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button7);
    button7.setEnabled(false);
    JButton button8 = new JButton();
    button8.setText("Симуляция сбоя системы охлаждения");
    button8.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button8);
    button8.setEnabled(false);
    JButton button9 = new JButton();
    button9.setText("Симуляция воздушной пробки");
    button9.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));

    button9.setEnabled(false);
    panel.add(button9);

    JButton button10 = new JButton();
    button10.setText("Симуляция неисправности задвижек");
    button10.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));

    button10.setEnabled(false);
    panel.add(button10);

    JButton button11 = new JButton();
    button11.setText("Симуляция отказа клапанов (reactor)");
    button11.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button11.setEnabled(false);
    panel.add(button11);

    JButton button12 = new JButton();
    button12.setText("Симуляция переполнения бака (mixing)");
    button12.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button12.setEnabled(false);
    panel.add(button12);

    JButton button13 = new JButton();
    button13.setText("Симуляция переполнения бака (reactor)");
    button13.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button13.setEnabled(false);
    panel.add(button13);

    JButton buttonX = new JButton();
    buttonX.setText("Симуляция блокировки выпускного клапана");
    buttonX.addActionListener(e -> FilterStationEmergencyPredictionFilterDestructionAfterOuterValve
        .main(args));
    panel.add(buttonX);

    JButton button14 = new JButton();
    button14.setText("Симуляция отказа расходомера");
    button14.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button14.setEnabled(false);
    panel.add(button14);

    JButton button15 = new JButton();
    button15.setText("Симуляция нарушения дозирования");
    button15.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button15.setEnabled(false);
    panel.add(button15);

    JButton button16 = new JButton();
    button16.setText("Симуляция нарушения движения ленты");
    button16.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button16.setEnabled(false);
    panel.add(button16);

    JButton button17 = new JButton();
    button17.setText("Симуляция отказа детекторов емкости");
    button17.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    button17.setEnabled(false);
    panel.add(button17);

    JScrollPane panelPane = new JScrollPane(panel);
    frame.add(panelPane);
    frame.setSize(new Dimension(430,900));
    frame.setLocation(1030,0);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

  }
}