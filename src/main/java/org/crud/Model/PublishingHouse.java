package org.crud.Model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Transactional
@Table(name="publishing_houses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishingHouse{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String description;
    private int foundingYear;
}