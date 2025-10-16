package com.example.employees.dto;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegisterRequest(String email, String password) {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder(12);
	}
}