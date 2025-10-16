package com.example.employees.service.impl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.employees.entity.AppUser;
import com.example.employees.entity.Role;
import com.example.employees.exception.InvalidDataException;
import com.example.employees.repository.RoleRepository;
import com.example.employees.repository.UserRepository;

@Service
public class UserServiceImpl {

	private final UserRepository userRepo;
	  private final RoleRepository roleRepo;
	  private final PasswordEncoder encoder;
	
	// Constructor que recibe las dependencias necesarias para inyección automática (por Spring)
    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
    }
    public AppUser register(String email, String rawPassword, String roleName) {
        if(userRepo.findByEmail(email).isPresent()) throw new RuntimeException("exists");
	        AppUser u = new AppUser();
	        u.setEmail(email);
	        u.setPassword(encoder.encode(rawPassword));
	        Role role = roleRepo.findByName(roleName).orElseThrow(() -> new InvalidDataException("Employee has no role assigned"));
	        u.getRoles().add(role);
        return userRepo.save(u);
      }

}
