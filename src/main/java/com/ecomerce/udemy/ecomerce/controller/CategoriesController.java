package com.ecomerce.udemy.ecomerce.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/category")
public class CategoriesController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping(value = "/")
    public String getMethodName() {
        return "redirect:/category/all";
    }

    @GetMapping("/{slug}")
    public String category(@PathVariable String slug, Model model,
            @RequestParam(value = "page", required = false) Integer p) {

        int perPage = 6;
        int page = (p != null) ? p : 1;
        Pageable pageable = PageRequest.of(page - 1, perPage);
        long count = 0;

        if (slug.equals("all")) {
            Page<Product> products = productRepository.findAll(pageable);
            count = productRepository.count();
            model.addAttribute("products", products);
            model.addAttribute("categoryName", "All Products");
        } else {
            Category category = categoryRepository.findBySlug(slug);
            if (category == null) {
                return "redirect:/";
            }
            int categoryId = category.getId();
            String categoryName = category.getName();
            List<Product> products = productRepository.findAllByCategoryId(categoryId, pageable);

            count = productRepository.countAllByCategory_Id(categoryId);

            model.addAttribute("products", products);
            model.addAttribute("categoryName", categoryName);
        }

        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int) pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "products";
    }
}
