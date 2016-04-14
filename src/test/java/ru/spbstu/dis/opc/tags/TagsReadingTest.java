package ru.spbstu.dis.opc.tags;

import org.junit.Ignore;
import org.junit.Test;
import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ep.data.opc.OPCDataReader;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
@Ignore
public class TagsReadingTest {
  @Test
  public void shouldOPCReaderReadWaterPumpsState() throws InterruptedException {
		// given
		final OPCDataReader opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();

		// when
		final Map<Tag, Double> actualValues = opcDataReader.getActualValues();

		// then
		assertThat(actualValues.get(Tag.MIX_valve_V201_ToMainTank_on)).isNotNull();
		assertThat(actualValues.get(Tag.MIX_valve_V202_ToMainTank_on)).isNotNull();
		assertThat(actualValues.get(Tag.MIX_valve_V203_ToMainTank_on)).isNotNull();
  }

	@Test
	public void shouldOPCReaderReadWaterFlowState() throws InterruptedException {
		// given
		final OPCDataReader opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();

		// when
		final Map<Tag, Double> actualValues = opcDataReader.getActualValues();

		// then
		assertThat(actualValues.get(Tag.MIX_ControlPanel_FLOW_SPEED)).isNotNull();
		assertThat(actualValues.get(Tag.MIX_tank_B201_water_top_level_sensor)).isNotNull();
		assertThat(actualValues.get(Tag.MIX_PumpToMainTank_P201_on)).isNotNull();
	}
}