package ru.spbstu.dis.kb.fuzzy.inference;

import ru.spbstu.dis.ChosenAction;
import ru.spbstu.dis.kb.neural.networks.NeuralNetworkOutput;
import java.util.List;

public interface FuzzyInferenceEngine {
  ChosenAction generateAction(List<NeuralNetworkOutput> listOfNeuralNetworkOutputs);
}
