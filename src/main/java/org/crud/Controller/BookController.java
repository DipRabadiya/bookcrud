package org.crud.Controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.crud.Service.BookService;
import org.crud.Model.Book;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookController {

    @Inject
    BookService bookService;

    @GET
    @Path("/count")
    @Transactional
    public long count() {
        return bookService.count();
    }

    @GET
    @Transactional
    public Response getAllBooks() {
        return bookService.getAllBooks();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response persist(Book book) {
        return bookService.addBook(book);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id,Book book) {
        return bookService.update(id, book);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        return bookService.delete(id);
    }
}