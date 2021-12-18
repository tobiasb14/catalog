package com.project.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

@SpringBootTest
@Transactional
public class CategoryServiceIT {

	@Autowired
	private CategoryService categoryService;
	
	private long correctId;
	private long wrongId;
	private long categoryCount;
	
	@BeforeEach
	void setUp() throws Exception {
		correctId = 1L;
		wrongId = 100L;
		categoryCount = 3L;
	}
	
	@Test
	void findAllPagedShouldReturnPage() {
		Page<CategoryDTO> result = categoryService.findAllPaged(PageRequest.of(0, 5));
		
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(0, result.getNumber());
		assertEquals(5, result.getSize());
		assertEquals(categoryCount, result.getTotalElements());
	}
	
	@Test
	void findAllPagedShouldReturnPageOrderBy() {
		Page<CategoryDTO> result = categoryService.findAllPaged(PageRequest.of(0, 5, Sort.by("id")));
		
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(1L, result.getContent().get(0).getId());
		assertEquals(2L, result.getContent().get(1).getId());
		assertEquals(3L, result.getContent().get(2).getId());
	}
	
	@Test
	void findByIdReturnCategoryDtoWhenIdExists() {
		CategoryDTO result = categoryService.findById(correctId);
		
		assertEquals(1L, result.getId());
		assertEquals("Livros", result.getName());
	}
	
	@Test
	void findByIdThrowsExceptionWhenIdDoesntExists() {
		assertThrows(ResponseStatusException.class, () -> categoryService.findById(wrongId));
	}
	
	@Test
	void insertCreateCategoryWhenNewDtoIsCorrect() {
		CategoryDTO insertCategory = new CategoryDTO();
		CategoryDTO result = categoryService.insert(insertCategory);
		
		assertEquals(4L, result.getId());
	}
	
	@Test
	void updateReturnsCategoryDtoWhenIdExists() {
		CategoryDTO edit = new CategoryDTO(correctId, "garden");
		CategoryDTO result = categoryService.update(correctId, edit);
		
		assertEquals(edit.getId(), result.getId());
		assertEquals(edit.getName(), result.getName());
		
	}
	
	@Test
	void updateThrowsExceptionWhenIdDoesntExist() {
		assertThrows(ResponseStatusException.class, () -> categoryService.update(wrongId, new CategoryDTO()));
	}
	
	@Test
	void deleteThrowsExceptionWhenIdExists() {
		categoryService.delete(correctId);
		assertThrows(ResponseStatusException.class, () -> categoryService.delete(correctId));
	}
	
	@Test
	void deleteShouldThrowsExceptionWhenIdDoesntExists() {
		assertThrows(ResponseStatusException.class, () -> categoryService.delete(wrongId));
	}
}
