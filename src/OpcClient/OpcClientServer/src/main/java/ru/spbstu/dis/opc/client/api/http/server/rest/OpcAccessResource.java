package ru.spbstu.dis.opc.client.api.http.server.rest;

import com.google.inject.Inject;
import ru.spbstu.dis.opc.client.api.http.server.services.OpcService;
import ru.spbstu.dis.opc.client.api.opc.access.AvailableTags;
import ru.spbstu.dis.opc.client.api.opc.access.OpcAccessApi;
import ru.spbstu.dis.opc.client.api.opc.access.TagValueBoolean;
import ru.spbstu.dis.opc.client.api.opc.access.TagValueFloat;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1/opc")
@Produces(MediaType.APPLICATION_JSON)
public class OpcAccessResource implements OpcAccessApi {
  private final OpcService opcService;

  @Inject
  public OpcAccessResource(final OpcService opcService) {
    this.opcService = opcService;
  }

  public AvailableTags availableTags() {
    return new AvailableTags(opcService.tags());
  }

  public void writeValueForTag(final String tag, final Boolean value) {
    opcService.writeValueForTag(tag, value);
  }

  public void writeValueForTag(final String tag, final Float value) {
    opcService.writeValueForTag(tag, value);
  }


  public TagValueBoolean readBoolean(final String tag) {
    return new TagValueBoolean(opcService.readBoolean(tag));
  }

  public TagValueFloat readFloat(final String tag) {
    return new TagValueFloat(opcService.readFloat(tag));
  }
}
