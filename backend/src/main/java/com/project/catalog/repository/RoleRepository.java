package com.project.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.catalog.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
