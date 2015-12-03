package ru.spbstu.dis.kb.neural.networks.examples;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.*;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.*;
import java.util.Arrays;

public class HebbianEmergencyChemStation{


  public static void main(String args[]) {

  // create training set
  DataSet trainingSet;
  int nInputs, nOutputs;

  nInputs = 3;
  nOutputs = 2;
  trainingSet = new DataSet(nInputs, nOutputs);
  createTrainingSet_PT_binary(trainingSet);

  nInputs = 3;
  nOutputs = 2;
  trainingSet = new DataSet(nInputs, nOutputs);
  createTrainingSet_PT_binary(trainingSet);

  SupervisedHebbianNetwork myHebb = new SupervisedHebbianNetwork(nInputs, nOutputs);
  setRules(myHebb);
  myHebb.learn(trainingSet);
  // add one more 'incomplete' pattern for testing

  trainingSet.addRow(new DataSetRow(new double[]{0.075,0.065, 0.06}, new double[]{0,1}));
  trainingSet.addRow(new DataSetRow(new double[]{0.08,0.02,0.02}, new double[]{1,1}));
  trainingSet.addRow(new DataSetRow(new double[]{0.18, 0.18,  0.1}, new double[]{0,0}));
  trainingSet.addRow(new DataSetRow(new double[]{0.17,  0.1,0.02}, new double[]{1,0}));

  // print network output for the each element from the specified training set.
  trainNeuralNetwork(trainingSet, myHebb);
  System.out.println("Testing MLP");
  NeuralNetwork myPerceptron = new Perceptron(nInputs, nOutputs);
  setRules(myPerceptron);
  // learn the training set
  //myPerceptron.learn(trainingSet);
  // test perceptron

  //trainNeuralNetwork(trainingSet, myPerceptron);
}

  private static void setRules(final NeuralNetwork myNeuralNetwork) {
    SupervisedLearning learningRule2 = (SupervisedLearning) myNeuralNetwork.getLearningRule();
    learningRule2.setMaxError(0.00001);
    learningRule2.setMaxIterations( 100000);
  }

  private static void trainNeuralNetwork(final DataSet trainingSet,
                                         final NeuralNetwork myNeuralNetwork) {

    int i=0, k1=0, k2=0, k3=0;
    double out1=0, out2=0;
    for (DataSetRow trainingElement : trainingSet.getRows()) {
      myNeuralNetwork.setInput(trainingElement.getInput());
      myNeuralNetwork.calculate();
      double[] networkOutput = myNeuralNetwork.getOutput();
      System.out.print("i = " +i);
      System.out.print("  Input: " + Arrays.toString(trainingElement.getInput()));
      System.out.print(" Desired: " + Arrays.toString(trainingElement.getDesiredOutput()));
      System.out.println(" Output: " + Arrays.toString(networkOutput));
      i++;
      out1=trainingElement.getDesiredOutput()[0];
      out2=networkOutput[0];
      if (Math.abs(out1-Math.abs(out2))>0.5) k1++;


      out1=trainingElement.getDesiredOutput()[1];
      out2=networkOutput[1];
      if (Math.abs(out1-Math.abs(out2))>0.5) k2++;
    }
    System.out.println("Errors1: "+k1 +" Errors2: " +k2);

  }

