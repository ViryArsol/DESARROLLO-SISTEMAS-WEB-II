package com.tiendasara.tiendasara.models;

import jakarta.validation.constraints.NotEmpty;

public class MarkDto {
    
    @NotEmpty(message = "La marca es requerida")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}
