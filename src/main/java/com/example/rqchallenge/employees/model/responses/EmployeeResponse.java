package com.example.rqchallenge.employees.model.responses;

import com.example.rqchallenge.employees.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private String status;
    private Employee data;
    private String message;
}