  private static void createTrainingSet_PT_binary(final DataSet trainingSet) {
   // variate pressure
    trainingSet.addRow(new DataSetRow(new double[]{0.06,  0.07,0.02  }, 
                                      new double[]{0, 0}));
    trainingSet.addRow(new DataSetRow(new double[]{ 0.07,  0.07,0.02  },
                                      new double[]{0, 0}));
    trainingSet.addRow(new DataSetRow(new double[]{ 0.07, 0.06,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.15, 0.145,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.15,0.025,0.02  },
                                      new double[]{1, 1}));//падение давления >50, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.15,0.045,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.24,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.25,0.025,0.02  },
                                      new double[]{1, 1}));//падение давления >50, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.25,0.055,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25,0.075,0.02  },
                                      new double[]{1, 0}));//падение давления >50

    trainingSet.addRow(new DataSetRow(new double[]{0.25,0.025,0.02  },
                                      new double[]{0, 0}));//падение давления 
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.2,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.195,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.185,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.175,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.105,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.095,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.05,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    
    trainingSet.addRow(new DataSetRow(new double[]{0.23, 0.22,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.23,  0.07,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.23, 0.17,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.23,  0.1,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    
    trainingSet.addRow(new DataSetRow(new double[]{0.21, 0.2,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.21, 0.04,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.21, 0.14,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.21,  0.1,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.21, 0.09,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.21, 0.16,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.17,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.04,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.14,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.09,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.08,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.04,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    
    
    
    trainingSet.addRow(new DataSetRow(new double[]{0.16,  0.13,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.16,  0.1,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.16, 0.09,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.16, 0.08,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.16, 0.06,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.16,0.025,0.02 },
                                      new double[]{1, 1}));//падение давления >50, ///
    trainingSet.addRow(new DataSetRow(new double[]{0.16, 0.15,0.02 },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.16,  0.12,0.02 },
                                      new double[]{0, 0}));//падение давления

    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.18,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19,  0.1,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.09,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.04,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.03,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.05,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.08,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19,  0.12,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.14,0.02 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.15,0.02 },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.16,0.02 },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.17,0.02 },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.18,0.02 },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.19,0.02 },
                                      new double[]{0, 0}));

    trainingSet.addRow(new DataSetRow(new double[]{0.16, 155,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.14,  0.11,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{ 0.12,  0.11,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{ 0.11,  0.11,0.02  },
                                      new double[]{0, 0}));
    trainingSet.addRow(new DataSetRow(new double[]{ 0.1, 0.09,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{ 0.1, 0.099,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{ 0.1, 0.08,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.09,  0.07,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.08,  0.07,0.02  },
                                      new double[]{0, 0}));//падение давления
    // variate lower pressure
    trainingSet.addRow(new DataSetRow(new double[]{0.08,  0.07, 0.06  },
                                      new double[]{0, 1}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{ 0.07, 0.04,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.08,0.045,0.02  },
                                      new double[]{0, 0}));//падение давления
    // variate lower pressure
    trainingSet.addRow(new DataSetRow(new double[]{0.08,0.045, 0.04  },
                                      new double[]{0, 1}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.08, 0.04, 0.04  },
                                      new double[]{0, 1}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{ 0.07,0.02,0.02  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.09, 0.04,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.09, 0.03,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.09,0.025,0.02  },
                                      new double[]{1, 1}));//падение давления >50, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.06, 0.04,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.06,0.01,0.02  },
                                      new double[]{1, 1}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.085,0.035,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.085,0.025,0.02  },
                                      new double[]{1, 0}));//падение давления >50
    
    trainingSet.addRow(new DataSetRow(new double[]{0.02,0.025,0.02  },
                                      new double[]{0, 0}));
    trainingSet.addRow(new DataSetRow(new double[]{0.025,0.02,0.02  },
                                      new double[]{0, 1}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.02, 0.03,0.02  },
                                      new double[]{0, 0}));
    trainingSet.addRow(new DataSetRow(new double[]{0.04, 0.03,0.02  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.03,0.025,0.02  },
                                      new double[]{0, 1}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.04,0.02,0.02  },
                                      new double[]{0, 1}));//падение давления, <0.8min




    trainingSet.addRow(new DataSetRow(new double[]{0.06,  0.07,  0.07  },
                                      new double[]{0, 1}));
    trainingSet.addRow(new DataSetRow(new double[]{ 0.07,  0.07, 0.06  },
                                      new double[]{0, 1}));
    trainingSet.addRow(new DataSetRow(new double[]{ 0.07, 0.06, 0.05  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.15, 0.145, 0.14  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.15,0.025,0.025  },
                                      new double[]{1, 1}));//падение давления >50, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.15,0.045,0.035  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.24, 0.2  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.25,0.025,0.01  },
                                      new double[]{1, 0}));//падение давления >50, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.25,0.055, 0.05  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25,0.075, 0.06  },
                                      new double[]{1, 1}));//падение давления >50

    trainingSet.addRow(new DataSetRow(new double[]{0.25,0.025, 0.16  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.2, 0.16  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.195, 0.16  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.185, 0.15  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.175, 0.15  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.105, 0.08  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.095, 0.09  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.25, 0.05, 0.05  },
                                      new double[]{1, 1}));//падение давления >50

    trainingSet.addRow(new DataSetRow(new double[]{0.23, 0.22, 0.2  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.23,  0.07, 0.06  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.23, 0.17,  0.13  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.23, 0.17, 0.14  },
                                      new double[]{1, 1}));//падение давления >50

    trainingSet.addRow(new DataSetRow(new double[]{0.21, 0.2, 0.19  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.21, 0.04,0.035  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.21, 0.04, 0.03  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.21,  0.1, 0.08  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.21, 0.09,  0.07  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.21, 0.16,  0.12  },
                                      new double[]{1, 0}));//падение давления >50

    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.17, 0.14  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.04,0.01  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.14,  0.12  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.09, 0.08  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.08,  0.07  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.04, 0.04 },
                                      new double[]{1, 1}));//падение давления >50

