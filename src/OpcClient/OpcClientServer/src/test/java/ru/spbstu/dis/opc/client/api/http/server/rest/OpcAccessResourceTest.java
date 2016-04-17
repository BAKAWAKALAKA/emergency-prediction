package ru.spbstu.dis.opc.client.api.http.server.rest;

import com.google.common.net.HostAndPort;
import io.dropwizard.testing.junit.DropwizardClientRule;
import static java.util.Collections.singleton;
import org.assertj.core.util.Preconditions;
import org.junit.ClassRule;
import org.junit.Test;
import ru.spbstu.dis.opc.client.api.OpcClientApiFactory;
import ru.spbstu.dis.opc.client.api.http.server.services.OpcService;
import ru.spbstu.dis.opc.client.api.opc.access.AvailableTags;
import ru.spbstu.dis.opc.client.api.opc.access.OpcAccessApi;
import ru.spbstu.dis.opc.client.api.opc.access.ValueWritten;
import java.io.IOException;
import java.util.Set;

public class OpcAccessResourceTest {

  @ClassRule
  public static final DropwizardClientRule dropwizard =
      new DropwizardClientRule(new OpcAccessResource(new OpcAccessFake()));

  @Test
  public void shouldPing()
  throws IOException {
    final HostAndPort target =
        HostAndPort.fromParts(dropwizard.baseUri().getHost(), dropwizard.baseUri().getPort());
    final OpcAccessApi opcAccessApi = OpcClientApiFactory.createOpcAccessApi(target);
    final AvailableTags availableTags = opcAccessApi.availableTags();
    final ValueWritten any = opcAccessApi.writeValueForTag("any", true);
    int i = 0;
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