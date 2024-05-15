package org.crud;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.crud.Model.Book;
import org.crud.Model.PublishingHouse;
import org.crud.Repository.BookRepository;
import org.crud.Repository.PublishingHouseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class PublishingHouseServiceTest {

    @InjectMock
    PublishingHouseRepository publishingHouseRepository;

    @Inject
    PublishingHouseService publishingHouseService;

    @InjectMock
    BookRepository bookRepository;


    @Test
    void getAllPublishingHouses() {
        List<PublishingHouse> publishingHouses = new ArrayList<>();
        publishingHouses.add(new PublishingHouse(1L,"House1","New One",2001));
        publishingHouses.add(new PublishingHouse(2L,"House2","New Two",2002));
        publishingHouses.add(new PublishingHouse(3L,"House3","New Three",2003));

        Mockito.when(publishingHouseRepository.listAll()).thenReturn(publishingHouses);

        Response response = publishingHouseService.getAllPublishingHouses();

        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

        List<PublishingHouse> publishingHouses1= (List<PublishingHouse>) response.getEntity();

        assertEquals(publishingHouses1.size(),3);
    }

    @Test
    void update(){
        PublishingHouse publishingHouse = new PublishingHouse(1L,"House1","New One",2000);

        Mockito.when(publishingHouseRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(publishingHouse);
        PublishingHouse newUpdatePublishinghouse = new PublishingHouse();
        newUpdatePublishinghouse.setName("House2");
        newUpdatePublishinghouse.setDescription("New Two");
        newUpdatePublishinghouse.setFoundingYear(2001);
        Response response = publishingHouseService.update(1L,newUpdatePublishinghouse);

        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());

        PublishingHouse ph = (PublishingHouse) response.getEntity();

        assertEquals(ph.getName(),"House2");
        assertEquals(ph.getDescription(),"New Two");
        assertEquals(ph.getFoundingYear(),2001);
    }

    @Test
    void addPublish(){
        PublishingHouse publishingHouse = new PublishingHouse(1L,"House1","New One",2000);

        Mockito.doNothing().when(publishingHouseRepository).persist(publishingHouse);

        Response response=publishingHouseService.addPublish(publishingHouse);

        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());
    }
}