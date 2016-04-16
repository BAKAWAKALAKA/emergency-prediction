package ru.spbstu.dis.opc.client.api.http.server.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import ru.spbstu.dis.opc.client.api.http.server.services.FakeOpcService;
import ru.spbstu.dis.opc.client.api.http.server.services.OpcService;

public class OpcWrapperGuiceModule implements Module {

  public void configure(final Binder binder) {
    binder.bind(Config.class).toProvider(ConfigProvider.class).in(Singleton.class);
    binder.bind(OpcService.class).to(FakeOpcService.class).in(Singleton.class);
  }
}
