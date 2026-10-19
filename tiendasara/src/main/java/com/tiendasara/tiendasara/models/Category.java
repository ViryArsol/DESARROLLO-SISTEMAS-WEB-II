package com.tiendasara.tiendasara.models;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDcategoria", columnDefinition = "INT")
    private Long id;

    @Column(name = "DescripcionCategoria", columnDefinition = "VARCHAR")
    private String name;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}