package com.project.catalog.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

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
	@NotBlank
	private String name;
	@NotBlank
	private String description;
	@Positive
	private BigDecimal price;
	@NotBlank
	private String imgUrl;
	@PastOrPresent
	private Instant date;
	@NotNull
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
