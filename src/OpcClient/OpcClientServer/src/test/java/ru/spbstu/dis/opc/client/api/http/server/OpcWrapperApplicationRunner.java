package ru.spbstu.dis.opc.client.api.http.server;

import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spbstu.dis.opc.client.api.http.server.guice.OpcWrapperGuiceModule;

public class OpcWrapperApplicationRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(OpcWrapperApplicationRunner.class);

  public static void main(String[] args) {
    try {
      final String ymlConfig = Resources.getResource("opc.yml").getFile();
      new OpcWrapperApplication(new OpcWrapperGuiceModule()).run("server", ymlConfig);
    } catch (Exception e) {
      LOGGER.error("Error occurred", e);
      throw new IllegalStateException(e);
    }
  }
}