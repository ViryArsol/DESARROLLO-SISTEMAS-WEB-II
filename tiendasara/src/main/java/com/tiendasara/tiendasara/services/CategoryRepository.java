package com.tiendasara.tiendasara.services;

import com.tiendasara.tiendasara.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}