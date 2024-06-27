package com.example.rqchallenge.employees.client.impl;

import com.example.rqchallenge.employees.client.IEmployeeClient;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.responses.CreateEmployeeResponse;
import com.example.rqchallenge.employees.model.responses.DeleteEmployeeResponse;
import com.example.rqchallenge.employees.model.responses.EmployeeResponse;
import com.example.rqchallenge.employees.model.responses.GetEmployeeListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EmployeeClientImpl implements IEmployeeClient {

    @Value("${spring.datasource.url}")
    public String clientUrl;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Employee> getAllEmployees() {
        try {
            ResponseEntity<GetEmployeeListResponse> response = restTemplate.exchange(
                    clientUrl + "/employees",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody().getData();
        } catch (HttpClientErrorException e) {
            // Handle HTTP client errors
            log.error("Client error occurred in getAllEmployees for call {}", clientUrl + "/employees", e);
            throw new HttpClientErrorException(e.getStatusCode(), e.getStatusText());
        } catch (Exception e) {
            // Handle all other exceptions
            log.error("Client error occurred in getAllEmployees for call {}", clientUrl + "/employees", e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Override
    public Employee getEmployeeById(String id) {
        try {
            ResponseEntity<EmployeeResponse> response = restTemplate.exchange(
                    clientUrl + "/employee/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    });
            return response.getBody().getData();
        } catch (HttpClientErrorException e) {
            // Handle HTTP client errors
            log.error("Client error occurred in getEmployeeById for call {}", clientUrl + "/employee/" + id, e);
            throw new HttpClientErrorException(e.getStatusCode(), e.getStatusText());
        } catch (Exception e) {
            // Handle all other exceptions
            log.error("Client error occurred in getEmployeeById for call {}", clientUrl + "/employee/" + id, e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Override
    public String createEmployee(Map<String, Object> employeeInput) {
        try {
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(employeeInput);
            ResponseEntity<CreateEmployeeResponse> response = restTemplate.exchange(
                    clientUrl + "/create",
                    HttpMethod.POST,
                    requestEntity,
                    CreateEmployeeResponse.class);
            return response.getBody().getStatus();
        } catch (HttpClientErrorException e) {
            // Handle HTTP client errors
            log.error("Client error occurred in createEmployee for call {} with the params {}", clientUrl + "/create", employeeInput, e);
            throw new HttpClientErrorException(e.getStatusCode(), e.getStatusText());
        } catch (Exception e) {
            // Handle all other exceptions
            log.error("Client error occurred in createEmployee for call {} with the params {}", clientUrl + "/create", employeeInput, e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @Override
    public String deleteEmployeeById(String id) {
        try {
            ResponseEntity<DeleteEmployeeResponse> response = restTemplate.exchange(
                    clientUrl + "/delete/" + id,
                    HttpMethod.DELETE,
                    null,
                    DeleteEmployeeResponse.class);
            return response.getBody().getMessage();
        } catch (HttpClientErrorException e) {
            // Handle HTTP client errors
            log.error("Client error occurred in deleteEmployeeById for call {}", clientUrl + "/delete/" + id, e);
            throw new HttpClientErrorException(e.getStatusCode(), e.getStatusText());
        } catch (Exception e) {
            // Handle all other exceptions
            log.error("Client error occurred in deleteEmployeeById for call {}", clientUrl + "/delete/" + id, e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }
}
