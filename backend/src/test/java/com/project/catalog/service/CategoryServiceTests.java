package com.project.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import com.project.catalog.dto.CategoryDTO;
import com.project.catalog.entity.Category;
import com.project.catalog.repository.CategoryRepository;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

	@InjectMocks
	private CategoryService categoryService;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long correctId;
	private long wrongId;
	private Category category;
	private Category editedCategory;
	private PageImpl<Category> page;
	
	@BeforeEach
	void setUp() throws Exception {
		correctId = 1L;
		wrongId = 100L;
		category = new Category(correctId, "categoria", Instant.MAX, Instant.MIN, new HashSet<>());
		editedCategory = new Category(correctId, "updated", Instant.MAX, Instant.MIN, new HashSet<>());
		page = new PageImpl<>(List.of(category));
		
		when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);
		when(categoryRepository.findById(correctId)).thenReturn(Optional.of(category));
		when(categoryRepository.findById(wrongId)).thenReturn(Optional.empty());
		when(categoryRepository.getById(correctId)).thenReturn(category);
		when(categoryRepository.save(category)).thenReturn(category);
		doNothing().when(categoryRepository).deleteById(correctId);
		doThrow(EntityNotFoundException.class).when(categoryRepository).getById(wrongId);
		doThrow(EmptyResultDataAccessException.class).when(categoryRepository).deleteById(wrongId);
		doThrow(DataIntegrityViolationException.class).when(categoryRepository).deleteById(correctId);
	}
	
	@Test
	void findAllPagedShouldReturnPage() {
		Page<CategoryDTO> result = categoryService.findAllPaged(PageRequest.of(0, 3));
		
		assertNotNull(result);
		assertEquals(1, result.getSize());
		
		verify(categoryRepository).findAll(PageRequest.of(0, 3));
	}
	
	@Test
	void findByIdReturnCategoryDtoWhenIdExists() {
		CategoryDTO result = categoryService.findById(correctId);
		
		assertEquals(category.getId(), result.getId());
		assertEquals(category.getName(), result.getName());
		
		verify(categoryRepository).findById(correctId);
	}
	
	@Test
	void findByIdThrowsExceptionWhenIdDoesntExists() {
		assertThrows(ResponseStatusException.class, () -> categoryService.findById(wrongId));
		
		verify(categoryRepository).findById(wrongId);
	}
	
	@Test
	void updateReturnsCategoryDtoWhenIdExists() {
		CategoryDTO edit = new CategoryDTO(editedCategory);
		CategoryDTO result = categoryService.update(correctId, edit);
		
		assertEquals(editedCategory.getId(), result.getId());
		assertEquals(editedCategory.getName(), result.getName());
		
		verify(categoryRepository).getById(correctId);
		verify(categoryRepository).save(any(Category.class));
	}
	
	@Test
	void updateThrowsExceptionWhenIdDoesntExist() {
		assertThrows(ResponseStatusException.class, () -> categoryService.update(wrongId, new CategoryDTO()));
		
		verify(categoryRepository).getById(wrongId);
	}
	
	@Test
	void deleteShouldThrowsExceptionWhenIdExists() {
		assertThrows(ResponseStatusException.class, () -> categoryService.delete(correctId));
		
		verify(categoryRepository).deleteById(correctId);
	}
	
	@Test
	void deleteShouldThrowsExceptionWhenIdDoesntExists() {
		assertThrows(ResponseStatusException.class, () -> categoryService.delete(wrongId));
		
		verify(categoryRepository).deleteById(wrongId);
	}

}
