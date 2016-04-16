package ru.spbstu.dis.opc.client.api.opc.access;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/v1/opc")
@Produces(MediaType.APPLICATION_JSON)
public interface OpcAccessApi {

  @Path("/tags")
  @GET
  AvailableTags availableTags();

  @Path("/boolean/{tag}/{value}")
  @POST
  TagWriteStatus writeValueForTag(
      @NotNull @Size(min = 1) @PathParam("tag") String tag,
      @NotNull @PathParam("value") Boolean value);

  @Path("/float/{tag}/{value}")
  @POST
  TagWriteStatus writeValueForTag(
      @NotNull @Size(min = 1) @PathParam("tag") String tag,
      @NotNull @PathParam("value") Float value);

  @Path("/read/boolean/{tag}")
  @GET
  TagValueBoolean readBoolean(@NotNull @Size(min = 1) @PathParam("tag") String tag);

  @Path("/read/float/{tag}")
  @GET
  TagValueFloat readFloat(@NotNull @Size(min = 1) @PathParam("tag") String tag);
}
