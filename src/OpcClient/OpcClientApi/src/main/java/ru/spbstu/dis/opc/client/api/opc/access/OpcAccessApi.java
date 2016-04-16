package ru.spbstu.dis.opc.client.api.opc.access;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("api/1.0/opc")
@Produces(MediaType.APPLICATION_JSON)
public interface OpcAccessApi {
  @Path("/tags")
  @GET
  AvailableTags availableTags();

  @Path("/boolean")
  @POST
  TagWriteStatus writeValueForTag(@NotNull @Size(min = 1) String tag, @NotNull Boolean value);

  @Path("/read/boolean")
  @GET
  TagValueBoolean readBoolean(@NotNull @Size(min = 1) String tag);

  @Path("/read/float")
  @GET
  TagValueFloat readFloat(@NotNull @Size(min = 1) String tag);
}
