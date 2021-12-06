package com.ecomerce.udemy.ecomerce.models.repositories;

import java.util.List;

import com.ecomerce.udemy.ecomerce.models.data.Product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByOrderByCreatedAtAsc();

    // @Query("SELECT product p from products where p.name IN ?1 ")
    // List<Product>
    // findAllByNameContainsOrDescriptionContatinsOrCategery_NameContains(String[]
    // str);

    Product findBySlug(String slug);

    Product findBySlugAndIdNot(String slug, int id);

    boolean existsByImage(String fileName);

    boolean existsByImageAndIdNot(String image, int id);

    boolean existsBySlugAndIdNot(String slug, int id);

    List<Product> findAllByCategoryId(int cateoryId, Pageable pageable);

    long countAllByCategory_Id(int categoryId);

}
