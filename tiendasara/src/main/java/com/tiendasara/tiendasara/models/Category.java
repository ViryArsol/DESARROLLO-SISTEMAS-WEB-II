package com.tiendasara.tiendasara.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Categoria") //nombre de la tabla en SQL
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDcategoria") //nombre de la llave primaria en SQL
    private Long id;
    
    @Column(name = "DescripcionCategoria") //Nombre de la columna descripcion en SQL
    private String name;

    //Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id;}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}