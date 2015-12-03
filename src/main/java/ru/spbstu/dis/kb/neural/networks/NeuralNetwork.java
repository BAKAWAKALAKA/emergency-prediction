package ru.spbstu.dis.kb.neural.networks;

import ru.spbstu.dis.kb.DataInput;

public interface NeuralNetwork {
  NeuralNetworkOutput inferOutput(DataInput input);
}
