package com.ecomerce.udemy.ecomerce.controller;

import com.ecomerce.udemy.ecomerce.models.data.Page;
import com.ecomerce.udemy.ecomerce.models.repositories.PageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequestMapping("/")
public class PagesController {
    @Autowired
    private PageRepository pageRepository;

    @GetMapping("/")
    public String home(Model model) {
        Page page = pageRepository.findBySlug("home");
        model.addAttribute("page", page);
        return "page";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/{slug}")
    public String page(Model model, @PathVariable String slug) {
        Page page = pageRepository.findBySlug(slug);
        if (page == null) {
            return "redirect:/";
        }
        model.addAttribute("page", page);
        return "page";
    }

}
