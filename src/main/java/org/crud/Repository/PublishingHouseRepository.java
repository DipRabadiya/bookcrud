package org.crud.Repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.crud.Model.PublishingHouse;

@ApplicationScoped
public class PublishingHouseRepository implements PanacheRepository<PublishingHouse> {
}
