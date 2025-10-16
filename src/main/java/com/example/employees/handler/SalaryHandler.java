package com.example.employees.handler;

import com.example.employees.dto.EmployeeRequest;

public interface SalaryHandler {
    double calculateSalary(EmployeeRequest req);
    String getRoleName();
}
