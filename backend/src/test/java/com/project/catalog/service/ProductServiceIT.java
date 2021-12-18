package com.project.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.project.catalog.dto.CategoryDTO;
import com.project.catalog.dto.ProductDTO;
import com.project.catalog.entity.Category;
import com.project.catalog.repository.CategoryRepository;
import com.project.catalog.repository.ProductRepository;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private long correctId;
	private long wrongId;
	private long productsCount;
	private Category category;
	
	@BeforeEach
	void setUp() throws Exception {
		correctId = 1L;
		wrongId = 100L;
		productsCount = 25L;
		category = categoryRepository.getById(1L);
	}
	
	@Test
	void findAllPagedShouldReturnPage() {
		Page<ProductDTO> result = productService.findAllPaged(PageRequest.of(0, 10));
		
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(0, result.getNumber());
		assertEquals(10, result.getSize());
		assertEquals(productsCount, result.getTotalElements());
	}
	
	@Test
	void findAllPagedShouldReturnPageOrderBy() {
		Page<ProductDTO> result = productService.findAllPaged(PageRequest.of(0, 10, Sort.by("id")));
		
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(1L, result.getContent().get(0).getId());
		assertEquals(2L, result.getContent().get(1).getId());
		assertEquals(3L, result.getContent().get(2).getId());
	}
	
	@Test
	void findByIdReturnProductDtoWhenIdExists() {
		ProductDTO result = productService.findById(correctId);
		
		assertEquals(1L, result.getId());
		assertEquals("The Lord of the Rings", result.getName());
		assertEquals(new BigDecimal("90.50"), result.getPrice());
		assertEquals(1, result.getCategories().size());
	}
	
	@Test
	void findByIdThrowsExceptionWhenIdDoesntExists() {
		assertThrows(ResponseStatusException.class, () -> productService.findById(wrongId));
	}
	
	@Test
	void insertCreateProductWhenNewDtoIsCorrect() {
		ProductDTO insertProduct = new ProductDTO();
		ProductDTO result = productService.insert(insertProduct);
		
		assertEquals(26L, result.getId());
	}
	
	@Test
	void updateReturnsProductDtoWhenIdExists() {
		ProductDTO edit = new ProductDTO(correctId, "updated", "new product", new BigDecimal(10.0), 
				"vflkvmpr", Instant.parse("2020-07-13T20:50:07.12345Z"), List.of(new CategoryDTO(category)));
		ProductDTO result = productService.update(correctId, edit);
		
		assertEquals(edit.getId(), result.getId());
		assertEquals(edit.getName(), result.getName());
		assertEquals(edit.getDescription(), result.getDescription());
		assertEquals(edit.getPrice(), result.getPrice());
		assertEquals(edit.getImgUrl(), result.getImgUrl());
		assertEquals(edit.getDate(), result.getDate());
		assertEquals(2, result.getCategories().get(0).getId());
		assertEquals(1, result.getCategories().get(1).getId());
	}
	
	@Test
	void updateThrowsExceptionWhenIdDoesntExist() {
		assertThrows(ResponseStatusException.class, () -> productService.update(wrongId, new ProductDTO()));
	}
	
	@Test
	void deleteShouldDoNothingWhenIdExists() {
		productService.delete(correctId);
		
		assertEquals(productsCount - 1, productRepository.count());
	}
	
	@Test
	void deleteShouldThrowsExceptionWhenIdDoesntExists() {
		assertThrows(ResponseStatusException.class, () -> productService.delete(wrongId));
	}
}
