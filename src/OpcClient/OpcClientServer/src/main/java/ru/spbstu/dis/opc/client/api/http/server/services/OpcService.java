package ru.spbstu.dis.opc.client.api.http.server.services;

import java.util.Set;

public interface OpcService {

  Set<String> tags();

  void writeValueForTag(String tag, Boolean value);

  void writeValueForTag(String tag, Float value);

  Boolean readBoolean(String tag);

  Float readFloat(String tag);
}
