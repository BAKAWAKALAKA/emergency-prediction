package ru.spbstu.dis;

import java.util.function.Supplier;

@FunctionalInterface
public interface DataProvider extends Supplier<DataInput> {
  @Override
  default DataInput get() {
    return nextDataPortion();
  }

  DataInput nextDataPortion();
}
