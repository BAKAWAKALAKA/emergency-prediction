package ru.spbstu.dis.opc.tags;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.junit.Test;
import org.openscada.opc.lib.da.Item;
import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ep.data.opc.OPCDataReader;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TagsWritingTest {
  @Test
  public void shouldOPCWriteToItems()
  throws InterruptedException {
    // given
    final OPCDataReader opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();

    // when
    final Map<Tag, Double> actualValues = opcDataReader.getActualValues();

    // add a thread for writing a value every 3 seconds
    ScheduledExecutorService writeThread = Executors.newSingleThreadScheduledExecutor();
    final AtomicInteger i = new AtomicInteger(0);
    writeThread.scheduleWithFixedDelay(() -> {
      final JIVariant value = new JIVariant(i.incrementAndGet());
      try {
        Item item = opcDataReader.getItemsToRead().get(Tag.MIX_tank_B204_top);

        System.out.println(">>> " + "writing value " + i.get());
        item.write(value);
      } catch (JIException e) {
        e.printStackTrace();
      }
    }, 5, 3, TimeUnit.SECONDS);

    // wait a little bit
    Thread.sleep(20 * 1000);
    writeThread.shutdownNow();
  }

  @Test
  public void shouldOPCWriteToItemsInfinite()
  throws InterruptedException {
    // given
    final OPCDataReader opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();

    // add a thread for writing a value every 3 seconds
    ScheduledExecutorService writeThread = Executors.newSingleThreadScheduledExecutor();
    final AtomicInteger i = new AtomicInteger(0);
    while (true) {
      final JIVariant value = new JIVariant(1);
      try {
        // when
        final Map<Tag, Double> actualValues = opcDataReader.getActualValues();
        System.out.println(actualValues);
        Item item = opcDataReader.getItemsToRead().get(Tag.MIX_tank_B204_top);

        System.out.println(">>> " + "writing value " + value.toString());
        if (item != null) {
          item.write(value);
        }
      } catch (JIException e) {
        e.printStackTrace();
      }
      // wait a little bit
      Thread.sleep(4000);
    }
  }
}