package com.example.employees.repository;

import com.example.employees.entity.SalaryRecord;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository // Indica que esta interfaz es un componente de acceso a datos (DAO/Repository), 
//especializado para la persistencia y gestión de la entidad SalaryRecord. Permite a Spring detectarla automáticamente 
//y manejar excepciones de acceso a datos de forma unificada[4][6][7].

public interface SalaryRecordRepository extends JpaRepository<SalaryRecord, Long> { 
	// Define el repositorio como una interfaz que extiende JpaRepository, proporcionando todos los métodos CRUD estándar 
	//para SalaryRecord usando IDs tipo Long.

    @Query("SELECT s FROM SalaryRecord s WHERE s.employee.id = :employeeId ORDER BY s.calculatedAt DESC") 
    // Declara una consulta JPQL personalizada directamente en la interfaz. Selecciona todos los SalaryRecord 
    // cuyo empleado tiene el ID especificado, ordenados por fecha de cálculo descendente.
    List<SalaryRecord> findByEmployeeIdOrderByCalculatedAtDesc(@Param("employeeId") Long employeeId); 
    // Define el método que ejecuta esa consulta. El parámetro employeeId es enlazado a la variable :employeeId 
    //de la query gracias a la anotación @Param. Retorna una lista de registros de salario del empleado, iniciando 
    //por el más reciente.
}
