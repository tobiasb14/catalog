package com.project.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.catalog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	
}
