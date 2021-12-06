package com.ecomerce.udemy.ecomerce.controller.admin;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import com.ecomerce.udemy.ecomerce.models.data.Category;
import com.ecomerce.udemy.ecomerce.models.data.Product;
import com.ecomerce.udemy.ecomerce.models.repositories.CategoryRepository;
import com.ecomerce.udemy.ecomerce.models.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository CategoryRepository;

    @GetMapping
    public String index(Model model, @RequestParam(value = "page", required = false) Integer p) {

        int perPage = 6;
        int page = (p != null) ? p : 1;
        Pageable pageable = PageRequest.of(page - 1, perPage);

        Page<Product> products = productRepo.findAll(pageable);
        List<Category> categories = CategoryRepository.findAll();

        model.addAttribute("products", products);
        model.addAttribute("cats", categories);

        long count = productRepo.count();
        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int) pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "admin/products/index";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "page", required = false) Integer p) {

        int perPage = 6;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Product> products = productRepo.findAll(pageable);
        List<Category> categories = CategoryRepository.findAll();

        model.addAttribute("products", products);
        model.addAttribute("cats", categories);

        long count = productRepo.count();
        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int) pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "admin/products/index";
    }

    @GetMapping("/add")
    public String add(Product product, Model model) {

        List<Category> categories = CategoryRepository.findAll();
        model.addAttribute("categories", categories);

        return "admin/products/add";
    }

    @PostMapping("/add")
    public String add(@Valid Product product, BindingResult bindingResult, MultipartFile file,
            RedirectAttributes redirectAttributes, Model model, @RequestParam("category") Integer category)
            throws IOException {

        // *****Errors check****

        byte[] bytes = file.getBytes();
        String fileName = file.getOriginalFilename().replace(" ", "-");
        Path path = Paths.get("src/main/resources/static/media/" + fileName);

        if (!(fileName.endsWith("jpg") || fileName.endsWith("png"))) {
            bindingResult.addError(new FieldError("product", "image", "Image must be a jpg or a png"));
        }
        if (category == 0) {
            bindingResult.addError(new FieldError("product", "category", "You must choose a category"));
        }
        String slug = product.getName().toLowerCase().replace(" ", "-");

        Product productExists = productRepo.findBySlug(slug);
        if (productExists != null) {
            bindingResult.reject("error.exist", "*Product already exists");
            bindingResult.addError(new FieldError("product", "name", "Product already exists"));
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
            model.addAttribute("categories", CategoryRepository.findAll());

            return "admin/products/add";
        }
        // *****No Errors****
        redirectAttributes.addFlashAttribute("message", "Product added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        product.setSlug(slug);
        product.setImage(fileName);
        product.setCategory(CategoryRepository.getById(category));
        productRepo.save(product);

        Files.write(path, bytes);

        return "redirect:/admin/products/add";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {

        Product product = productRepo.getById(id);
        List<Category> categories = CategoryRepository.findAll();

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "admin/products/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Product product, BindingResult bindingResult, MultipartFile file,
            RedirectAttributes redirectAttributes, Model model, @RequestParam("category") Integer category)
            throws IOException {

        Product oldProduct = productRepo.getById(product.getId());
        // *****Errors check****
        byte[] bytes = file.getBytes();
        String fileName = file.getOriginalFilename().replace(" ", "-");
        Path path = Paths.get("src/main/resources/static/media/" + fileName);
        ;
        if (!file.isEmpty()) {
            if (!(fileName.endsWith("jpg") || fileName.endsWith("png"))) {
                bindingResult.addError(new FieldError("product", "image", "Image must be a jpg or a png"));
            }
        }
        if (category == 0) {
            bindingResult.addError(new FieldError("product", "category", "You must choose a category"));
        }
        String slug = product.getName().toLowerCase().replace(" ", "-");

        if (productRepo.existsBySlugAndIdNot(slug, product.getId())) {
            // bindingResult.reject("error.exist", "*Product already exists");
            bindingResult.addError(new FieldError("product", "name", "Product already exists"));
        }
        if (bindingResult.hasErrors()) {
            product.setImage(oldProduct.getImage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
            model.addAttribute("productName", product.getName());
            model.addAttribute("categories", CategoryRepository.findAll());

            return "admin/products/edit";
        }
        // *****No Errors****
        redirectAttributes.addFlashAttribute("message", "Product edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        product.setSlug(slug);
        if (!file.isEmpty()) {
            Path oldPath = Paths.get("src/main/resources/static/media/" + oldProduct.getImage());
            if (!productRepo.existsByImageAndIdNot(oldProduct.getImage(), product.getId())) {
                Files.delete(oldPath);
            }
            product.setImage(fileName);
            if (!productRepo.existsByImage(fileName))
                Files.write(path, bytes);
        } else {
            product.setImage(oldProduct.getImage());
        }
        product.setCategory(CategoryRepository.getById(category));
        productRepo.save(product);

        return "redirect:/admin/products/edit/" + product.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) throws IOException {

        Product product = productRepo.getById(id);
        Product oldProduct = productRepo.getById(product.getId());

        Path path2 = Paths.get("src/main/resources/static/media/" + oldProduct.getImage());
        Files.delete(path2);
        productRepo.deleteById(id);

        redirectAttributes.addFlashAttribute("message", "Product deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/products";

    }

}