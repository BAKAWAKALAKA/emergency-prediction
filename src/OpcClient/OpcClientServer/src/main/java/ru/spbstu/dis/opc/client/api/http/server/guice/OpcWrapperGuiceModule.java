package ru.spbstu.dis.opc.client.api.http.server.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.typesafe.config.Config;

public class OpcWrapperGuiceModule implements Module {

  public void configure(final Binder binder) {
    binder.bind(Config.class).toProvider(ConfigProvider.class).in(Singleton.class);
  }
}
