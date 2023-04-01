package com.example.springboot.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b_id")
    @Access(AccessType.PROPERTY)
    protected Long id;

    @Column(name = "b_name")
    @Access(AccessType.PROPERTY)
    @NotEmpty
    protected String name;

    @JsonIgnoreProperties("books")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "g_id")
    @Access(AccessType.PROPERTY)
    @NotNull
    @Valid
    protected Genre genre;

    @JsonIgnoreProperties("books")
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @Access(AccessType.PROPERTY)
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "a_id"),
            inverseJoinColumns = @JoinColumn(name = "b_id")
    )
    @NotNull
    @Valid
    protected Set<Author> authors = new HashSet<>();
}
