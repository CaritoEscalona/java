package com.example.employees.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employees.entity.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>{
	Optional<AppUser> findByEmail(String email);
}
