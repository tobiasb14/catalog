package com.project.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.catalog.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
