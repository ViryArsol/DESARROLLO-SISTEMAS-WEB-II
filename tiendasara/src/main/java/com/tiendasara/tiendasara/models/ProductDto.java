package com.tiendasara.tiendasara.models;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;

public class ProductDto {

    @NotEmpty(message = "La descripción es obligatoria")
    private String description;

   
    @Min(0)
    private int amount;

    @NotEmpty(message = "La categoría es obligatoria")
    private String category;

    @NotEmpty(message = "La marca es obligatoria")
    private String mark;

    private MultipartFile imageFile;

    // Getters y Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
