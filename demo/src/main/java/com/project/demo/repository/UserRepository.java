package com.project.demo.repository;

import com.project.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findById(Integer id);
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);

//    Page<User> findByNameContainingIgnoreCase(String search, PageRequest of);
}
