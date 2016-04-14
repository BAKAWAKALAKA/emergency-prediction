package ru.spbstu.dis.ep.kb;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Ignore;
import org.junit.Test;
import ru.spbstu.dis.ep.data.DataInput;
import ru.spbstu.dis.ep.data.Tag;
@Ignore
public class KnowledgeBaseTest {
  @Test
  public void shouldInferInSimpleKnowledgeBase_case1()
  throws Exception {
    // given
    final SimpleKnowledgeBase simpleKnowledgeBase = new SimpleKnowledgeBase();

    // when
    final ChosenAction chosenAction = simpleKnowledgeBase
        .inferOutput(DataInput.withPressureAndLowerPressure(5, 1));

    // then
    assertThat(chosenAction).isEqualTo(new ChosenAction("Action2"));
  }

  @Test
  public void shouldInferInSimpleKnowledgeBase_case2()
  throws Exception {
    // given
    final SimpleKnowledgeBase simpleKnowledgeBase = new SimpleKnowledgeBase();

    // when
    final ChosenAction chosenAction = simpleKnowledgeBase
        .inferOutput(DataInput.withPressureAndLowerPressure(1235, 1));

    // then
    assertThat(chosenAction).isEqualTo(new ChosenAction("Action1"));
  }
}

class SimpleKnowledgeBase implements KnowledgeBase {
  @Override
  public ChosenAction inferOutput(final DataInput input) {
    if (input.getDataForTag(Tag.MIX_tank_B204_water_bottom_level_sensor) > 10) {
      return new ChosenAction("Action1");
    } else {
      return new ChosenAction("Action2");
    }
  }
}