package ru.spbstu.dis.opc.client.api.opc.access;

import com.google.common.net.HostAndPort;
import ru.spbstu.dis.opc.client.api.OpcClientApiFactory;

public class OpcAccessApiExample {
  public static void main(String[] args) {
    final OpcAccessApi opcAccessApi = OpcClientApiFactory
        .createOpcAccessApi(HostAndPort.fromParts("10.18.129.6", 7998));
    checkPump(opcAccessApi);
//    checkFloatWrite(opcAccessApi);
  }

  private static void checkFloatWrite(final OpcAccessApi opcAccessApi) {
    final TagValueFloat tag1 = opcAccessApi.readFloat("tag");
    opcAccessApi.writeValueForTag("omg", new Float(5.5));
  }

  private static void checkPump(final OpcAccessApi opcAccessApi) {
    final AvailableTags availableTags = opcAccessApi.availableTags();
    final String pumpName = "FilterConnection/M/TP_1M2";
    final TagValueBoolean pumpStatus = opcAccessApi.readBoolean(pumpName);
    opcAccessApi.writeValueForTag("FilterConnection/M/TP_1M1", !pumpStatus.value);
  }
}