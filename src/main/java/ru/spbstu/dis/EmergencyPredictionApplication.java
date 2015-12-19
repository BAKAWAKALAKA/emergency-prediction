package ru.spbstu.dis;

import com.google.common.collect.Lists;
import ru.spbstu.dis.data.DataProvider;
import ru.spbstu.dis.data.random.RandomDataProvider;
import ru.spbstu.dis.kb.KnowledgeBase;
import ru.spbstu.dis.kb.dcep.DCepKnowledgeBase;
import ru.spbstu.dis.kb.nf.fuzzy.FuzzyInferenceEngine;
import ru.spbstu.dis.kb.nf.nn.NeuralNetwork;
import ru.spbstu.dis.kb.nf.nn.NeuralNetworkOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EmergencyPredictionApplication {
  public static void main(String[] args) {
//    final DataProvider dataProvider = new SoftingOpcDataProvider();
//    final KnowledgeBase kb = new NeuroFuzzyKnowledgeBase(getNeuralNetworks(), getFuzzyEngine());
    DataProvider dataProvider = new RandomDataProvider();
    KnowledgeBase kb = new DCepKnowledgeBase();

    final EmergencyPredictor emergencyPredictor =
        new EmergencyPredictor(
            dataProvider,
            kb,
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
}
