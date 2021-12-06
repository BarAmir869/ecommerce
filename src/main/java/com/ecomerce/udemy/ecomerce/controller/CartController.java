package com.ecomerce.udemy.ecomerce.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ecomerce.udemy.ecomerce.models.CartItem;
import com.ecomerce.udemy.ecomerce.models.data.Product;
import com.ecomerce.udemy.ecomerce.models.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
@SuppressWarnings("unchecked")
public class CartController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/add/{id}")
    public String add(@PathVariable int id, HttpSession session, Model model,
            @RequestParam(value = "cartPage", required = false) String cartPage) {
        Product product = productRepository.getById(id);

        if (session.getAttribute("cart") == null) { // if no products in cart
            HashMap<Integer, CartItem> cart = new HashMap<>();
            cart.put(id, new CartItem(id, product.getName(), product.getPrice(), 1, product.getImage()));
            session.setAttribute("cart", cart);
        } else { // if cart is not empty
            HashMap<Integer, CartItem> cart = (HashMap<Integer, CartItem>) session.getAttribute("cart");
            if (cart.containsKey(id)) { // if this products already in cart
                int quantity = cart.get(id).getQuantity();
                cart.put(id, new CartItem(id, product.getName(), product.getPrice(), ++quantity, product.getImage()));
            } else {
                cart.put(id, new CartItem(id, product.getName(), product.getPrice(), 1, product.getImage()));
                session.setAttribute("cart", cart);
            }
        }

        HashMap<Integer, CartItem> cart = (HashMap<Integer, CartItem>) session.getAttribute("cart");

        // CartItem to sum all products quantity and price
        CartItem sumCartItem = cart.values().stream().reduce(new CartItem(0, "", 0, 0, ""), (c1, c2) -> {
            return new CartItem(0, "", c1.getPrice() + (c2.getPrice() * c2.getQuantity()),
                    c1.getQuantity() + c2.getQuantity(), "");
        });

        model.addAttribute("size", sumCartItem.getQuantity());
        model.addAttribute("total", sumCartItem.getPrice());

        if (cartPage != null) {
            return "redirect:/cart/view";
        }
        return "cart_view";
    }

    @GetMapping("/subtract/{id}")
    public String subtract(@PathVariable int id, HttpSession session, Model model,
            HttpServletRequest httpServletRequest) {

        Product product = productRepository.getById(id);

        HashMap<Integer, CartItem> cart = (HashMap<Integer, CartItem>) session.getAttribute("cart");

        int quantity = cart.get(id).getQuantity();
        if (quantity == 1) {
            cart.remove(id);
            if (cart.size() == 0) {
                session.removeAttribute("cart");
            }
        } else {
            cart.put(id, new CartItem(id, product.getName(), product.getPrice(), --quantity, product.getImage()));
        }
        String refererLink = httpServletRequest.getHeader("referer");
        return "redirect:" + refererLink;
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable int id, HttpSession session, Model model,
            HttpServletRequest httpServletRequest) {

        Product product = productRepository.getById(id);

        HashMap<Integer, CartItem> cart = (HashMap<Integer, CartItem>) session.getAttribute("cart");

        cart.remove(id);
        if (cart.size() == 0) {
            session.removeAttribute("cart");
        }

        String refererLink = httpServletRequest.getHeader("referer");
        return "redirect:" + refererLink;
    }

    @GetMapping("/clear")
    public String clear(HttpSession session,
            HttpServletRequest httpServletRequest) {

        session.removeAttribute("cart");

        String refererLink = httpServletRequest.getHeader("referer");
        return "redirect:" + refererLink;
    }

    @RequestMapping("/view")
    public String view(HttpSession session, Model model) {
        if (session.getAttribute("cart") == null) {
            return "redirect:/";
        }
        HashMap<Integer, CartItem> cart = (HashMap<Integer, CartItem>) session.getAttribute("cart");
        model.addAttribute("cart", cart);
        model.addAttribute("notCartViewPage", true);

        return "cart";
    }

}
