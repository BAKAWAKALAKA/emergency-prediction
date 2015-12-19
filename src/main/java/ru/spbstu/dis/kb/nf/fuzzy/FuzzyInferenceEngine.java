package ru.spbstu.dis.kb.nf.fuzzy;

import ru.spbstu.dis.kb.ChosenAction;
import ru.spbstu.dis.kb.nf.nn.NeuralNetworkOutput;
import java.util.List;

public interface FuzzyInferenceEngine {
  ChosenAction generateAction(List<NeuralNetworkOutput> listOfNeuralNetworkOutputs);
}
