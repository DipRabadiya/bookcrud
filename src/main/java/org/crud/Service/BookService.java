package org.crud.Service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.crud.Model.Book;
import org.crud.Model.PublishingHouse;
import org.crud.Repository.BookRepository;
import org.crud.Repository.PublishingHouseRepository;
import org.crud.pages.PageRequest;

import java.util.List;

@ApplicationScoped
public class BookService {
    @Inject
    PublishingHouseRepository publishingHouseRepository;

    @Inject
    BookRepository bookRepository;

    public long count() {
        if (bookRepository.count() == 0)
            throw new WebApplicationException("Books not found!", Response.Status.NOT_FOUND);

        return bookRepository.count();
    }

    public Response getAllPaged(PageRequest pageRequest) {
        if (bookRepository.findAll().count() == 0)
            throw new WebApplicationException("Books not found!", Response.Status.NOT_FOUND);

        return Response
                .ok(bookRepository.findAll().page(Page.of(pageRequest.getPageNum(), pageRequest.getPageSize())).list())
                .build();
    }

    public Response getAllByName(String name, PageRequest pageRequest) {
        if (bookRepository.find("name", name).count() == 0)
            throw new WebApplicationException("Name not found!", Response.Status.NOT_FOUND);

        PanacheQuery<Book> book = bookRepository.find("name", name);

        return Response.ok(book.page(Page.of(pageRequest.getPageNum(), pageRequest.getPageSize())).list()).build();
    }

    public Response addBook(Book book) {
        if (publishingHouseRepository.findById(book.getPublishingHouse().getId()) == null)
            throw new WebApplicationException("Book not found!", Response.Status.NOT_FOUND);

        PublishingHouse publishingHouse = publishingHouseRepository.findById(book.getPublishingHouse().getId());

        book.getPublishingHouse().setName(publishingHouse.getName());
        book.getPublishingHouse().setDescription(publishingHouse.getDescription());
        book.getPublishingHouse().setFoundingYear(publishingHouse.getFoundingYear());

        bookRepository.persist(book);
        return Response.ok(book).status(Response.Status.CREATED).build();
    }


    public Response update(Long id, Book book) {
        Book updateBook = bookRepository.findById(id);

        if (updateBook == null)
            throw new WebApplicationException("Book not found!", Response.Status.NOT_FOUND);

        PublishingHouse publishingHouse = publishingHouseRepository.findById(book.getPublishingHouse().getId());
        if(publishingHouse == null)
            throw new WebApplicationException("Publisher  not found with id: "+book.getPublishingHouse().getId(), Response.Status.NOT_FOUND);

        updateBook.setName(book.getName());
        updateBook.setDescription(book.getDescription());
        updateBook.setYearOfPublication(book.getYearOfPublication());
        updateBook.setPublishingHouse(publishingHouse);

        return Response.ok(publishingHouse).build();
    }


    public Response delete(Long id) {
        if (bookRepository.findById(id) == null)
            throw new WebApplicationException("Book not found!", Response.Status.NOT_FOUND);

        bookRepository.deleteById(id);
        return Response.noContent().build();
    }
}