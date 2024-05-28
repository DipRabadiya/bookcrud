package org.crud;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.crud.Model.Book;
import org.crud.Model.Genrel;
import org.crud.Model.PublishingHouse;
import org.crud.Repository.BookRepository;
import org.crud.Repository.GenrelRepository;
import org.crud.Repository.PublishingHouseRepository;
import org.crud.Service.GenrelService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class GenrelServiceTest {

    @Inject
    GenrelService genrelService;
    @InjectMock
    GenrelRepository genrelRepository;

    @InjectMock
    BookRepository bookRepository;
    @InjectMock
    PublishingHouseRepository publishingHouseRepository;

    @Test
    void getAllGenrels() {
        List<Genrel> genrels = new ArrayList<>();
        genrels.add(new Genrel(1L,"Genrel1","New One",
                new Book(1L,"Book1","New One",2001,
                        new PublishingHouse(1L,"House1","New One",2001))));
        genrels.add(new Genrel(2L,"Genrel2","New Two",
                new Book(2L,"Book2","New Two",2002,
                        new PublishingHouse(2L,"House2","New Three",2002))));
        genrels.add(new Genrel(3L,"Genrel1","New Three",
                new Book(3L,"Book3","New Three",2001,
                        new PublishingHouse(3L,"House3","New Three",2003))));
        Mockito.when(genrelRepository.listAll()).thenReturn(genrels);
//        System.out.println(genrels);

        Response response = genrelService.getAllGenrels();

        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

        List<Genrel> genrels1= (List<Genrel>) response.getEntity();

        assertEquals(genrels1.size(),3);

    }

    @Test
    void addGenrel() {
        PublishingHouse publishingHouse = new PublishingHouse(1L,"House1","New One",2000);
        Book book = new Book(1L,"Book1","New One",2001,publishingHouse);
        Genrel genrel = new Genrel(1L,"Genrel1","New One",book);
        Mockito.when(publishingHouseRepository.findById(1L)).thenReturn(publishingHouse);
        Mockito.when(bookRepository.findById(1L)).thenReturn(book);
        Mockito.doNothing().when(genrelRepository).persist(genrel);

        Response response=genrelService.addGenrel(genrel);

        assertNotNull(response);
        assertNotNull(response.getEntity());
    }

    @Test
    void update() {
        Book book = new Book(1L,"House1","New One",2000,new PublishingHouse(1L,"House1","New One",2001));
        Genrel genrel = new Genrel(1L,"Genrel1","New One",book);
        PublishingHouse publishingHouse = new PublishingHouse(2L,"House2","New Two",2002);
        Book book1 = new Book(2L,"Book2","New Two",2002,publishingHouse);

        Mockito.when(publishingHouseRepository.findById(2L)).thenReturn(publishingHouse);
        Mockito.when(bookRepository.findById(2L)).thenReturn(book1);

        Mockito.when(genrelRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(genrel);
        Genrel newUpdateGenrel = new Genrel();
        newUpdateGenrel.setBook(book1);
        newUpdateGenrel.setName("Genrel2");
        newUpdateGenrel.setDescription("New Two");
        Response response = genrelService.update(1L,newUpdateGenrel);

        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

        Genrel genrel1 = (Genrel) response.getEntity();

        assertEquals(genrel1.getName(),"Genrel2");
        assertEquals(genrel1.getDescription(),"New Two");
        System.out.println(newUpdateGenrel);
    }

    @Test
    void delete(){
        PublishingHouse publishingHouse = new PublishingHouse(1L,"House1","New One",2000);
        Book book = new Book(1L,"Book1","New One",2001,publishingHouse);
        Genrel genrel = new Genrel(1L,"Genrel1","New One",book);
        Mockito.when(genrelRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(genrel);

        Mockito.when(genrelRepository.deleteById(1L)).thenReturn(Boolean.TRUE);

        Response response = genrelService.delete(1L);
        assertEquals(Response.noContent().build().getStatus(),response.getStatus());

    }

    @Test
    void count(){
        List<Genrel> genrels = new ArrayList<>();
        genrels.add(new Genrel(1L,"Genrel1","New One",
                new Book(1L,"Book1","New One",2001,
                        new PublishingHouse(1L,"House1","New One",2001))));
        genrels.add(new Genrel(2L,"Genrel2","New Two",
                new Book(2L,"Book2","New Two",2002,
                        new PublishingHouse(2L,"House2","New Three",2002))));
        genrels.add(new Genrel(3L,"Genrel1","New Three",
                new Book(3L,"Book3","New Three",2001,
                        new PublishingHouse(3L,"House3","New Three",2003))));
        Mockito.when(genrelRepository.count()).thenReturn((long) genrels.size());

        long response = genrelService.count();

        assertNotNull(response);
        assertEquals(3L,response);
    }
}