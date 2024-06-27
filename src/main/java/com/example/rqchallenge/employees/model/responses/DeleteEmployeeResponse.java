package com.example.rqchallenge.employees.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteEmployeeResponse {
    private String status;
    private String data;
    private String message;
}
