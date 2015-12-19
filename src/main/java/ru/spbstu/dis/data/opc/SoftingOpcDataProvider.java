package ru.spbstu.dis.data.opc;

import ru.spbstu.dis.data.DataInput;
import ru.spbstu.dis.data.DataProvider;
import ru.spbstu.dis.data.Tag;

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
