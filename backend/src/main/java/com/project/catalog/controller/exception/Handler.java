package com.project.catalog.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class Handler {

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<String> handler(ResponseStatusException e) {
		return ResponseEntity.status(e.getStatus()).body(e.getReason());
	}
}