//aaaaaaaaaaaaaaaaaaa

    trainingSet.addRow(new DataSetRow(new double[]{0.16,  0.13,  0.1 },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.16,  0.1,  0.07 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.16, 0.09,  0.07 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.16, 0.08,  0.07 },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.16, 0.06,  0.07 },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.16,0.025,0.01 },
                                      new double[]{1, 0}));//падение давления >50, ///
    trainingSet.addRow(new DataSetRow(new double[]{0.16, 0.15,  0.13 },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.16,  0.12,  0.1 },
                                      new double[]{0, 1}));//падение давления

    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.18, 0.15  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19,  0.1, 0.06 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.09, 0.06 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.04, 0.06 },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.03,0.01 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.05, 0.04 },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.08, 0.06 },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19,  0.12,  0.12 },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.14,  0.12 },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.15, 125 },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.16,  0.12 },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.17,  0.12 },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.18, 0.150 },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.19, 0.19, 0.09 },
                                      new double[]{0, 0}));

    trainingSet.addRow(new DataSetRow(new double[]{0.16, 155, 0.09  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.14,  0.11, 0.09  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{ 0.12,  0.11, 0.08  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{ 0.11,  0.11,  0.07  },
                                      new double[]{0, 0}));
    trainingSet.addRow(new DataSetRow(new double[]{ 0.1, 0.09, 0.08  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{ 0.1, 0.099, 0.08  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{ 0.1, 0.08, 0.08  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.09,  0.07, 0.08  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.08,  0.07,0.065  },
                                      new double[]{0, 1}));//падение давления
    // variate lower pressure
    trainingSet.addRow(new DataSetRow(new double[]{0.08,  0.07, 0.04  },
                                      new double[]{0, 0}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{ 0.07, 0.04,0.045  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.08,0.045,0.045  },
                                      new double[]{0, 1}));//падение давления
    // variate lower pressure
    trainingSet.addRow(new DataSetRow(new double[]{0.08,0.045, 0.03  },
                                      new double[]{0, 0}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.08, 0.04,0.01  },
                                      new double[]{0, 0}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{ 0.07,0.02,0.01  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.09, 0.04, 0.04  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.09, 0.03, 0.03  },
                                      new double[]{1, 1}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.09,0.025, 0.04  },
                                      new double[]{1, 1}));//падение давления >50, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.06, 0.04, 0.04  },
                                      new double[]{0, 1}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.06,0.01,0.01  },
                                      new double[]{1, 1}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.085,0.035,0.01  },
                                      new double[]{1, 0}));//падение давления >50
    trainingSet.addRow(new DataSetRow(new double[]{0.085,0.025,0.01  },
                                      new double[]{1, 0}));//падение давления >50

    trainingSet.addRow(new DataSetRow(new double[]{0.02,0.025,0.01  },
                                      new double[]{0, 0}));
    trainingSet.addRow(new DataSetRow(new double[]{0.025,0.02, 0.03  },
                                      new double[]{0, 1}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.02, 0.03,0.01  },
                                      new double[]{0, 0}));
    trainingSet.addRow(new DataSetRow(new double[]{0.04, 0.03,0.01  },
                                      new double[]{0, 0}));//падение давления
    trainingSet.addRow(new DataSetRow(new double[]{0.03,0.025, 0.03  },
                                      new double[]{0, 1}));//падение давления, <0.8min
    trainingSet.addRow(new DataSetRow(new double[]{0.04,0.02,0.025  },
                                      new double[]{0, 1}));//падение давления, <0.8min
  }
}
