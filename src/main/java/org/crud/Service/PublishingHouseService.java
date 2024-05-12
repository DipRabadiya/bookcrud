package org.crud.Service;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.crud.Model.PublishingHouse;
import org.crud.Repository.PublishingHouseRepository;
import org.crud.pages.PageRequest;

import java.net.URI;

@ApplicationScoped
public class PublishingHouseService {

    @Inject
    PublishingHouseRepository publishingHouseRepository;

    public Long count() {
        if(publishingHouseRepository.count() == 0)
            throw new WebApplicationException("PublishHouse not found!", Response.Status.NOT_FOUND);

        return publishingHouseRepository.count();
    }

    public Response getAllPaged(PageRequest pageRequest) {
        if(publishingHouseRepository.findAll().count() == 0)
            throw new WebApplicationException("PublishHouse not found!", Response.Status.NOT_FOUND);

        return Response
                .ok(publishingHouseRepository.findAll().page(Page.of(pageRequest.getPageNum(), pageRequest.getPageSize())).list())
                .build();

    }

    public Response delete(Long id) {
        if (publishingHouseRepository.findById(id) == null)
            throw new WebApplicationException("PublishingHouse not found!", Response.Status.NOT_FOUND);

        publishingHouseRepository.deleteById(id);
        return Response.noContent().build();
    }

    public Response update(Long id, PublishingHouse publishingHouse) {
        PublishingHouse updatePublishingHouse = publishingHouseRepository.findById(id);

        if (updatePublishingHouse == null)
            throw new WebApplicationException("publishinghouse not found!", Response.Status.NOT_FOUND);

        updatePublishingHouse.setName(publishingHouse.getName());
        updatePublishingHouse.setDescription(publishingHouse.getDescription());
        updatePublishingHouse.setFoundingYear(publishingHouse.getFoundingYear());
        return Response.ok(updatePublishingHouse).build();
    }

    public Response addPublish(PublishingHouse publishingHouse) {
        publishingHouseRepository.persist(publishingHouse);

        return Response.ok(publishingHouse).build();
    }
}
