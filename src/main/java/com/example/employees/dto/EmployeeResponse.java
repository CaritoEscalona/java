package com.example.employees.dto;

/** Response DTO — use record in Java 17 style */
public record EmployeeResponse(String employee, String role, double salary) {}
