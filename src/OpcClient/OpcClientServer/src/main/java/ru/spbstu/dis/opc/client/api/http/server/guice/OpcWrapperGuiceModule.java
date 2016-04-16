package ru.spbstu.dis.opc.client.api.http.server.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import ru.spbstu.dis.opc.client.api.http.server.services.FakeOpcService;
import ru.spbstu.dis.opc.client.api.http.server.services.OpcService;
import ru.spbstu.dis.opc.client.api.http.server.services.RealOpcService;

public class OpcWrapperGuiceModule implements Module {

  public void configure(final Binder binder) {
    binder.bind(Config.class).toProvider(ConfigProvider.class).in(Singleton.class);
  }

  @Singleton
  @Provides
  OpcService bindOPcService(Config config) {
    if (config.getBoolean("opc.service.real")) {
      return RealOpcService.createForConfig(config);
    } else {
      return new FakeOpcService();
    }
  }
}
