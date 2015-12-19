package ru.spbstu.dis.kb.nf.nn;

import ru.spbstu.dis.data.DataInput;

public interface NeuralNetwork {
  NeuralNetworkOutput inferOutput(DataInput input);
}
