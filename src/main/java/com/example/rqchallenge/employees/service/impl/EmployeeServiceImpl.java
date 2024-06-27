package com.example.rqchallenge.employees.service.impl;

import com.example.rqchallenge.employees.client.IEmployeeClient;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    private IEmployeeClient employeeClient;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeClient.getAllEmployees();
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = employeeClient.getAllEmployees();
        return employees.stream()
                .filter(employee -> employee.getEmployee_name().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employeeClient.getEmployeeById(id);
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        List<Employee> employees = employeeClient.getAllEmployees();
        return employees.stream()
                .max(Comparator.comparing(Employee::getEmployee_salary)).get().getEmployee_salary();
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = employeeClient.getAllEmployees();
        return employees.stream()
                .sorted(Comparator.comparing(Employee::getEmployee_salary).reversed())
                .limit(10)
                .map(Employee::getEmployee_name)
                .collect(Collectors.toList());
    }

    @Override
    public String createEmployee(Map<String, Object> employeeInput) {
        return employeeClient.createEmployee(employeeInput);
    }

    @Override
    public String deleteEmployeeById(String id) {
        return employeeClient.deleteEmployeeById(id);
    }
}
