package com.ecomerce.udemy.ecomerce.models.repositories;

import java.util.List;

import com.ecomerce.udemy.ecomerce.models.data.Page;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Integer> {
    Page findBySlug(String slug);

    Page findBySlugAndIdNot(String slug, int id);

    List<Page> findAllByOrderBySortingAsc();
}
