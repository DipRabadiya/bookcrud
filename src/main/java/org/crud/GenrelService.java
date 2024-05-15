package org.crud;

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
import org.crud.pages.PageRequest;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class GenrelService {
    @Inject
    GenrelRepository genrelRepository;

    @Inject
    BookRepository bookRepository;
    private static final Logger LOGGER = Logger.getLogger(GenrelService.class.getName());


    public long count() {
        LOGGER.log(Level.INFO, "Attempting to count Genrels...");
        Long count = genrelRepository.count();
        if (count == 0) {
            LOGGER.log(Level.WARNING, "No Genrels found.");
            throw new WebApplicationException("Genrels not found!", Response.Status.NOT_FOUND);
        }
        LOGGER.log(Level.INFO, "Number of genrels found: " + count);
        return count;
    }

    public Response getAllGenrels() {
        LOGGER.log(Level.INFO, "Attempting to retrieve all Genrels...");
        List<Genrel> genrels = genrelRepository.listAll();
        if (genrels.isEmpty()) {
            LOGGER.log(Level.WARNING, "No Genrels found.");
            throw new WebApplicationException("Genrels not found!", Response.Status.NOT_FOUND);
        }
        LOGGER.log(Level.INFO, "Genrels retrieved successfully.");
        return Response.ok(genrels).build();
    }

    public Response getAllByName(String name, PageRequest pageRequest) {
        LOGGER.log(Level.INFO, "Attempting to retrieve Genrels by name: " + name);
        PanacheQuery<Genrel> genrelQuery = genrelRepository.find("name", name);
        if (genrelQuery.count() == 0) {
            LOGGER.log(Level.WARNING, "No Genrels found with name: " + name);
            throw new WebApplicationException("Genrels not found with name: " + name, Response.Status.NOT_FOUND);
        }
        LOGGER.log(Level.INFO, "Genrels retrieved successfully.");
        return Response.ok(genrelQuery.page(Page.of(pageRequest.getPageNum(), pageRequest.getPageSize())).list()).build();
    }

    public Response addGenrel(Genrel genrel) {
        LOGGER.log(Level.INFO, "Attempting to add Genrel...");
        Book book = bookRepository.findById(genrel.getBook().getId());
        if (book == null) {
            LOGGER.log(Level.WARNING, "Book with ID " + genrel.getBook().getId() + " not found.");
            throw new WebApplicationException("Book not found!", Response.Status.NOT_FOUND);
        }
        genrel.setBook(book);
        genrelRepository.persist(genrel);
        LOGGER.log(Level.INFO, "Genrel added successfully.");
        return Response.ok(genrel).status(Response.Status.CREATED).build();
    }


    public Response update(Long id, Genrel genrel) {
        LOGGER.log(Level.INFO, "Attempting to update Genrel with ID: " + id);
        Genrel updateGenrel = genrelRepository.findById(id);
        if (updateGenrel == null) {
            LOGGER.log(Level.WARNING, "Genrel with ID " + id + " not found.");
            throw new WebApplicationException("Genrel not found!", Response.Status.NOT_FOUND);
        }
        Book book = bookRepository.findById(genrel.getBook().getId());
        if (book == null) {
            LOGGER.log(Level.WARNING, "Book with ID " + genrel.getBook().getId() + " not found.");
            throw new WebApplicationException("Book not found!", Response.Status.NOT_FOUND);
        }
        updateGenrel.setName(genrel.getName());
        updateGenrel.setDescription(genrel.getDescription());
        updateGenrel.setBook(book);
        LOGGER.log(Level.INFO, "Genrel updated successfully.");
        return Response.ok(updateGenrel).build();
    }


    public Response delete(Long id) {
        LOGGER.log(Level.INFO, "Attempting to delete Genrel with ID: " + id);
        Genrel deleteGenrel = genrelRepository.findById(id);
        if (deleteGenrel == null) {
            LOGGER.log(Level.WARNING, "Genrel with ID " + id + " not found.");
            throw new WebApplicationException("Genrel not found!", Response.Status.NOT_FOUND);
        }
        genrelRepository.deleteById(id);
        LOGGER.log(Level.INFO, "Genrel deleted successfully.");
        return Response.noContent().build();
    }
}