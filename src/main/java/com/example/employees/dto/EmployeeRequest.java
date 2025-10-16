package com.example.employees.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Request: calculate salary for existing employee
 */
public class EmployeeRequest {
	// Nombre del empleado, no puede estar vacío ni ser sólo espacios
	@NotBlank(message = "name must not be blank")
	private String name;

    @Positive(message = "baseSalary must be positive")
    private double baseSalary;

    @PositiveOrZero(message = "bonus must be positive or zero")
    private double bonus;

    @PositiveOrZero(message = "hoursWorked must be positive or zero")
    private double hoursWorked;



    public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus = bonus; }
    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }
}
