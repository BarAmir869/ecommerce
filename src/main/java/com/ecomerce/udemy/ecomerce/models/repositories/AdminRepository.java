package com.ecomerce.udemy.ecomerce.models.repositories;

import com.ecomerce.udemy.ecomerce.models.data.users.Admin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByUsername(String username);

}
