package org.crud.Controller;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.crud.Service.GenrelService;
import org.crud.Model.Genrel;

@Path("/genres")
@Produces(MediaType.APPLICATION_JSON)
public class GenrelController {

    @Inject
    GenrelService genrelService;

    @GET
    @Path("/count")
    @Transactional
    public long count() {
        return genrelService.count();
    }

    @GET
    @Transactional
    public Response getAllGenrels() {
        return genrelService.getAllGenrels();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response persist(Genrel genrel) {
        return genrelService.addGenrel(genrel);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id,Genrel genrel) {
        return genrelService.update(id, genrel);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        return genrelService.delete(id);
    }

}