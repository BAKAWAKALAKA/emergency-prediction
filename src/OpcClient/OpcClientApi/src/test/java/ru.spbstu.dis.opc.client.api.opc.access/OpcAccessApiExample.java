package ru.spbstu.dis.opc.client.api.opc.access;

import com.google.common.net.HostAndPort;
import ru.spbstu.dis.opc.client.api.OpcClientApiFactory;

public class OpcAccessApiExample {
  public static void main(String[] args) {
    final OpcAccessApi opcAccessApi = OpcClientApiFactory
        .createOpcAccessApi(HostAndPort.fromParts("127.0.0.1", 7998));
    final AvailableTags availableTags = opcAccessApi.availableTags();
    final TagValueBoolean tag = opcAccessApi.readBoolean("tag");
    final TagValueFloat tag1 = opcAccessApi.readFloat("tag");
    final ValueWritten valueWritten = opcAccessApi.writeValueForTag("omg", new Float(5.5));
    final ValueWritten omg = opcAccessApi.writeValueForTag("omg", new Boolean(true));
    int i = 0;
  }
}