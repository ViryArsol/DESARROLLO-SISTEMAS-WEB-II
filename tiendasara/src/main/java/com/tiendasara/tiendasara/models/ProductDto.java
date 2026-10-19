package com.tiendasara.tiendasara.models;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class ProductDto {
    @NotEmpty(message = "La descripción es requerida")
    private String description;

    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private double price;

    @Min(value = 0, message = "La cantidad debe ser mayor o igual a 0")
    private int amount;

    @NotEmpty(message = "Seleccione una categoría")
    private String category;

    @NotEmpty(message = "Seleccione una marca")
    private String mark;

    private MultipartFile imageFile; // Para subir la imagen del producto

    // Getters y Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getMark() { return mark; }
    public void setMark(String mark) { this.mark = mark; }
    public MultipartFile getImageFile() { return imageFile; }
    public void setImageFile(MultipartFile imageFile) { this.imageFile = imageFile; }
}
