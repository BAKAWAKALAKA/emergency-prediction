package ru.spbstu.dis.kb;

import ru.spbstu.dis.ChosenAction;
import ru.spbstu.dis.DataInput;

public interface KnowledgeBase {
  ChosenAction inferOutput(DataInput input);
}
