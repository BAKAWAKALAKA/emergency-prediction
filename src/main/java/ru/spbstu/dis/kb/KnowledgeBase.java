package ru.spbstu.dis.kb;

import ru.spbstu.dis.data.DataInput;

public interface KnowledgeBase {
  ChosenAction inferOutput(DataInput input);
}
