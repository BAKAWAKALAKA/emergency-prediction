package ru.spbstu.dis.opc.client.api.opc.access;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/api/v1/opc")
@Produces(MediaType.APPLICATION_JSON)
public interface OpcAccessApi {

  @Path("/tags")
  @GET
  AvailableTags availableTags();

  @Path("/boolean/{value}")
  @POST
  void writeValueForTag(
      @NotNull @Size(min = 1) @QueryParam("tag") String tag,
      @NotNull @QueryParam("value") Boolean value);

  @Path("/float/{value}")
  @POST
  void writeValueForTag(
      @NotNull @Size(min = 1) @QueryParam("tag") String tag,
      @NotNull @QueryParam("value") Float value);

  @Path("/read/boolean")
  @GET
  TagValueBoolean readBoolean(@NotNull @Size(min = 1) @QueryParam("tag") String tag);

  @Path("/read/float")
  @GET
  TagValueFloat readFloat(@NotNull @Size(min = 1) @QueryParam("tag") String tag);
}
