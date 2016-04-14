package ru.spbstu.dis.opc.tags;

import org.junit.Ignore;
import org.junit.Test;
import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ep.data.opc.OPCDataReader;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

public class ConnectionTest {
  @Ignore
  @Test
  public void shouldOPCReaderReturnAnyOutput() throws InterruptedException {
		// given
		final OPCDataReader opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();

		// when
		final Map<Tag, Double> actualValues = opcDataReader.getActualValues();
		System.out.println(actualValues);

		// then
    assertThat(!actualValues.isEmpty());
  }
}