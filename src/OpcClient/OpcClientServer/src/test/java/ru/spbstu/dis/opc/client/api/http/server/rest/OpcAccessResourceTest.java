package ru.spbstu.dis.opc.client.api.http.server.rest;

import com.google.common.io.Resources;
import com.google.common.net.HostAndPort;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import static java.util.Collections.singleton;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.assertj.core.util.Preconditions;
import org.junit.Ignore;
import org.junit.Test;
import ru.spbstu.dis.opc.client.api.OpcClientApiFactory;
import ru.spbstu.dis.opc.client.api.http.server.OpcWrapperApplication;
import ru.spbstu.dis.opc.client.api.http.server.guice.OpcWrapperGuiceModule;
import ru.spbstu.dis.opc.client.api.http.server.services.OpcService;
import ru.spbstu.dis.opc.client.api.opc.access.OpcAccessApi;
import java.util.Set;

public class OpcAccessResourceTest {
  @Ignore
  @Test
  public void opcClientApiWorksAsExpected()
  throws Exception {
    //given
    final String ymlConfig = Resources.getResource("opc.yml").getFile();
    final Module testModule =
        Modules.override(new OpcWrapperGuiceModule())
            .with(new Module() {
              @Override
              public void configure(final Binder binder) {
                binder.bind(OpcService.class).toInstance(new OpcAccessFake());
              }
            });
    final OpcWrapperApplication opcWrapperApplication = new OpcWrapperApplication(testModule);
    opcWrapperApplication.run("server", ymlConfig);
    final HostAndPort target = HostAndPort.fromParts("127.0.0.1", 7998);
    final OpcAccessApi opcAccessApi = OpcClientApiFactory.createOpcAccessApi(target);
    //when
    opcAccessApi.availableTags();
    opcAccessApi.writeValueForTag("any", true);
    opcAccessApi.writeValueForTag("any", 5f);
    opcAccessApi.readBoolean("any");
    opcAccessApi.readFloat("any");
    //then
    assertThat(OpcAccessFake.readTagsWasCalled).isTrue();
    assertThat(OpcAccessFake.writeBooleanValueWasCalled).isTrue();
    assertThat(OpcAccessFake.writeFloatValueWasCalled).isTrue();
    assertThat(OpcAccessFake.readBooleanWasCalled).isTrue();
    assertThat(OpcAccessFake.readFloatWasCalled).isTrue();
    //tear down
    opcWrapperApplication.stop();
  }

  private static class OpcAccessFake implements OpcService {

    public static boolean writeFloatValueWasCalled = false;

    public static boolean writeBooleanValueWasCalled = false;

    public static boolean readBooleanWasCalled = false;

    public static boolean readFloatWasCalled = false;

    public static boolean readTagsWasCalled = false;

    @Override
    public Set<String> tags() {
      readTagsWasCalled = true;
      return singleton("tag1");
    }

    @Override
    public void writeValueForTag(final String tag, final Boolean value) {
      Preconditions.checkNotNull(tag);
      Preconditions.checkNotNull(value);
      writeBooleanValueWasCalled = true;
    }

    @Override
    public void writeValueForTag(final String tag, final Float value) {
      Preconditions.checkNotNull(tag);
      Preconditions.checkNotNull(value);
      writeFloatValueWasCalled = true;
    }

    @Override
    public Boolean readBoolean(final String tag) {
      Preconditions.checkNotNull(tag);
      readBooleanWasCalled = true;
      return true;
    }

    @Override
    public Float readFloat(final String tag) {
      Preconditions.checkNotNull(tag);
      readFloatWasCalled = true;
      return 5f;
    }
  }
}