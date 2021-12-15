package com.project.catalog.dto;

import com.project.catalog.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

	private long id;
	private String name;
	
	public CategoryDTO(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}
}
