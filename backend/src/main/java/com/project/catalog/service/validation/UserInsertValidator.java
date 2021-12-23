package com.project.catalog.service.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Service;

import com.project.catalog.controller.exception.ValidationErrorResponse;
import com.project.catalog.dto.UserInsertDTO;
import com.project.catalog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	private final UserRepository userRepository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<ValidationErrorResponse> list = new ArrayList<>();
		
		if (userRepository.findByEmail(dto.getEmail()) != null) {
			list.add(new ValidationErrorResponse("email", "email já cadastrado"));
		}
		
		for (ValidationErrorResponse e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldError())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
