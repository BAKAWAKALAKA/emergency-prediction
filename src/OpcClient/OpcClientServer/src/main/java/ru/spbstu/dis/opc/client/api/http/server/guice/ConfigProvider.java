package ru.spbstu.dis.opc.client.api.http.server.guice;

import com.google.inject.Provider;
import com.typesafe.config.Config;
import static com.typesafe.config.ConfigFactory.parseResources;
import com.typesafe.config.ConfigParseOptions;
import static java.lang.Thread.currentThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigProvider implements Provider<Config> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProvider.class);

  private static final String APPLICATION_CONF = "application.conf";

  public Config get() {
    final ConfigParseOptions options = ConfigParseOptions.defaults().setAllowMissing(false);
    final ClassLoader classLoader = currentThread().getContextClassLoader();
    final Config c = parseResources(classLoader, APPLICATION_CONF, options);
    return c.resolve();
  }
}