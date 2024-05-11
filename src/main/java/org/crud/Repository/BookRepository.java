package org.crud.Repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.crud.Model.Book;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {
}
