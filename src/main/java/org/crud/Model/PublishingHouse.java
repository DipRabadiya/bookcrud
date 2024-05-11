package org.crud.Model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Transactional
@Table(name="publishing_houses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishingHouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int foundingYear;

    @OneToMany(mappedBy = "publishingHouse",cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

}
