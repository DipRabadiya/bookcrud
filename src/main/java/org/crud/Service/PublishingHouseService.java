package org.crud.Service;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.crud.Model.Book;
import org.crud.Model.PublishingHouse;
import org.crud.Repository.BookRepository;
import org.crud.Repository.PublishingHouseRepository;
import org.crud.pages.PageRequest;

import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;

@ApplicationScoped
public class PublishingHouseService {

    @Inject
    PublishingHouseRepository publishingHouseRepository;

    @Inject
    BookRepository bookRepository;

    private static final Logger LOGGER = Logger.getLogger(PublishingHouseService.class.getName());

    public Long count() {
        LOGGER.log(Level.INFO, "Attempting to count publishing houses...");
        Long count = publishingHouseRepository.count();
        if (count == 0) {
            LOGGER.log(Level.WARNING, "No publishing houses found.");
            throw new WebApplicationException("PublishHouse not found!", Response.Status.NOT_FOUND);
        }
        LOGGER.log(Level.INFO, "Number of publishing houses found: " + count);
        return count;
    }

    public Response getAllPaged(PageRequest pageRequest) {
        LOGGER.log(Level.INFO, "Attempting to retrieve paged list of publishing houses...");
        Long publishingHouse = publishingHouseRepository.findAll().count();
        if (publishingHouse == 0) {
            LOGGER.log(Level.WARNING, "No publishing houses found for the given page request.");
            throw new WebApplicationException("PublishHouse not found!", Response.Status.NOT_FOUND);
        }
        LOGGER.log(Level.INFO, "Publishing houses retrieved successfully.");
        return Response
                .ok(publishingHouseRepository.findAll().page(Page.of(pageRequest.getPageNum(), pageRequest.getPageSize())).list())
                .build();

    }

    //    public Response delete(Long id) {
//        LOGGER.log(Level.INFO, "Attempting to delete publishing house with ID: " + id);
//        PublishingHouse publishingHouse = publishingHouseRepository.findById(id);
//        if (publishingHouse == null) {
//            LOGGER.log(Level.WARNING, "Publishing house with ID " + id + " not found.");
//            throw new WebApplicationException("PublishingHouse not found!", Response.Status.NOT_FOUND);
//        }
//        publishingHouseRepository.deleteById(id);
//        LOGGER.log(Level.INFO, "Publishing house deleted successfully.");
//        return Response.noContent().build();
//    }
    public Response delete(Long id) {
        LOGGER.log(Level.INFO, "Attempting to delete publishing house with ID: " + id);


        // Check if there are any books associated with this publishing house
        List<Book> books = bookRepository.find("publishingHouse.id", id).list();
        if (!books.isEmpty()) {
            LOGGER.log(Level.WARNING, "Cannot delete publishing house with ID " + id + " because it still has associated books.");
            throw new WebApplicationException("Cannot delete publishing house because it still has associated books!", Response.Status.BAD_REQUEST);
        }

        PublishingHouse publishingHouse = publishingHouseRepository.findById(id);
        if (publishingHouse == null) {
            LOGGER.log(Level.WARNING, "Publishing house with ID " + id + " not found.");
            throw new WebApplicationException("Publishing house not found!", Response.Status.NOT_FOUND);
        }

        publishingHouseRepository.deleteById(id);
        LOGGER.log(Level.INFO, "Publishing house deleted successfully.");
        return Response.noContent().build();
    }


    public Response update(Long id, PublishingHouse publishingHouse) {
        LOGGER.log(Level.INFO, "Attempting to update publishing house with ID: " + id);
        PublishingHouse updatePublishingHouse = publishingHouseRepository.findById(id);
        if (updatePublishingHouse == null) {
            LOGGER.log(Level.WARNING, "Publishing house with ID " + id + " not found.");
            throw new WebApplicationException("Publishing house not found!", Response.Status.NOT_FOUND);
        }
        updatePublishingHouse.setName(publishingHouse.getName());
        updatePublishingHouse.setDescription(publishingHouse.getDescription());
        updatePublishingHouse.setFoundingYear(publishingHouse.getFoundingYear());
        LOGGER.log(Level.INFO, "Publishing house updated successfully.");
        return Response.ok(updatePublishingHouse).build();
    }

    public Response addPublish(PublishingHouse publishingHouse) {
        LOGGER.log(Level.INFO, "Attempting to add publishing house...");
        publishingHouseRepository.persist(publishingHouse);
        LOGGER.log(Level.INFO, "Publishing house added successfully.");
        return Response.ok(publishingHouse).build();
    }
}
