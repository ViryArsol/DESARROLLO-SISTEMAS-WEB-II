package com.tiendasara.tiendasara.controllers;

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

    // 1. Lista Productos (Consulta)
    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Product> products = productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    // 2. Mostrar formulario de crear producto (ALTA - GET)
    @GetMapping("/create")
    public String showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("marks", markRepo.findAll());
        return "products/CreateProduct";
    }

    // 3. Procesar creación de producto (ALTA - POST)
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

        // Guardar la imagen en el servidor local
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

        // Mapear DTO a la entidad Product
        Product product = new Product();
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setAmount(productDto.getAmount());
        product.setCategory(productDto.getCategory());
        product.setMark(productDto.getMark());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);

        productRepo.save(product);

        return "redirect:/products";
    }

    // 4. Mostrar formulario de editar producto (MODIFICACIÓN - GET)
    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            Product product = productRepo.findById(id).get();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());
            productDto.setAmount(product.getAmount());
            productDto.setCategory(product.getCategory());
            productDto.setMark(product.getMark());

            model.addAttribute("productDto", productDto);
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("marks", markRepo.findAll());
        } catch (Exception ex) {
            System.out.println("Excepción: " + ex.getMessage());
            return "redirect:/products";
        }

        return "products/EditProduct";
    }

    // 5. Procesar actualizarción de producto (MODIFICACIÓN - POST)
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
                // Borrar imagen anterior
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
                try {
                    Files.delete(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Excepción: " + ex.getMessage());
                }

                // Subir nueva imagen
                MultipartFile image = productDto.getImageFile();
                Date createdAt = new Date();
              String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                }
                product.setImageFileName(storageFileName);
            }

            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setAmount(productDto.getAmount());
            product.setCategory(productDto.getCategory());
            product.setMark(productDto.getMark());

            productRepo.save(product);

        } catch (Exception ex) {
            System.out.println("Excepción: " + ex.getMessage());
        }

        return "redirect:/products";
    }

    // 6. Eliminar producto (BAJA)
    @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id) {
        try {
            Product product = productRepo.findById(id).get();

            // Borrar archivo de imagen del servidor
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
