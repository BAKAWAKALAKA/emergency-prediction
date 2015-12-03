package ru.spbstu.dis.opc;

import com.google.common.collect.Lists;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DuplicateGroupException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

public class OPCConnectionExample {
  public static void main(String[] args)
  throws InterruptedException, AlreadyConnectedException, JIException, UnknownHostException,
      NotConnectedException, DuplicateGroupException, AddFailedException {

    final ArrayList<String> tagsToRead = Lists.newArrayList(
        "maths.sin",
        "maths.cos",
        "maths.tan"
    );

    final OPCDataReader opcDataReader = new OPCDataReader(tagsToRead).startReading();

    while (true) {
      final Map<String, Double> actualValues = opcDataReader.getActualValues();
      System.out.println(actualValues);
      Thread.sleep(1000);
    }
  }
}

