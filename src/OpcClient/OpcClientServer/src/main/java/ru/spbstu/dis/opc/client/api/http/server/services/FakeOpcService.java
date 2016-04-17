package ru.spbstu.dis.opc.client.api.http.server.services;

import static java.util.Collections.singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;
import java.util.Set;

public class FakeOpcService implements OpcService {
  private static final Logger LOGGER = LoggerFactory.getLogger(FakeOpcService.class);

  @Override
  public Set<String> tags() {
    return singleton("hardcodedTag");
  }

  @Override
  public void writeValueForTag(final String tag, final Boolean value) {
    LOGGER.info("Writing {} for tag {}", value, tag);
  }

  @Override
  public void writeValueForTag(final String tag, final Float value) {
    LOGGER.info("Writing {} for tag {}", value, tag);
  }

  @Override
  public Boolean readBoolean(final String tag) {
    final boolean res = new Random().nextBoolean();
    LOGGER.info("Returning {} for tag {}", res, tag);
    return res;
  }

  @Override
  public Float readFloat(final String tag) {
    final float res = new Random().nextFloat();
    LOGGER.info("Returning {} for tag {}", res, tag);
    return res;
  }
}
