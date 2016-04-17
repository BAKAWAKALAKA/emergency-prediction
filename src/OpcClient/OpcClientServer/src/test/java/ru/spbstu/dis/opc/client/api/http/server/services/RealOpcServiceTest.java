package ru.spbstu.dis.opc.client.api.http.server.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.typesafe.config.Config;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ru.spbstu.dis.opc.client.api.http.server.guice.OpcWrapperGuiceModule;
import java.util.Random;
import java.util.Set;

@Ignore
public class RealOpcServiceTest {

  private static RealOpcService opcService;

  @Test
  public void availableTagsWorking()
  throws Exception {
    final Set<String> tags = opcService.tags();
    assertThat(tags).isNotEmpty();
    final String tagNameToCheck = "MixingConnection/M/TP_2M5";
    assertThat(tags).contains(tagNameToCheck);
  }

  @Test
  public void readWriteValuesWorking()
  throws Exception {
    final String tag = "MixingConnection/M/SP_Man";
    final Float currentValue = opcService.readFloat(tag);
    assertThat(currentValue).isNotNull();
    final float newValue = new Random().nextFloat();
    opcService.writeValueForTag(tag, newValue);
    final Float updatedValue = opcService.readFloat(tag);
    assertThat(updatedValue).isEqualTo(newValue);
    opcService.writeValueForTag(tag, currentValue);
  }

  @Test
  public void pumpsAreWorking()
  throws Exception {
    final String tag = "MixingConnection/M/TP_2M5";
    final boolean currentTagValue = opcService.readBoolean(tag);
    opcService.writeValueForTag(tag, !currentTagValue);
    final Boolean updatedPumpValue = opcService.readBoolean(tag);
    assertThat(updatedPumpValue).isEqualTo(!currentTagValue);
    opcService.writeValueForTag(tag, currentTagValue);
  }

  @BeforeClass
  public static void setUp()
  throws Exception {
    final Injector injector = Guice.createInjector(new OpcWrapperGuiceModule());
    opcService = RealOpcService.createForConfig(injector.getInstance(Config.class));
  }
}