package com.example.rqchallenge.employees.client;

import com.example.rqchallenge.employees.model.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IEmployeeClient {
    List<Employee> getAllEmployees();

    Employee getEmployeeById(String id);

    String createEmployee(Map<String, Object> employeeInput);

    String deleteEmployeeById(String id);
}