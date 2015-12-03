package ru.spbstu.dis.kb;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class NeuroFuzzyKnowledgeBaseTest {

  @Test
  public void shouldInferInSimpleKnowledgeBase_case1()
  throws Exception {
    // given
    final SimpleKnowledgeBase simpleKnowledgeBase = new SimpleKnowledgeBase();

    // when
    final KnowledgeBaseOutput knowledgeBaseOutput = simpleKnowledgeBase
        .inferOutput(new KnowledgeBaseInput(5, 1));

    // then
    assertThat(knowledgeBaseOutput).isEqualTo(new KnowledgeBaseOutput("Action2"));
  }

  @Test
  public void shouldInferInSimpleKnowledgeBase_case2()
  throws Exception {
    // given
    final SimpleKnowledgeBase simpleKnowledgeBase = new SimpleKnowledgeBase();

    // when
    final KnowledgeBaseOutput knowledgeBaseOutput = simpleKnowledgeBase
        .inferOutput(new KnowledgeBaseInput(1235, 1));

    // then
    assertThat(knowledgeBaseOutput).isEqualTo(new KnowledgeBaseOutput("Action1"));
  }
}

class SimpleKnowledgeBase implements KnowledgeBase {
  @Override
  public KnowledgeBaseOutput inferOutput(final KnowledgeBaseInput input) {
    if (input.getPressure() > 10) {
      return new KnowledgeBaseOutput("Action1");
    } else {
      return new KnowledgeBaseOutput("Action2");
    }
  }
}