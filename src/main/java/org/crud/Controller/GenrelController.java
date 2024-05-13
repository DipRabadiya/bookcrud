package org.crud.Controller;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.crud.Model.Book;
import org.crud.Model.Genrel;
import org.crud.Service.BookService;
import org.crud.Service.GenrelService;
import org.crud.pages.PageRequest;

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
    public Response getAllPaged(@BeanParam PageRequest pageRequest) {
        return genrelService.getAllPaged(pageRequest);
    }


    @GET
    @Path("/find/{name}")
    @Transactional
    public Response getAllByCityName(@PathParam("name") String name, @BeanParam PageRequest pageRequest) {
        return genrelService.getAllByCityName(name, pageRequest);
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