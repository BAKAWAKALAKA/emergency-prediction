package ru.spbstu.dis.kb;

import com.google.common.collect.Lists;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import ru.spbstu.dis.kb.fuzzy.inference.FuzzyInferenceEngine;
import ru.spbstu.dis.kb.neural.networks.NeuralNetwork;
import ru.spbstu.dis.kb.neural.networks.NeuralNetworkOutput;
import java.util.ArrayList;
import java.util.List;

public class NeuroFuzzyKnowledgeBaseTest {

  @Test
  public void shouldInferInSimpleKnowledgeBase_case1()
  throws Exception {
    // given
    final SimpleKnowledgeBase simpleKnowledgeBase = new SimpleKnowledgeBase();

    // when
    final ChosenAction chosenAction = simpleKnowledgeBase
        .inferOutput(new DataInput(5, 1));

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
        .inferOutput(new DataInput(1235, 1));

    // then
    assertThat(chosenAction).isEqualTo(new ChosenAction("Action1"));
  }

  @Test
  public void shouldInferForSimpleFuzzyNeuralImplementations_case1() {
    // given
    final ArrayList<NeuralNetwork> networks =
        Lists.newArrayList(new StupidNeuralNetwork(), new OtherStupidNeuralNetwork());
    final FuzzyInferenceEngine stupidFuzzyInferenceEngine =
        new StupidFuzzyInferenceEngine();
    final NeuroFuzzyKnowledgeBase neuroFuzzyKnowledgeBase = new NeuroFuzzyKnowledgeBase(networks,
        stupidFuzzyInferenceEngine);
    final DataInput dataInput = new DataInput(10, 5);

    // when
    final ChosenAction chosenAction = neuroFuzzyKnowledgeBase.inferOutput(dataInput);

    // then
    assertThat(chosenAction).isEqualTo(new ChosenAction("shutDown"));
  }
}

class SimpleKnowledgeBase implements KnowledgeBase {
  @Override
  public ChosenAction inferOutput(final DataInput input) {
    if (input.getPressure() > 10) {
      return new ChosenAction("Action1");
    } else {
      return new ChosenAction("Action2");
    }
  }
}

class StupidNeuralNetwork implements NeuralNetwork {
  @Override
  public NeuralNetworkOutput inferOutput(final DataInput input) {
    if (input.getPressure() > 5) {
      return new NeuralNetworkOutput("emergency", 1);
    } else {
      return new NeuralNetworkOutput("emergency", 0);
    }
  }
}

class OtherStupidNeuralNetwork implements NeuralNetwork {
  @Override
  public NeuralNetworkOutput inferOutput(final DataInput input) {
    if (input.getPressure() > 50) {
      return new NeuralNetworkOutput("emergency", 1);
    } else {
      return new NeuralNetworkOutput("emergency", 0);
    }
  }
}

class StupidFuzzyInferenceEngine implements FuzzyInferenceEngine {
  @Override
  public ChosenAction generateAction(final List<NeuralNetworkOutput> listOfNeuralNetworkOutputs) {
    boolean doShutDown = false;
    for (NeuralNetworkOutput neuralNetworkOutput : listOfNeuralNetworkOutputs) {
      switch (neuralNetworkOutput.getSituation()) {
        case "emergency":
          final double value = neuralNetworkOutput.getNeuralNetworkOutput();
          if (value > 0.5) {
            doShutDown = true;
          }
          break;
      }
    }

    if (doShutDown) {
      return new ChosenAction("shutDown");
    } else {
      return new ChosenAction("doNothing");
    }
  }
}