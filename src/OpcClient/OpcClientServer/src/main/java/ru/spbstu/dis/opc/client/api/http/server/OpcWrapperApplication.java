package ru.spbstu.dis.opc.client.api.http.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.typesafe.config.Config;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import static org.eclipse.jetty.servlets.CrossOriginFilter.*;
import org.glassfish.jersey.server.validation.ValidationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spbstu.dis.opc.client.api.http.server.guice.OpcWrapperGuiceModule;
import ru.spbstu.dis.opc.client.api.http.server.rest.OpcAccessResource;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class OpcWrapperApplication
    extends Application<OpcWrapperConfiguration> {
  private static final Logger LOGGER = LoggerFactory.getLogger(OpcWrapperApplication.class);

  private static final String RESOURCES_PACKAGE = OpcAccessResource.class.getPackage().getName();

  public OpcWrapperApplication() {
  }

  @Override
  public String getName() {
    return "OpcWrapperApplication";
  }

  @Override
  public void initialize(final Bootstrap<OpcWrapperConfiguration> bootstrap) {
    final GuiceBundle<OpcWrapperConfiguration> build = GuiceBundle
        .<OpcWrapperConfiguration>newBuilder()
        .addModule(new OpcWrapperGuiceModule())
        .setConfigClass(OpcWrapperConfiguration.class)
        .build();
    bootstrap.addBundle(build);
    final Config config = build.getInjector().getInstance(Config.class);
    initSwaggerWithConfig(config);
  }

  private void initSwaggerWithConfig(Config config) {
    final Config swagger = config.getConfig("swagger");
    final String host = swagger.getString("host");
    final int port = swagger.getInt("port");
    final String schema = swagger.getString("schema");
    final String rootPath = swagger.getString("rootPath");
    BeanConfig beanConfig = new BeanConfig();
    beanConfig.setVersion("1.0.0");
    beanConfig.setSchemes(new String[]{schema});
    beanConfig.setHost(String.format("%s:%s", host, port));
    beanConfig.setBasePath(rootPath);
    beanConfig.setResourcePackage(RESOURCES_PACKAGE);
    beanConfig.setScan(true);
  }

  @Override
  public void run(final OpcWrapperConfiguration configuration, final Environment environment) {
    final ObjectMapper mapper = environment.getObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.findAndRegisterModules();
    environment.jersey().packages(RESOURCES_PACKAGE);
    registerAndEnableFeatures(environment);
    configCors(environment);
  }

  private void registerAndEnableFeatures(final Environment environment) {
    environment.jersey().register(ApiListingResource.class);
    environment.jersey().register(ValidationFeature.class);
    environment.jersey().register(SwaggerSerializers.class);
  }

  private void configCors(final Environment environment) {
    FilterRegistration.Dynamic cors = environment.servlets()
        .addFilter("CORS", CrossOriginFilter.class);
    // Configure CORS parameters
    cors.setInitParameter(ALLOWED_ORIGINS_PARAM, "*");
    cors.setInitParameter(ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
    cors.setInitParameter(ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
  }

  public static void main(String[] args) {
    try {
      new OpcWrapperApplication().run(args);
    } catch (Exception e) {
      LOGGER.error("Unexpected error occurred", e);
    }
  }
}
