package com.project.catalog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.project.catalog.service.validation.UserInsertValid;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@UserInsertValid
public class UserInsertDTO extends UserDTO{

	@NotBlank
	@Size(min = 8, message = "senha muito curta")
	private String password;
}
