package ru.spbstu.dis.opc;

import ru.spbstu.dis.DataInput;
import ru.spbstu.dis.DataProvider;
import ru.spbstu.dis.Tag;

public class OpcDataProvider implements DataProvider{
  OPCDataReader opcDataReader;


  public OpcDataProvider(){
    opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();
  }

  @Override
  public DataInput nextDataPortion() {
    return new DataInput(opcDataReader.getActualValues());
  }
}
