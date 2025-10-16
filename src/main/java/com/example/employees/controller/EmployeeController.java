package com.example.employees.controller;

import com.example.employees.dto.EmployeeCreateRequest;
import com.example.employees.dto.EmployeeCreateResponse;
import com.example.employees.dto.EmployeeFullResponse;
import com.example.employees.dto.EmployeeRequest;
import com.example.employees.dto.EmployeeResponse;
import com.example.employees.service.interfaces.EmployeeService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) { this.service = service; }

    @PostMapping("/salary")
    public ResponseEntity<EmployeeResponse> calculateSalary(@Valid @RequestBody EmployeeRequest req) {
        return ResponseEntity.ok(service.calculateAndSaveSalary(req));
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getTypes() {
        return ResponseEntity.ok(service.getSupportedRoles());
    }
    
    @PostMapping("/create")
    public ResponseEntity<EmployeeCreateResponse> createEmployee(@Valid @RequestBody EmployeeCreateRequest req) {
        EmployeeCreateResponse saved = service.createEmployee(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<EmployeeFullResponse>> all() {
        return ResponseEntity.ok(service.getAllEmployees());
    }

}
