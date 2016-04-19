package ru.spbstu.dis.opc.client.api.http.server.data.opc;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.*;
import org.openscada.opc.lib.da.*;
import ru.spbstu.dis.opc.client.api.opc.access.Tag;
import java.net.UnknownHostException;
import java.util.Map;

public class OPCConnectionExample {
  public static void main(String[] args)
  throws InterruptedException, AlreadyConnectedException, JIException, UnknownHostException,
      NotConnectedException, DuplicateGroupException, AddFailedException {



    final OPCDataReader opcDataReader = new OPCDataReader(Tag.TAG_TO_ID_MAPPING).startReading();

    while (true) {
      final Map<Tag, Double> actualValues = opcDataReader.getActualValues();
      System.out.println(actualValues);
      Thread.sleep(1000);
    }
  }
}

