package com.example.rqchallenge.employees.model.responses;

import com.example.rqchallenge.employees.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEmployeeListResponse {
    private String status;
    private List<Employee> data;
    private String message;
}
