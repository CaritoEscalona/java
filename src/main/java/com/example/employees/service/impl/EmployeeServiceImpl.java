package com.example.employees.service.impl;

import com.example.employees.dto.EmployeeCreateRequest;
import com.example.employees.dto.EmployeeCreateResponse;
import com.example.employees.dto.EmployeeDetailsRequest;
import com.example.employees.dto.EmployeeDetailsResponse;
import com.example.employees.dto.EmployeeFullResponse;
import com.example.employees.dto.EmployeeRequest;
import com.example.employees.dto.EmployeeResponse;
import com.example.employees.entity.Employee;
import com.example.employees.entity.EmployeeDetails;
import com.example.employees.entity.Role;
import com.example.employees.entity.SalaryRecord;
import com.example.employees.exception.InvalidDataException;
import com.example.employees.handler.SalaryHandlerFactory;
import com.example.employees.repository.EmployeeRepository;
import com.example.employees.repository.RoleRepository;
import com.example.employees.repository.SalaryRecordRepository;
import com.example.employees.service.interfaces.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service  // Marca esta clase como un Service en Spring, que contiene la lógica de negocio
public class EmployeeServiceImpl implements EmployeeService {

    // Logger para registrar eventos e información durante la ejecución de este servicio
    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    // Inyección de dependencias para acceder a las interfaces que manipulan las entidades y lógica
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final SalaryRecordRepository salaryRepository;
    private final SalaryHandlerFactory handlerFactory;

    // Constructor que recibe las dependencias necesarias para inyección automática (por Spring)
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               RoleRepository roleRepository,
                               SalaryRecordRepository salaryRepository,
                               SalaryHandlerFactory handlerFactory) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.salaryRepository = salaryRepository;
        this.handlerFactory = handlerFactory;
    }

    @Override
    public EmployeeResponse calculateAndSaveSalary(EmployeeRequest req) {
        // Buscar empleado por nombre, si no existe arrojar excepción personalizada
        Employee emp = employeeRepository.findByName(req.getName())
            .orElseThrow(() -> new InvalidDataException("Employee not found"));

        // Validar que el empleado tenga rol asignado
        Role role = emp.getRole();
        if (role == null) throw new InvalidDataException("Employee has no role assigned");

        // Obtener el handler especializado para calcular salario según el rol
        var handler = handlerFactory.getHandler(role.getName());
        if (handler == null) throw new InvalidDataException("No handler for role " + role.getName());

        // Calcular el salario total usando la lógica del handler correspondiente
        double total = handler.calculateSalary(req);

        // Crear un nuevo registro de salario con los datos recibidos y el cálculo
        SalaryRecord record = new SalaryRecord();
        record.setBaseSalary(req.getBaseSalary());
        record.setBonus(req.getBonus());
        record.setHoursWorked(req.getHoursWorked());
        record.setTotalSalary(total);
        record.setEmployee(emp);

        // Guardar el registro en la base de datos
        salaryRepository.save(record);

        // Loguear la operación exitosa
        log.info("Saved salary record for employeeId={} total={}", emp.getId(), total);

        // Retornar un DTO simplificado con el resultado
        return new EmployeeResponse(emp.getName(), role.getName(), total);
    }

    @Override
    public List<String> getSupportedRoles() {
        // Obtener todos los roles desde la base y transformar la lista a solo nombres
        return roleRepository.findAll().stream().map(Role::getName).toList();
    }

    
    @Override
    public EmployeeCreateResponse createEmployee(EmployeeCreateRequest req) {
        // Crear un nuevo objeto Employee con datos del request
        Employee emp = new Employee();
        emp.setName(req.getName());
        
        // Buscar el rol/tipo en base a la ID recibida en el DTO, si no existe lanzar excepción
        Role role = roleRepository.findById(req.getRoleId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        emp.setRole(role);

        // Guardar el empleado en la base
        employeeRepository.save(emp);
        log.info("Saved employee={}", emp);
        // Crear y retornar respuesta DTO con algunos campos
        return new EmployeeCreateResponse(emp.getId(), emp.getName(), emp.getRole().getName());
    }
    
    @Override
    @Transactional
    public EmployeeDetailsResponse updateEmployee(Long id, EmployeeDetailsRequest req) {
        /** Busca el empleado principal por ID; si no lo encuentra, lanza una excepción controlada */
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("Employee not found with ID: " + id));

        /** Obtiene el detalle del empleado si existe, de lo contrario lo crea nuevo y asocia */
        EmployeeDetails details = employee.getDetails();
        if (details == null) {
            details = new EmployeeDetails(); // Crea nuevo detalle si no existe
            details.setEmployee(employee);   // Establece la relación con el empleado
        }

        /** Actualiza solo el email y la dirección con los valores recibidos en el request */
        details.setEmail(req.getEmail());
        details.setAddress(req.getAddress());

        /** Establece la relación bidireccional de detalle a empleado */
        employee.setDetails(details);

        /** Guarda los cambios en la base de datos; el detalle se persiste/cascada por @MapsId */
        employeeRepository.save(employee);

        /** Registra en el log la actualización realizada con los nuevos valores */
        log.info("Updated EmployeeDetails for employeeId={} email={} address={}",
                employee.getId(), details.getEmail(), details.getAddress());

        /** Devuelve una respuesta DTO con los datos relevantes del empleado actualizado */
        return new EmployeeDetailsResponse(
                employee.getName(), employee.getRole().getName(),
                employee.getDetails().getEmail(), employee.getDetails().getAddress());
    }

    @Override
    public void deleteEmployee(Long id) {
        /** Verifica si existe el empleado antes de intentar borrarlo */
        if (!employeeRepository.existsById(id)) {
            throw new InvalidDataException("Employee not found");
        }
        /** Borra el empleado por ID si existe */
        employeeRepository.deleteById(id);

        /** Log de auditoría de la eliminación */
        log.info("Employee {} deleted", id);
    }

    public Double getLastCalculatedSalaryForEmployee(Long employeeId) {
        /** Obtiene los registros salariales del empleado, ordenados desde el más reciente */
        List<SalaryRecord> salaries = salaryRepository.findByEmployeeIdOrderByCalculatedAtDesc(employeeId);

        /** Si existe al menos un registro, retorna el salario más reciente */
        if (!salaries.isEmpty()) {
            return salaries.get(0).getTotalSalary(); // Último salario calculado
        }
        /** Si no hay registros, retorna null */
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeFullResponse> getAllEmployees() {
        /** Busca todos los empleados desde la base de datos */
        List<Employee> employees = employeeRepository.findAll();

        /** Si no hay empleados, registra en el log y retorna una lista vacía */
        if (employees.isEmpty()) {
            log.info("No employees found in database");
            return List.of();
        }

        /** Convierte cada entidad Employee en un DTO de respuesta con datos completos */
        return employees.stream().map(employee -> {
            EmployeeDetails details = employee.getDetails();
            /** Si el detalle existe toma email y dirección, si no, usa 'N/A' */
            String email = (details != null && details.getEmail() != null) ? details.getEmail() : "N/A";
            String address = (details != null && details.getAddress() != null) ? details.getAddress() : "N/A";
            String roleName = (employee.getRole() != null) ? employee.getRole().getName() : "N/A";
            
            /** Obtiene el último salario calculado desde la base de datos */
            Double salary = getLastCalculatedSalaryForEmployee(employee.getId());

            /** Crea y retorna el DTO de respuesta con toda la información */
            return new EmployeeFullResponse(
                    employee.getId(),
                    employee.getName(),
                    roleName,
                    email,
                    address,
                    salary
            );
        }).toList();
    }
    
    
}
