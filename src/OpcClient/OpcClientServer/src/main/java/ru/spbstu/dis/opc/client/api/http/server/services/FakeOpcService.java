package ru.spbstu.dis.opc.client.api.http.server.services;

import static java.util.Collections.singleton;
import java.util.Random;
import java.util.Set;

public class FakeOpcService implements OpcService {
  @Override
  public Set<String> tags() {
    return singleton("hardcodedTag");
  }

  @Override
  public void writeValueForTag(final String tag, final Boolean value) {
  }

  @Override
  public void writeValueForTag(final String tag, final Float value) {
  }

  @Override
  public Boolean readBoolean(final String tag) {
    return new Random().nextBoolean();
  }

  @Override
  public Float readFloat(final String tag) {
    return new Random().nextFloat();
  }
}
