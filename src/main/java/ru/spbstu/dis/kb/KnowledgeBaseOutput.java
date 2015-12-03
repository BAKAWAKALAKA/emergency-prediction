package ru.spbstu.dis.kb;

public class KnowledgeBaseOutput {
  String action;

  public KnowledgeBaseOutput(final String action) {
    this.action = action;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final KnowledgeBaseOutput that = (KnowledgeBaseOutput) o;

    return !(action != null ? !action.equals(that.action) : that.action != null);
  }

  @Override
  public int hashCode() {
    return action != null ? action.hashCode() : 0;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("KnowledgeBaseOutput{");
    sb.append("action='").append(action).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
