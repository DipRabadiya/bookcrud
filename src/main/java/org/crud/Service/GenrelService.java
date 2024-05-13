package org.crud.Service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.crud.Model.Book;
import org.crud.Model.Genrel;
import org.crud.Model.PublishingHouse;
import org.crud.Repository.BookRepository;
import org.crud.Repository.GenrelRepository;
import org.crud.Repository.PublishingHouseRepository;
import org.crud.pages.PageRequest;

@ApplicationScoped
public class GenrelService {
    @Inject
    GenrelRepository genrelRepository;

    @Inject
    BookRepository bookRepository;

    public long count() {
        if (genrelRepository.count() == 0)
            throw new WebApplicationException("Cities not found!", Response.Status.NOT_FOUND);

        return genrelRepository.count();
    }

    public Response getAllPaged(PageRequest pageRequest) {
        if (genrelRepository.findAll().count() == 0)
            throw new WebApplicationException("Cities not found!", Response.Status.NOT_FOUND);

        return Response
                .ok(genrelRepository.findAll().page(Page.of(pageRequest.getPageNum(), pageRequest.getPageSize())).list())
                .build();
    }

    public Response getAllByCityName(String name, PageRequest pageRequest) {
        if (genrelRepository.find("name", name).count() == 0)
            throw new WebApplicationException("Name not found!", Response.Status.NOT_FOUND);

        PanacheQuery<Genrel> genrel = genrelRepository.find("name", name);

        return Response.ok(genrel.page(Page.of(pageRequest.getPageNum(), pageRequest.getPageSize())).list()).build();
    }

    public Response addGenrel(Genrel genrel) {
        if (bookRepository.findById(genrel.getBook().getId()) == null)
            throw new WebApplicationException("Genrel not found!", Response.Status.NOT_FOUND);

       Book book = bookRepository.findById(genrel.getBook().getId());

        genrel.getBook().setName(book.getName());
        genrel.getBook().setDescription(book.getDescription());
        genrel.getBook().setYearOfPublication(book.getYearOfPublication());

        genrelRepository.persist(genrel);
        return Response.ok(genrel).status(Response.Status.CREATED).build();
    }


    public Response update(Long id, Genrel genrel) {
        Genrel updateGenrel = genrelRepository.findById(id);

        if (updateGenrel == null)
            throw new WebApplicationException("Genrel not found!", Response.Status.NOT_FOUND);

        Book book = bookRepository.findById(genrel.getBook().getId());
        if(book == null)
            throw new WebApplicationException("Book not found with id: "+genrel.getBook().getId(), Response.Status.NOT_FOUND);

        updateGenrel.setName(genrel.getName());
        updateGenrel.setDescription(genrel.getDescription());
        updateGenrel.setDescription(genrel.getDescription());
        updateGenrel.setBook(book);

        return Response.ok(book).build();
    }


    public Response delete(Long id) {
        if (genrelRepository.findById(id) == null)
            throw new WebApplicationException("Book not found!", Response.Status.NOT_FOUND);

        genrelRepository.deleteById(id);
        return Response.noContent().build();
    }
}