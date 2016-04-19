package ru.spbstu.dis.opc.client.api.http.server.data.random;

import ru.spbstu.dis.opc.client.api.http.server.data.DataInput;
import ru.spbstu.dis.opc.client.api.opc.access.Tag;
import ru.spbstu.dis.opc.client.api.http.server.data.DataProvider;

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
