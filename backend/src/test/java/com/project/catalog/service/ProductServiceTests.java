package com.project.catalog.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import com.project.catalog.dto.ProductDTO;
import com.project.catalog.entity.Category;
import com.project.catalog.entity.Product;
import com.project.catalog.repository.CategoryRepository;
import com.project.catalog.repository.ProductRepository;

@ExtendWith(SpringExtension.class)
class ProductServiceTests {

	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long correctId;
	private long wrongId;
	private Category category;
	private Product product;
	private Product editedProduct;
	private PageImpl<Product> page;
	
	
	@BeforeEach
	void setUp() throws Exception {
		correctId = 1L;
		wrongId = 100L;
		product = new Product(correctId, "product1", "x", new BigDecimal(10), "xx", Instant.MIN, new HashSet<>());
		category = new Category(correctId, "categoria", Instant.MAX, Instant.MIN, Set.of(product));
		editedProduct = new Product(correctId, "product2", "x", new BigDecimal(10), "xx", Instant.MIN, Set.of(category));
		page = new PageImpl<>(List.of(product));
		
		when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
		when(productRepository.findById(correctId)).thenReturn(Optional.of(product));
		when(productRepository.findById(wrongId)).thenReturn(Optional.empty());
		when(productRepository.getById(correctId)).thenReturn(product);
		when(productRepository.save(product)).thenReturn(product);
		when(categoryRepository.getById(correctId)).thenReturn(category);
		doNothing().when(productRepository).deleteById(correctId);
		doThrow(EntityNotFoundException.class).when(productRepository).getById(wrongId);
		doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(wrongId);
	}
	
	@Test
	void findAllPagedShouldReturnPage() {
		Page<ProductDTO> result = productService.findAllPaged(PageRequest.of(0, 3));
		
		assertNotNull(result);
		assertEquals(1, result.getSize());
		
		verify(productRepository).findAll(PageRequest.of(0, 3));
	}
	
	@Test
	void findByIdReturnProductDtoWhenIdExists() {
		ProductDTO result = productService.findById(correctId);
		
		assertEquals(product.getId(), result.getId());
		assertEquals(product.getName(), result.getName());
		assertEquals(product.getDescription(), result.getDescription());
		assertEquals(product.getPrice(), result.getPrice());
		assertEquals(product.getDate(), result.getDate());
		assertEquals(product.getImgUrl(), result.getImgUrl());
		assertTrue(result.getCategories().isEmpty());
		
		verify(productRepository).findById(correctId);
	}
	
	@Test
	void findByIdThrowsExceptionWhenIdDoesntExists() {
		assertThrows(ResponseStatusException.class, () -> productService.findById(wrongId));
		
		verify(productRepository).findById(wrongId);
	}
	
	@Test
	void updateReturnsProductDtoWhenIdExists() {
		ProductDTO edit = new ProductDTO(editedProduct, editedProduct.getCategories());
		ProductDTO result = productService.update(correctId, edit);
		
		assertEquals(editedProduct.getId(), result.getId());
		assertEquals(editedProduct.getName(), result.getName());
		assertEquals(editedProduct.getDescription(), result.getDescription());
		assertEquals(editedProduct.getPrice(), result.getPrice());
		assertEquals(editedProduct.getImgUrl(), result.getImgUrl());
		assertEquals(editedProduct.getDate(), result.getDate());
		assertTrue(result.getCategories().get(0).getName().equals(category.getName()));
		
		verify(productRepository).getById(correctId);
		verify(categoryRepository).getById(correctId);
		verify(productRepository).save(any(Product.class));
	}
	
	@Test
	void updateThrowsExceptionWhenIdDoesntExist() {
		assertThrows(ResponseStatusException.class, () -> productService.update(wrongId, new ProductDTO()));
		
		verify(productRepository).getById(wrongId);
	}
	
	@Test
	void deleteShouldDoNothingWhenIdExists() {
		assertDoesNotThrow(() -> productService.delete(correctId));
		
		verify(productRepository).deleteById(correctId);
	}
	
	@Test
	void deleteShouldThrowsExceptionWhenIdDoesntExists() {
		assertThrows(ResponseStatusException.class, () -> productService.delete(wrongId));
		
		verify(productRepository).deleteById(wrongId);
	}

}
