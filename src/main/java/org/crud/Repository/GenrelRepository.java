package org.crud.Repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.crud.Model.Genrel;

@ApplicationScoped
public class GenrelRepository implements PanacheRepository<Genrel> {
}
