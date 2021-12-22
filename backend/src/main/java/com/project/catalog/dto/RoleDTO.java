package com.project.catalog.dto;

import com.project.catalog.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

	private Long id;
	private String authority;
	
	public RoleDTO(Role role) {
		this.id = role.getId();
		this.authority = role.getAuthority();
	}
	
}
