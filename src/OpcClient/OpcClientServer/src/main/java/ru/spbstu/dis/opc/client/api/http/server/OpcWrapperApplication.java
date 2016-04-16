package ru.spbstu.dis.opc.client.api.http.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spbstu.dis.opc.client.api.http.server.guice.OpcWrapperGuiceModule;

public class OpcWrapperApplication
    extends Application<OpcWrapperConfiguration> {
  private static final Logger LOGGER = LoggerFactory.getLogger(OpcWrapperApplication.class);

  private GuiceBundle<OpcWrapperConfiguration> guiceBundle;

  @Override
  public void initialize(Bootstrap<OpcWrapperConfiguration> bootstrap) {
    LOGGER.warn("Guice bundle trying to inject module. "
        + "If nothing happens after this message - see GuiceBUndle.initInjector()");
    guiceBundle = GuiceBundle.<OpcWrapperConfiguration>newBuilder()
        .addModule(new OpcWrapperGuiceModule())
        .build(Stage.PRODUCTION);
    bootstrap.addBundle(guiceBundle);
  }

  @Override
  public void run(OpcWrapperConfiguration configuration, Environment environment) {
    environment.jersey().packages("com.siemens.ct.agent.rest");
    final ObjectMapper mapper = environment.getObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.findAndRegisterModules();
  }

  public static void main(String[] args) {
    try {
      LOGGER.warn("String agent application");
      new OpcWrapperApplication().run(args);
    } catch (Exception e) {
      LOGGER.error("Unexpected error occurred during agent starting procedure", e);
    }
  }
}
