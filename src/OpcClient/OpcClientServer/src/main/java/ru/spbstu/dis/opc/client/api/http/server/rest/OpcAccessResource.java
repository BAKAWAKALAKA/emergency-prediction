package ru.spbstu.dis.opc.client.api.http.server.rest;

import ru.spbstu.dis.opc.client.api.opc.access.AvailableTags;
import ru.spbstu.dis.opc.client.api.opc.access.OpcAccessApi;
import ru.spbstu.dis.opc.client.api.opc.access.TagValueBoolean;
import ru.spbstu.dis.opc.client.api.opc.access.TagValueFloat;
import ru.spbstu.dis.opc.client.api.opc.access.TagWriteStatus;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("api/1.0/opc")
@Produces(MediaType.APPLICATION_JSON)
public class OpcAccessResource implements OpcAccessApi {

  public AvailableTags availableTags() {
    return null;
  }

  public TagWriteStatus writeValueForTag(final String tag, final Boolean value) {
    return null;
  }

  public TagValueBoolean readBoolean(final String tag) {
    return null;
  }

  public TagValueFloat readFloat(final String tag) {
    return null;
  }
}
