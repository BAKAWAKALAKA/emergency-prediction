package ru.spbstu.dis.ep.data;

import com.google.common.collect.Maps;
import java.util.HashMap;

public enum Tag {
  REACTOR_temperature_reached,
  REACTOR_tank_B301_water_top_level_sensor,
  REACTOR_tank_B301_water_bottom_level_sensor,
  REACTOR_ControlPanel_Mixing_on,
  REACTOR_Temperature1,
  REACTOR_Temperature2,
  REACTOR_Temperature3,
  REACTOR_Active_Temperature_Set,
  REACTOR_Current_Process_Temperature,
  REACTOR_Controlled_TEMPERATURE,
  REACTOR_Controlled_Output_From_Pump_P201,
  REACTOR_ControlPanel_mixing_pump_P201_on,
  MIX_tank_B204_top,
  MIX_tank_B204_water_bottom_level_sensor,
  MIX_ControlPanel_DownstreamStation_pump_P202_on,
  MIX_ControlPanel_PumpToMainTank_P201_on,
  MIX_valve_V201_ToMainTank_on,
  MIX_valve_V202_ToMainTank_on,
  MIX_valve_V203_ToMainTank_on,
  MIX_ControlPanel_FLOW_SPEED,
  MIX_PumpToMainTank_P201_on,
  FILT_foul_water_pump_P101_on,
  FILT_downstream_station_pump_P102_on,
  FILT_open_knife_gate_V101_and_close_butte_V103,
  MIX_tank_B201_water_top_level_sensor,
  MIX_tank_B201_water_bottom_level_sensor, REACTOR_DOWNSTREAM_ON;

  public static HashMap<Tag, String> TAG_TO_ID_MAPPING = Maps.newHashMap();

  static {
    // real OPC server tags
    TAG_TO_ID_MAPPING.put(REACTOR_Temperature1, "ReactorConnection/M/Temp1");//not working
    TAG_TO_ID_MAPPING.put(REACTOR_Temperature2, "ReactorConnection/M/Temp2");//not working
    TAG_TO_ID_MAPPING.put(REACTOR_Temperature3, "ReactorConnection/M/Temp3");//not working
    TAG_TO_ID_MAPPING.put(REACTOR_Active_Temperature_Set, "ReactorConnection/M/TempProc");
    //Active Temperature - не работает
    TAG_TO_ID_MAPPING
        .put(REACTOR_Controlled_Output_From_Pump_P201, "ReactorConnection/M/3CO1_Real");//controlled
    TAG_TO_ID_MAPPING.put(REACTOR_Controlled_TEMPERATURE, "ReactorConnection/M/SP_Man")
    ;//set temperature in reactor
    TAG_TO_ID_MAPPING.put(REACTOR_Current_Process_Temperature, "ReactorConnection/M/3PV1_TP");
    //Текущая температура процесса - сенсор
    TAG_TO_ID_MAPPING.put(REACTOR_ControlPanel_Mixing_on, "ReactorConnection/M/TP_3M4");
    TAG_TO_ID_MAPPING.put(REACTOR_DOWNSTREAM_ON, "ReactorConnection/M/TP_3M2");
    TAG_TO_ID_MAPPING.put(REACTOR_ControlPanel_mixing_pump_P201_on, "ReactorConnection/M/TP_3M1");
    TAG_TO_ID_MAPPING.put(REACTOR_temperature_reached, "ReactorConnection/E/3B1");
    TAG_TO_ID_MAPPING.put(REACTOR_tank_B301_water_top_level_sensor, "ReactorConnection/E/3B2");
    TAG_TO_ID_MAPPING.put(REACTOR_tank_B301_water_bottom_level_sensor, "ReactorConnection/E/3B3");
    TAG_TO_ID_MAPPING.put(MIX_tank_B204_top, "MixingConnection/E/2B6");
    TAG_TO_ID_MAPPING.put(MIX_tank_B204_water_bottom_level_sensor, "MixingConnection/E/2B7");
    TAG_TO_ID_MAPPING
        .put(MIX_ControlPanel_DownstreamStation_pump_P202_on, "MixingConnection/M/TP_2M2");
    TAG_TO_ID_MAPPING.put(MIX_ControlPanel_PumpToMainTank_P201_on, "MixingConnection/M/TP_2M1");
    TAG_TO_ID_MAPPING.put(MIX_valve_V201_ToMainTank_on, "MixingConnection/M/TP_2M3");
    TAG_TO_ID_MAPPING.put(MIX_valve_V202_ToMainTank_on, "MixingConnection/M/TP_2M4");
    TAG_TO_ID_MAPPING.put(MIX_valve_V203_ToMainTank_on, "MixingConnection/M/TP_2M5");
    TAG_TO_ID_MAPPING.put(MIX_ControlPanel_FLOW_SPEED, "MixingConnection/M/2PV1_TP");
    TAG_TO_ID_MAPPING.put(MIX_PumpToMainTank_P201_on, "MixingConnection/A/2M1");
    TAG_TO_ID_MAPPING.put(MIX_tank_B201_water_top_level_sensor, "MixingConnection/A/2B2");
    TAG_TO_ID_MAPPING.put(MIX_tank_B201_water_bottom_level_sensor, "MixingConnection/A/2B3");
    TAG_TO_ID_MAPPING.put(FILT_foul_water_pump_P101_on, "FilterConnection/A/1M2");
    TAG_TO_ID_MAPPING.put(FILT_downstream_station_pump_P102_on, "FilterConnection/A/1M3");
    TAG_TO_ID_MAPPING
        .put(FILT_open_knife_gate_V101_and_close_butte_V103, "FilterConnection/A/1M4_5");
  }
}
