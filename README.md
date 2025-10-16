# Empleados - Solución Tarea 2 (sin seguridad)
This is the solution project for the evaluated task 2.

Features:
- Java 17, Spring Boot 3
- Entities: Employee, Role, EmployeeDetails, SalaryRecord
- Relationships: @ManyToOne, @OneToMany, @OneToOne with FetchType.LAZY
- Handlers for salary calculation (FullTime, PartTime)
- Flyway migration V1 to create tables and seed roles
- GlobalExceptionHandler and DTOs with Bean Validation

Run:
1. Configure Postgres and update src/main/resources/application.yml
2. mvn -DskipTests clean package
3. mvn spring-boot:run
4. Use the endpoints:
   - POST /api/employees/salary
   - GET /api/employees/types
   - GET /api/employees
# java
