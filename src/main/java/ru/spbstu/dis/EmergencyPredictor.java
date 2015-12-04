package ru.spbstu.dis;

import ru.spbstu.dis.kb.ChosenAction;
import ru.spbstu.dis.kb.DataInput;
import ru.spbstu.dis.kb.KnowledgeBase;
import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
interface DataProvider extends Supplier<DataInput> {
  @Override
  default DataInput get() {
    return nextDataPortion();
  }

  DataInput nextDataPortion();
}

@FunctionalInterface
interface ChosenActionListener extends Consumer<ChosenAction> {

}

public class EmergencyPredictor {
  private DataProvider dataProvider;

  private KnowledgeBase knowledgeBase;

  private ChosenActionListener chosenActionListener;

  public EmergencyPredictor(final DataProvider dataProvider, final KnowledgeBase knowledgeBase,
      final ChosenActionListener chosenActionListener) {
    this.dataProvider = dataProvider;
    this.knowledgeBase = knowledgeBase;
    this.chosenActionListener = chosenActionListener;
  }

  public DataProvider getDataProvider() {
    return dataProvider;
  }

  public KnowledgeBase getKnowledgeBase() {
    return knowledgeBase;
  }

  public ChosenActionListener getChosenActionListener() {
    return chosenActionListener;
  }

  public void execute() {
    final DataInput integerDoubleHashMap = dataProvider.nextDataPortion();
    final ChosenAction chosenAction = knowledgeBase.inferOutput(integerDoubleHashMap);
    chosenActionListener.accept(chosenAction);
  }
}