package com.tiendasara.tiendasara.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Producto") // Nombre exacto de la tabla en SQL
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDproducto")
    private Long id;

    @Column(name = "DescripcionProducto")
    private String description;

    @Column(name = "Cantidad")
    private int amount;

    @ManyToOne
    @JoinColumn(name = "IDcategoria")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "IDmarca")
    private Mark mark;

    @Column(name = "Imagen")
    private String imageFileName;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Mark getMark() { return mark; }
    public void setMark(Mark mark) { this.mark = mark; }

    public String getImageFileName() { return imageFileName; }
    public void setImageFileName(String imageFileName) { this.imageFileName = imageFileName; }
}