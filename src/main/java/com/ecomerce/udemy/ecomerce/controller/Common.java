package com.ecomerce.udemy.ecomerce.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.ecomerce.udemy.ecomerce.models.CartItem;
import com.ecomerce.udemy.ecomerce.models.data.Category;
import com.ecomerce.udemy.ecomerce.models.data.Page;
import com.ecomerce.udemy.ecomerce.models.repositories.CategoryRepository;
import com.ecomerce.udemy.ecomerce.models.repositories.PageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@SuppressWarnings("unchecked")
public class Common {

    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @ModelAttribute
    public void sharedDate(Model model, HttpSession session, Principal principal) {
        if (principal != null) {
            model.addAttribute("principal", principal.getName());
            // model.addAttribute("principalType", principal.getClass().getSimpleName());
        }
        List<Page> pages = pageRepository.findAllByOrderBySortingAsc();

        List<Category> categories = categoryRepository.findAll();

        boolean cartActive = false;
        if (session.getAttribute("cart") != null) {
            HashMap<Integer, CartItem> cart = (HashMap<Integer, CartItem>) session.getAttribute("cart");

            CartItem sumCartItem = cart.values().stream().reduce(new CartItem(0, "", 0, 0, ""), (c1, c2) -> {
                return new CartItem(0, "", c1.getPrice() + (c2.getPrice() * c2.getQuantity()),
                        c1.getQuantity() + c2.getQuantity(), "");
            });
            model.addAttribute("csize", sumCartItem.getQuantity());
            model.addAttribute("ctotal", sumCartItem.getPrice());
            cartActive = true;
        }

        model.addAttribute("sharedPages", pages);
        model.addAttribute("ccategories", categories);
        model.addAttribute("cartActive", cartActive);
    }
}
