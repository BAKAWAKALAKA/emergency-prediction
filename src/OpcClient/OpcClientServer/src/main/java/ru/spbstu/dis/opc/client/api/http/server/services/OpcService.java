package ru.spbstu.dis.opc.client.api.http.server.services;

import java.util.Set;

public interface OpcService {

  Set<String> tags();

  Integer writeValueForTag(String tag, Boolean value);

  Integer writeValueForTag(String tag, Float value);

  Boolean readBoolean(String tag);

  Float readFloat(String tag);
}
