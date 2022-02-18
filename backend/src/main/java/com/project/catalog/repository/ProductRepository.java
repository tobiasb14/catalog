package com.project.catalog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.catalog.entity.Category;
import com.project.catalog.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT DISTINCT p FROM Product p INNER JOIN p.categories cats "
			+ "WHERE (COALESCE(:categories) IS NULL OR cats IN :categories) "
			+ "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
	Page<Product> findAllPagedWithCategoryAndName(Pageable pageable, String name, List<Category> categories);
}
