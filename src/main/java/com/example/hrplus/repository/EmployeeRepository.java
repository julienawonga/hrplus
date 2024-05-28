package com.example.hrplus.repository;

import com.example.hrplus.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByMatricule(Long matricule);
    long countEmployeeByDepartement(String departement);
}
