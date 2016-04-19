package ru.spbstu.dis.opc.client.api.http.server.data;

import java.util.function.Supplier;

@FunctionalInterface
public interface DataProvider extends Supplier<DataInput> {
  @Override
  default DataInput get() {
    return nextDataPortion();
  }

  DataInput nextDataPortion();
}
