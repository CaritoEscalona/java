package com.example.employees.handler;

import com.example.employees.dto.EmployeeRequest;
import org.springframework.stereotype.Component;

@Component
public class FullTimeSalaryHandler implements SalaryHandler {

    @Override
    public double calculateSalary(EmployeeRequest req) {
        return req.getBaseSalary() + req.getBonus();
    }

    @Override
    public String getRoleName() { return "FULLTIME"; }
}
