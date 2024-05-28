package org.crud.Service;

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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class BookService {
    @Inject
    PublishingHouseRepository publishingHouseRepository;

    @Inject
    BookRepository bookRepository;
    @Inject
    GenrelRepository genrelRepository;

    private static final Logger LOGGER = Logger.getLogger(BookService.class.getName());


    public long count() {
        LOGGER.log(Level.INFO, "Attempting to count books...");
        Long count = bookRepository.count();
        if (count == 0) {
            LOGGER.log(Level.WARNING, "No books found.");
            throw new WebApplicationException("Books not found!", Response.Status.NOT_FOUND);
        }
        LOGGER.log(Level.INFO, "Number of books found: " + count);
        return count;
    }

    public Response getAllBooks() {
        LOGGER.log(Level.INFO, "Attempting to retrieve all Books...");
        List<Book> books = bookRepository.listAll();
        if (books.isEmpty()) {
            LOGGER.log(Level.WARNING, "No Books found.");
            throw new WebApplicationException("Books not found!", Response.Status.NOT_FOUND);
        }
        LOGGER.log(Level.INFO, "Books retrieved successfully.");
        return Response.ok(books).build();
    }


    public Response addBook(Book book) {
        LOGGER.log(Level.INFO, "Attempting to add book: " + book.getName());
        PublishingHouse publishingHouse = publishingHouseRepository.findById(book.getPublishingHouse().getId());
        if (publishingHouse == null) {
            LOGGER.log(Level.WARNING, "Publishing house with ID " + book.getPublishingHouse().getId() + " not found.");
            throw new WebApplicationException("Publishing house not found!", Response.Status.NOT_FOUND);
        }
        book.setPublishingHouse(publishingHouse);
        bookRepository.persist(book);
        LOGGER.log(Level.INFO, "Book added successfully.");
        return Response.ok(book).status(Response.Status.CREATED).build();
    }


    public Response update(Long id, Book book) {
        LOGGER.log(Level.INFO, "Attempting to update book with ID: " + id);
        Book updateBook = bookRepository.findById(id);
        if (updateBook == null) {
            LOGGER.log(Level.WARNING, "Book with ID " + id + " not found.");
            throw new WebApplicationException("Book not found!", Response.Status.NOT_FOUND);
        }
        if (book.getPublishingHouse() == null || book.getPublishingHouse().getId() == null) {
            LOGGER.log(Level.WARNING, "Publishing house ID is null.");
            throw new WebApplicationException("Publishing house ID is null!", Response.Status.BAD_REQUEST);
        }

        PublishingHouse publishingHouse = publishingHouseRepository.findById(book.getPublishingHouse().getId());
        if (publishingHouse == null) {
            LOGGER.log(Level.WARNING, "Publishing house with ID " + book.getPublishingHouse().getId() + " not found.");
            throw new WebApplicationException("Publisher not found with ID: " + book.getPublishingHouse().getId(), Response.Status.NOT_FOUND);
        }
        updateBook.setName(book.getName());
        updateBook.setDescription(book.getDescription());
        updateBook.setYearOfPublication(book.getYearOfPublication());
        updateBook.setPublishingHouse(publishingHouse);
        LOGGER.log(Level.INFO, "Book updated successfully.");
        return Response.ok(updateBook).build();
    }

    public Response delete(Long id) {
        LOGGER.log(Level.INFO, "Attempting to delete book with ID: " + id);
        Book deleteBook = bookRepository.findById(id);
        if (deleteBook == null) {
            LOGGER.log(Level.WARNING, "Book with ID " + id + " not found.");
            throw new WebApplicationException("Book not found!", Response.Status.NOT_FOUND);
        }
        // Check if there are any associated genres with this book
        List<Genrel> genres = genrelRepository.find("book.id", id).list();
        if (!genres.isEmpty()) {
            LOGGER.log(Level.WARNING, "Cannot delete book with ID " + id + " because it still has associated genres.");
            throw new WebApplicationException("Cannot delete book because it still has associated genres!", Response.Status.BAD_REQUEST);
        }
        bookRepository.deleteById(id);
        LOGGER.log(Level.INFO, "Book deleted successfully.");
        return Response.noContent().build();
    }
}