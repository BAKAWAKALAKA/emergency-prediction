package ru.spbstu.dis.data.random;

import ru.spbstu.dis.data.DataInput;
import ru.spbstu.dis.data.Tag;
import ru.spbstu.dis.data.DataProvider;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class RandomDataProvider implements DataProvider{
  @Override
  public DataInput nextDataPortion() {
    Map<Tag, Double> data = Tag.TAG_TO_ID_MAPPING.entrySet().stream()
        .collect(toMap(
            (e) -> e.getKey(),
            (e) -> Math.random()));
    return new DataInput(data);
  }
}
