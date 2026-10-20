package com.tiendasara.tiendasara.controllers;

import com.tiendasara.tiendasara.models.Category;
import com.tiendasara.tiendasara.models.Mark;
import com.tiendasara.tiendasara.models.Product;
import com.tiendasara.tiendasara.models.ProductDto;
import com.tiendasara.tiendasara.services.CategoryRepository;
import com.tiendasara.tiendasara.services.MarkRepository;
import com.tiendasara.tiendasara.services.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private MarkRepository markRepo;

    // 1. HTTP GET: Listar todos los productos
    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Product> products = productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    // 2. HTTP GET: Mostrar formulario de alta
    @GetMapping("/create")
    public String showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("marks", markRepo.findAll());
        return "products/CreateProduct";
    }

    // 3. HTTP POST: Procesar y guardar el nuevo producto
    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result,
            Model model
    ) {
        if (productDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "imageFile", "El archivo de imagen es requerido"));
        }

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("marks", markRepo.findAll());
            return "products/CreateProduct";
        }

        // Guardar la imagen localmente
        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Excepción: " + ex.getMessage());
        }

        Category category = categoryRepo.findById(Long.parseLong(productDto.getCategory()))
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Mark mark = markRepo.findById(Long.parseLong(productDto.getMark()))
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        Product product = new Product();
        product.setDescription(productDto.getDescription());
        product.setAmount(productDto.getAmount());
        product.setCategory(category);
        product.setMark(mark);
        product.setImageFileName(storageFileName);

        productRepo.save(product);

        return "redirect:/products";
    }

    // 4. HTTP GET: Mostrar formulario de edición
    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            Product product = productRepo.findById(id).get();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setDescription(product.getDescription());
            productDto.setAmount(product.getAmount());
            productDto.setCategory(product.getCategory() != null ? product.getCategory().getId().toString() : null);
            productDto.setMark(product.getMark() != null ? product.getMark().getId().toString() : null);

            model.addAttribute("productDto", productDto);
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("marks", markRepo.findAll());
        } catch (Exception ex) {
            System.out.println("Excepción: " + ex.getMessage());
            return "redirect:/products";
        }

        return "products/EditProduct";
    }

    // 5. HTTP POST: Procesar actualización de producto
    @PostMapping("/edit")
    public String updateProduct(
            Model model,
            @RequestParam Long id,
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ) {
        try {
            Product product = productRepo.findById(id).get();
            model.addAttribute("product", product);

            if (result.hasErrors()) {
                model.addAttribute("categories", categoryRepo.findAll());
                model.addAttribute("marks", markRepo.findAll());
                return "products/EditProduct";
            }

            if (!productDto.getImageFile().isEmpty()) {
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
                try {
                    Files.delete(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Excepción: " + ex.getMessage());
                }

                MultipartFile image = productDto.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                }
                product.setImageFileName(storageFileName);
            }

            Category category = categoryRepo.findById(Long.parseLong(productDto.getCategory()))
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            Mark mark = markRepo.findById(Long.parseLong(productDto.getMark()))
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

            product.setDescription(productDto.getDescription());
            product.setAmount(productDto.getAmount());
            product.setCategory(category);
            product.setMark(mark);

            productRepo.save(product);

        } catch (Exception ex) {
            System.out.println("Excepción: " + ex.getMessage());
        }

        return "redirect:/products";
    }

    // 6. HTTP GET: Eliminar producto
    @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id) {
        try {
            Product product = productRepo.findById(id).get();
            Path imagePath = Paths.get("public/images/" + product.getImageFileName());
            try {
                Files.delete(imagePath);
            } catch (Exception ex) {
                System.out.println("Excepción: " + ex.getMessage());
            }

            productRepo.delete(product);
        } catch (Exception ex) {
            System.out.println("Excepción: " + ex.getMessage());
        }

        return "redirect:/products";
    }
}