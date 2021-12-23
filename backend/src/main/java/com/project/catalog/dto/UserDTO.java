package com.project.catalog.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.project.catalog.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private Long id;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@Email @NotBlank
	private String email;
	private List<RoleDTO> roles = new ArrayList<>();
	
	public UserDTO(User user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		user.getRoles().forEach(role -> this.getRoles().add(new RoleDTO(role)));
	}
}
