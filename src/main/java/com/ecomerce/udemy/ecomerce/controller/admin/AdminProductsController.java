package com.ecomerce.udemy.ecomerce.controller.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.validation.Valid;

import com.ecomerce.udemy.ecomerce.models.data.Category;
import com.ecomerce.udemy.ecomerce.models.data.Product;
import com.ecomerce.udemy.ecomerce.models.repositories.CategoryRepository;
import com.ecomerce.udemy.ecomerce.models.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/test")
public class AdminProductsController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    private String index(Model model) {
        List<Product> products = productRepository.findAllByOrderByCreatedAtAsc();
        model.addAttribute("products", products);
        return "admin/products/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute Product product, Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/products/add";
    }

    @PostMapping("/add")
    public String add(@Valid Product product, @RequestParam("file") MultipartFile file, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, @RequestParam("category") Integer category, Model model)
            throws IOException {
        // *****Errors check****
        byte[] bytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + fileName);

        if (!(fileName.endsWith("jpg") || fileName.endsWith("png"))) {
            bindingResult.rejectValue("image", "error.image", "Image must be a jpg or a png!");
        }
        if (category == 0) {
            bindingResult.rejectValue("category", "error.category", "You must choose a category");
        }
        String slug = product.getName().toLowerCase().replace(" ", "-");

        Product productExists = productRepository.findBySlug(slug);
        if (productExists != null) {
            bindingResult.rejectValue("exist", "error.exist", "Product already exists");
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
            model.addAttribute("categories", categoryRepository.findAll());

            return "admin/products/add";
        }
        // *****No Errors****
        redirectAttributes.addFlashAttribute("message", "Product added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        product.setSlug(slug);
        product.setImage(fileName);
        product.setCategory(categoryRepository.getById(category));
        productRepository.save(product);

        Files.write(path, bytes);

        return "redirect:/admin/products/add";
    }
}
