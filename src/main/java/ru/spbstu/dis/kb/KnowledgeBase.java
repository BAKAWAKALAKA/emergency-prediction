package ru.spbstu.dis.kb;

public interface KnowledgeBase {
  KnowledgeBaseOutput inferOutput(KnowledgeBaseInput input);
}
