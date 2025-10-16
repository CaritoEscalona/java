package com.example.employees.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // ADMIN, FULLTIME, PARTTIME

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();

    public Role() {}
    public Role(String name){ this.name = name; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Employee> getEmployees() { return employees; }
}
