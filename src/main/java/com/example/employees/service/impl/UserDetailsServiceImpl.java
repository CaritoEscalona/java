package com.example.employees.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.employees.entity.AppUser;
import com.example.employees.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repo;

    public UserDetailsServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = repo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        var authorities = user.getRoles().stream()
            .map(r -> new SimpleGrantedAuthority(r.getName()))
            .toList();
        return new User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
    }
}
