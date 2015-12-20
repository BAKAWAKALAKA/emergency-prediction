package ru.spbstu.dis.ep.kb;

import com.google.common.collect.Lists;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import ru.spbstu.dis.ep.data.DataInput;
import ru.spbstu.dis.ep.data.Tag;
import ru.spbstu.dis.ep.kb.nf.NeuroFuzzyKnowledgeBase;
import ru.spbstu.dis.ep.kb.nf.fuzzy.FuzzyInferenceEngine;
import ru.spbstu.dis.ep.kb.nf.nn.NeuralNetwork;
import ru.spbstu.dis.ep.kb.nf.nn.NeuralNetworkOutput;
import java.util.ArrayList;
import java.util.List;

public class NeuroFuzzyKnowledgeBaseTest {

  @Test
  public void shouldInferForSimpleFuzzyNeuralImplementations_case1() {
    // given
    final ArrayList<NeuralNetwork> networks =
        Lists.newArrayList(new StupidNeuralNetwork(), new OtherStupidNeuralNetwork());
    final FuzzyInferenceEngine stupidFuzzyInferenceEngine =
        new StupidFuzzyInferenceEngine();
    final NeuroFuzzyKnowledgeBase neuroFuzzyKnowledgeBase = new NeuroFuzzyKnowledgeBase(networks,
        stupidFuzzyInferenceEngine);
    final DataInput dataInput = DataInput.withPressureAndLowerPressure(10, 5);

    // when
    final ChosenAction chosenAction = neuroFuzzyKnowledgeBase.inferOutput(dataInput);

    // then
    assertThat(chosenAction).isEqualTo(new ChosenAction("shutDown"));
  }
}


class StupidNeuralNetwork implements NeuralNetwork {
  @Override
  public NeuralNetworkOutput inferOutput(final DataInput input) {
    if (input.getDataForTag(Tag.PRESSURE) > 5) {
      return new NeuralNetworkOutput("emergency", 1);
    } else {
      return new NeuralNetworkOutput("emergency", 0);
    }
  }
}

class OtherStupidNeuralNetwork implements NeuralNetwork {
  @Override
  public NeuralNetworkOutput inferOutput(final DataInput input) {
    if (input.getDataForTag(Tag.PRESSURE) > 50) {
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