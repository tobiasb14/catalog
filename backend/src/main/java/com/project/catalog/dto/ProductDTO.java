package com.project.catalog.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.project.catalog.entity.Category;
import com.project.catalog.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

	private long id;
	private String name;
	private String description;
	private BigDecimal price;
	private String imgUrl;
	private Instant date;
	private List<CategoryDTO> categories = new ArrayList<>();
	
	public ProductDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.imgUrl = product.getImgUrl();
		this.date = product.getDate();
	}
	
	public ProductDTO(Product product, Set<Category> categories) {
		this(product);
		categories.forEach(category -> this.categories.add(new CategoryDTO(category)));
	}
}
