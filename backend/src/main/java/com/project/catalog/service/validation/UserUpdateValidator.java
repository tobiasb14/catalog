package com.project.catalog.service.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

import com.project.catalog.controller.exception.ValidationErrorResponse;
import com.project.catalog.dto.UserUpdateDTO;
import com.project.catalog.entity.User;
import com.project.catalog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {
	
	private final UserRepository userRepository;
	private final HttpServletRequest request;
	@Override
	public void initialize(UserUpdateValid ann) {
	}

	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
		@SuppressWarnings("unchecked")
		var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		long userId = Long.parseLong(uriVars.get("id"));
		 
		List<ValidationErrorResponse> list = new ArrayList<>();
		
		User user = userRepository.findByEmail(dto.getEmail());
		if (user != null && userId != user.getId()) {
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
