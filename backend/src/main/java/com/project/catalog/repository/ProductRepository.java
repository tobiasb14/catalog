package com.project.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.catalog.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
