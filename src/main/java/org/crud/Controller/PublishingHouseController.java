package org.crud.Controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.crud.Model.PublishingHouse;
import org.crud.Service.PublishingHouseService;
import org.crud.pages.PageRequest;

@Path("/publishingHouses")
@Produces(MediaType.APPLICATION_JSON)
public class PublishingHouseController {

    @Context
    UriInfo uriInfo;

    @Inject
    PublishingHouseService publishingHouseService;

    @GET
    @Path("/count")
    @Transactional
    public long count() {
        return publishingHouseService.count();
    }

    @GET
    @Transactional
    public Response getAllPaged(@BeanParam PageRequest pageRequest) {
        return publishingHouseService.getAllPaged(pageRequest);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addPublish(PublishingHouse publishingHouse) {
        return publishingHouseService.addPublish(publishingHouse, uriInfo);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, PublishingHouse publishingHouse) {
        return publishingHouseService.update(id, publishingHouse);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        return publishingHouseService.delete(id);
    }
}
