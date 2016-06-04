package ru.spbstu.dis.ui.emergency;

import javax.swing.*;

public class EmergencyPredictionWindowRunner {
  public static void main(String[] args) {
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      // If Nimbus is not available, you can set the GUI to another look and feel.
    }

    JFrame frame = new JFrame("Типы НС");

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JLabel label = new JLabel("Типы нештатных ситуаций:");
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
    button4.setText("Симуляция неисправности фильтра");
    button4.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button4);

    JButton button5 = new JButton();
    button5.setText("Симуляция неисправности фильтра");
    button5.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));
    panel.add(button5);



    frame.add(panel);
    frame.setSize(330, 600);
    frame.setLocation(1115, 0);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

  }
}