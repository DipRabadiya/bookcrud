package org.crud.Model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Transactional
@Table(name="genres")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genrel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public String description;

    @OneToOne
    public Book book;
}
