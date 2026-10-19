package com.tiendasara.tiendasara.services;

import com.tiendasara.tiendasara.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
