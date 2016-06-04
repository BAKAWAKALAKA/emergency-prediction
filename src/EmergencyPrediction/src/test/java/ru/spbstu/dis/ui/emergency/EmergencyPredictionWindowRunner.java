package ru.spbstu.dis.ui.emergency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    JButton button = new JButton();
    button.setText("Симуляция засора фильтра");
    button.addActionListener(e -> EmergencyPredictionWindow.main(args));

    JButton button1 = new JButton();
    button1.setText("Симуляция старого фильтра");
    button1.addActionListener(e -> FilterStationEmergencyPredictionOldFilterBlockage.main(args));

    JButton button2 = new JButton();
    button2.setText("Симуляция неисправности фильтра");
    button2.addActionListener(e -> FilterStationEmergencyPredictionFoulBlockage.main(args));


    panel.add(label);
    panel.add(button);
    panel.add(button1);
    panel.add(button2);

    frame.add(panel);
    frame.setSize(250, 600);
    frame.setLocation(1600,0);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

  }
}