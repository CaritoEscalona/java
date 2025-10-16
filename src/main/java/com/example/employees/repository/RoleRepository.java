package com.example.employees.repository;

import com.example.employees.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository  // Marca esta interfaz como un componente de repositorio en Spring,
// lo que permite la gestión automática de excepciones y la detección
// automática de la implementación para inyección de dependencias.
public interface RoleRepository extends JpaRepository<Role, Long> {

// Método para buscar un Role por su nombre. Retorna un Optional que puede 
// estar vacío si no encuentra ningún rol con ese nombre.
Optional<Role> findByName(String name);
}