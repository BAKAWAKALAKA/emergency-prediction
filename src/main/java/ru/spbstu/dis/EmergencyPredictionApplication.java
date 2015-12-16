package ru.spbstu.dis;

import com.google.common.collect.Lists;
import ru.spbstu.dis.kb.NeuroFuzzyKnowledgeBase;
import ru.spbstu.dis.kb.fuzzy.inference.FuzzyInferenceEngine;
import ru.spbstu.dis.kb.classification.neural.networks.NeuralNetwork;
import ru.spbstu.dis.kb.classification.neural.networks.NeuralNetworkOutput;
import ru.spbstu.dis.opc.SoftingOpcDataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EmergencyPredictionApplication {
  public static void main(String[] args) {
    final SoftingOpcDataProvider softingOpcDataProvider = new SoftingOpcDataProvider();
    final NeuroFuzzyKnowledgeBase neuroFuzzyKnowledgeBase = new NeuroFuzzyKnowledgeBase(
        getNeuralNetworks(), getFuzzyEngine());

    final EmergencyPredictor emergencyPredictor =
        new EmergencyPredictor(
            softingOpcDataProvider,
            neuroFuzzyKnowledgeBase,
            chosenAction -> {
              System.out.println("EMERGENCY PREDICTOR GENERATED ACTION " + chosenAction);
            });

    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
        () -> {
          try {
            emergencyPredictor.execute();
          } catch (Exception e) {
            e.printStackTrace();
          }
        },
        0L, 1000L, TimeUnit.MILLISECONDS);
  }

  private static ArrayList<NeuralNetwork> getNeuralNetworks() {
    return Lists.newArrayList(new NeuralNetwork() {
      @Override
      public NeuralNetworkOutput inferOutput(final DataInput input) {
        return new NeuralNetworkOutput("s1", 1.0);
      }
    });
  }

  private static FuzzyInferenceEngine getFuzzyEngine() {
    return new FuzzyInferenceEngine() {
      @Override
      public ChosenAction generateAction(
          final List<NeuralNetworkOutput> listOfNeuralNetworkOutputs) {
        return new ChosenAction("test");
      }
    };
  }
}
