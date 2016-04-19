package ru.spbstu.dis.opc.client.api.http.server.data.opc;

import ru.spbstu.dis.opc.client.api.http.server.data.DataInput;
import ru.spbstu.dis.opc.client.api.http.server.data.DataProvider;
import ru.spbstu.dis.opc.client.api.opc.access.Tag;

public class SoftingOpcDataProvider implements DataProvider{
  OPCDataReader opcDataReader;


  public SoftingOpcDataProvider(){
    opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();
  }

  @Override
  public DataInput nextDataPortion() {
    return new DataInput(opcDataReader.getActualValues());
  }
}
