package com.ecomerce.udemy.ecomerce.models.repositories;

import com.ecomerce.udemy.ecomerce.models.data.users.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

}
