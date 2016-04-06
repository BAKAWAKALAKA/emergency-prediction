package ru.spbstu.dis.opc.tags;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ep.data.opc.OPCDataReader;

public class TagsReadingTest {
  @Test
  public void shouldOPCReaderReadWaterPumpsState() throws InterruptedException {
		// given
		final OPCDataReader opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();

		// when
		final Map<Tag, Double> actualValues = opcDataReader.getActualValues();

		// then
		assertThat(actualValues.get(Tag.MIX_PUMP_2M3)).isNotNull();
		assertThat(actualValues.get(Tag.MIX_PUMP_2M4)).isNotNull();
		assertThat(actualValues.get(Tag.MIX_PUMP_2M5)).isNotNull();
  }

	@Test
	public void shouldOPCReaderReadWaterFlowState() throws InterruptedException {
		// given
		final OPCDataReader opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();

		// when
		final Map<Tag, Double> actualValues = opcDataReader.getActualValues();

		// then
		assertThat(actualValues.get(Tag.MIX_FLOW_SPEED)).isNotNull();
		assertThat(actualValues.get(Tag.MIX_WATER_HIGH_LVL_SENSOR)).isNotNull();
		assertThat(actualValues.get(Tag.MIX_MAIN_PUMP)).isNotNull();
	}
}