package com.example.employees.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    
    /*fetch = FetchType.LAZY difiere la carga de la entidad relacionada hasta que realmente se necesite, 
     * optimizando el acceso a datos y el uso de recursos.La configuración @ManyToOne(fetch = FetchType.LAZY) 
     * significa que la entidad relacionada (por ejemplo, el Role de un Employee) no se carga automáticamente 
     * desde la base de datos cuando recuperas el empleado. Se carga únicamente cuando accedes explícitamente 
     * a la propiedad relacionada, como con emp.getRole().*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private EmployeeDetails details;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SalaryRecord> salaries = new ArrayList<>();

    public Employee(){}
    public Employee(String name, Role role){ this.name = name; this.role = role; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public EmployeeDetails getDetails() { return details; }
    public void setDetails(EmployeeDetails details) { this.details = details; details.setEmployee(this); }
    public List<SalaryRecord> getSalaries() { return salaries; }
    public void addSalary(SalaryRecord r){ r.setEmployee(this); salaries.add(r); }
}
