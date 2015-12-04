package ru.spbstu.dis;

import com.google.common.collect.Maps;
import java.util.HashMap;

public enum Tag {
  PRESSURE,
  LOWER_PRESSURE;

  public static HashMap<Tag, String> TAG_TO_ID_MAPPING = Maps.newHashMap();
  static {
    TAG_TO_ID_MAPPING.put(Tag.PRESSURE, "maths.sin");
    TAG_TO_ID_MAPPING.put(Tag.LOWER_PRESSURE, "maths.cos");
  }
}
