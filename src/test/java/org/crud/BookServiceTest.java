package org.crud;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.crud.Model.Book;
import org.crud.Model.PublishingHouse;
import org.crud.Repository.BookRepository;
import org.crud.Repository.GenrelRepository;
import org.crud.Repository.PublishingHouseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class BookServiceTest {
    @Inject
    BookService bookService;

    @InjectMock
    PublishingHouseRepository publishingHouseRepository;

    @InjectMock
    BookRepository bookRepository;
    @InjectMock
    GenrelRepository genrelRepository;

    @Test
    void getAllBooks() {
        PublishingHouse publishingHouse1 = new PublishingHouse(1L,"House1","New One",2001);
        PublishingHouse publishingHouse2 = new PublishingHouse(2L,"House2","New Two",2002);
        PublishingHouse publishingHouse3 = new PublishingHouse(3L,"House3","New Three",2003);
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"Book1","New One",2001,publishingHouse1));
        books.add(new Book(2L,"Book2","New Two",2002,publishingHouse2));
        books.add(new Book(3L,"Book3","New Three",2003,publishingHouse3));

        Mockito.when(bookRepository.listAll()).thenReturn(books);
        System.out.println(books);

        Response response = bookService.getAllBooks();

        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

        List<Book> books1= (List<Book>) response.getEntity();

        assertEquals(books1.size(),3);
    }

    @Test
    void addBook() {
        PublishingHouse publishingHouse = new PublishingHouse(1L,"House1","New One",2000);

        Book book = new Book(1L,"Book1","New One",2001,publishingHouse);
        Mockito.when(publishingHouseRepository.findById(1L)).thenReturn(publishingHouse);
        Mockito.doNothing().when(bookRepository).persist(book);

        Response response=bookService.addBook(book);

        assertNotNull(response);
        assertNotNull(response.getEntity());

    }

    @Test
    void update() {
        Book book = new Book(1L,"House1","New One",2000,new PublishingHouse(1L,"House1","New One",2001));
        PublishingHouse publishingHouse = new PublishingHouse(2L,"House2","New Two",2002);

        Mockito.when(publishingHouseRepository.findById(2L)).thenReturn(publishingHouse);

        Mockito.when(bookRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(book);
        Book newUpdateBook = new Book();
        newUpdateBook.setPublishingHouse(publishingHouse);
        newUpdateBook.setName("Book2");
        newUpdateBook.setDescription("New Two");
        newUpdateBook.setYearOfPublication(2001);
        Response response = bookService.update(1L,newUpdateBook);

        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

        Book book1 = (Book) response.getEntity();

        assertEquals(book1.getName(),"Book2");
        assertEquals(book1.getDescription(),"New Two");
        assertEquals(book1.getYearOfPublication(),2001);
        System.out.println(newUpdateBook);
    }
}