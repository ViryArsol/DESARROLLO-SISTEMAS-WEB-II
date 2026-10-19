package com.tiendasara.tiendasara.models;

import jakarta.validation.constraints.NotEmpty;

public class CategoryDto {
    
    @NotEmpty(message = "El nombre es requerido")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
