package ru.spbstu.dis.opc.tags;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import ru.spbstu.dis.ep.EmergencyPredictor;
import ru.spbstu.dis.ep.data.DataInput;
import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ep.data.opc.OPCDataReader;
import ru.spbstu.dis.ep.kb.ChosenAction;
import ru.spbstu.dis.ep.kb.KnowledgeBase;

public class ConnectionTest {
  @Test
  public void shouldOPCReaderReturnAnyOutput() throws InterruptedException {
		// given
		final OPCDataReader opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();

		// when
		final Map<Tag, Double> actualValues = opcDataReader.getActualValues();
		System.out.println(actualValues);

		// then
		assertThat(actualValues.isEmpty()).isFalse();
  }
}