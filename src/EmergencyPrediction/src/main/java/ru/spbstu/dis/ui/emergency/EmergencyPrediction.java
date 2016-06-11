package ru.spbstu.dis.ui.emergency;

import com.google.common.net.HostAndPort;
import com.typesafe.config.Config;
import org.slf4j.LoggerFactory;
import ru.spbstu.dis.ConfigProvider;
import ru.spbstu.dis.opc.client.api.OpcClientApiFactory;
import ru.spbstu.dis.opc.client.api.opc.access.OpcAccessApi;
import ru.spbstu.dis.opc.client.api.opc.access.Tag;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Александр on 05.06.2016.
 */
public class EmergencyPrediction {
  final OpcAccessApi opcAccessApi;

  private static final org.slf4j.Logger LOGGER = LoggerFactory
      .getLogger(EmergencyPrediction.class);

  ArrayList notifications = new ArrayList();

  static String filter_TP_1M7 = Tag.TAG_TO_ID_MAPPING.get(Tag
      .FILT_ControlPanel_WARNING);

  static String filter_open_rev_valve = Tag.TAG_TO_ID_MAPPING.get(Tag.FILT_open_rev_valve);//1M45

  static String filter_open_rev_pump = Tag.TAG_TO_ID_MAPPING.get(Tag
      .FILT_ControlPanel_downstream_station_pump_P102_on);//1M3

  static String filter_p101 = Tag.TAG_TO_ID_MAPPING.get(Tag
      .FILT_pump_101_on);

  static String filter_p102 = Tag.TAG_TO_ID_MAPPING.get(Tag
      .FILT_pump_102_on);
  static String MIX_2M3 = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_valve_V201_ToMainTank_on);
  static String MIX_2M1 = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_ControlPanel_PumpToMainTank_P201_on);
  static String MIX_set_point_man = Tag.TAG_TO_ID_MAPPING.get(Tag.MIX_ControlPanel_FLOW_SPEED);
  static String MIX_2M2 = Tag.TAG_TO_ID_MAPPING.get(Tag
                                                        .MIX_ControlPanel_DownstreamStation_pump_P202_on);
  static String REAC_3M3 = Tag.TAG_TO_ID_MAPPING.get(Tag
                                                        .REACTOR_ControlPanel_downstream_station_pump_P302_on);
  static String FILT_1M6 = Tag.TAG_TO_ID_MAPPING.get(Tag
                                                         .FILT_ControlPanel_downstream_valve_V103_on);
  static String MIX_2B2_sensor = Tag.TAG_TO_ID_MAPPING.get(Tag
                                                               .MIX_tank_B201_water_top_level_sensor);
  static String REAC_3M1 = Tag.TAG_TO_ID_MAPPING.get(Tag
                                                               .REACTOR_ControlPanel_mixing_pump_P201_on);
  static String REAC_3M4 = Tag.TAG_TO_ID_MAPPING.get(Tag
                                                         .REACTOR_ControlPanel_Mixing_on);

  double filter_fake_risk_value = 0d;

  boolean filter_fake_active_flag = true;

  JList actionsFinishedList;

  JLabel picLabel;

  JLabel progressText = new JLabel();

  DefaultListModel listModel = new DefaultListModel();

  DynamicDataChart closenessChartFrame = new DynamicDataChart(
      "ИСУ ТП УПРАВЛЕНИЯ ХИМ. СТАНЦИЕЙ");

  public EmergencyPrediction(final OpcAccessApi opcAccessApi) {
    this.opcAccessApi = opcAccessApi;
  }

  static OpcAccessApi createOpcApi() {
    final Config config = new ConfigProvider().get().resolve();
    final Config opcAccessApiConf = config.getConfig("http.opc.client");
    final String host = opcAccessApiConf.getString("host");
    final int port = opcAccessApiConf.getInt("port");
    final HostAndPort hostAndPort = HostAndPort.fromParts(host, port);
    LOGGER.warn("Opc access api uses {} to connect", hostAndPort);
    return OpcClientApiFactory.createOpcAccessApi(hostAndPort);
  }

  static void setLookAndFeelType() {
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
  }

  void initMeterChart() {
    final MeterChart demo = new MeterChart("");

    Thread th = new Thread(() -> {
      while (true) {
        double max = filter_fake_risk_value;
        demo.setValue(max);

        demo.getDataset().setValue(max);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    th.start();
    final JPanel titlePanel = new JPanel();
    titlePanel.setLayout(new FlowLayout());
    titlePanel.add(demo.getChartPanel());
    closenessChartFrame.add(titlePanel);
  }

  void runSimulation() {

    Thread th = new Thread(() -> {

      try {
        getDataFromOPC();
      } catch (InterruptedException e) {
        LOGGER.error("Error during data retrieving", e);
      }
    });
    th.start();
  }

  void notifier(String term, double dangerLevel) {

    if (dangerLevel > 0.5d) {
      ImageIcon icon = new ImageIcon(
          FilterStationEmergencyPredictionFoulBlockage.class.getResource("/alarm.png"));
      showErrorNotif(term, new Color(249, 78, 30), icon);
    }
    if (dangerLevel > 0.0d && dangerLevel <= 0.3d) {
      ImageIcon icon = new ImageIcon(
          FilterStationEmergencyPredictionFoulBlockage.class.getResource("/info.png"));
      showErrorNotif(term, new Color(127, 176, 72), icon);
    }
    if (dangerLevel > 0.3d && dangerLevel <= 0.5d) {
      ImageIcon icon = new ImageIcon(
          FilterStationEmergencyPredictionFoulBlockage.class.getResource("/warning.png"));
      showErrorNotif(term, new Color(249, 236, 100), icon);
    }
  }

  void showErrorNotif(String term, Color color, ImageIcon icon) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ex) {
      Logger.getLogger(PopupTester.class.getName()).log(Level.SEVERE, null, ex);
    }
    notifications.add(term);
    closenessChartFrame.getList().setListData(notifications.toArray());
    closenessChartFrame.getList().updateUI();
    closenessChartFrame.repaint();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException ex) {
      Logger.getLogger(PopupTester.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  void getDataFromOPC()
  throws InterruptedException {

  }
}
