package com.example.employees.service.interfaces;

import com.example.employees.dto.EmployeeCreateRequest;
import com.example.employees.dto.EmployeeCreateResponse;
import com.example.employees.dto.EmployeeDetailsRequest;
import com.example.employees.dto.EmployeeDetailsResponse;
import com.example.employees.dto.EmployeeFullResponse;
import com.example.employees.dto.EmployeeRequest;
import com.example.employees.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse calculateAndSaveSalary(EmployeeRequest req);
    List<String> getSupportedRoles();
    EmployeeCreateResponse createEmployee(EmployeeCreateRequest req);
    EmployeeDetailsResponse updateEmployee(Long id, EmployeeDetailsRequest req);
    void deleteEmployee(Long id);
	List<EmployeeFullResponse> getAllEmployees();
}
