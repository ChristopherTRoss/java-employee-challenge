package com.example.rqchallenge;

import com.example.rqchallenge.employees.client.impl.EmployeeClientImpl;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.responses.CreateEmployeeResponse;
import com.example.rqchallenge.employees.model.responses.DeleteEmployeeResponse;
import com.example.rqchallenge.employees.model.responses.EmployeeResponse;
import com.example.rqchallenge.employees.model.responses.GetEmployeeListResponse;
import com.example.rqchallenge.employees.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RqChallengeApplicationTests {

    @InjectMocks
    private EmployeeClientImpl employeeClient;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeClientImpl employeeClientMock;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        employeeClient.clientUrl = "http://localhost:8080";
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> mockEmployees = Arrays.asList(
                new Employee(1, "John Doe", 5000, 20, ""),
                new Employee(2, "Jane Smith", 6000, 21, "")
        );
        when(employeeClientMock.getAllEmployees()).thenReturn(mockEmployees);

        List<Employee> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEmployee_name()).isEqualTo("John Doe");
        assertThat(result.get(1).getEmployee_name()).isEqualTo("Jane Smith");
    }

    @Test
    void testGetEmployeesByNameSearch() {
        List<Employee> mockEmployees = Arrays.asList(
                new Employee(1, "John Doe", 5000, 20, ""),
                new Employee(2, "Jane Smith", 6000, 21, "")
        );
        when(employeeClientMock.getAllEmployees()).thenReturn(mockEmployees);

        List<Employee> result = employeeService.getEmployeesByNameSearch("John");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmployee_name()).isEqualTo("John Doe");
    }

    @Test
    void testGetEmployeeById() {
        Employee mockEmployee = new Employee(1, "John Doe", 5000, 40, "");
        when(employeeClientMock.getEmployeeById("1")).thenReturn(mockEmployee);

        Employee result = employeeService.getEmployeeById("1");

        assertThat(result).isNotNull();
        assertThat(result.getEmployee_name()).isEqualTo("John Doe");
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        List<Employee> mockEmployees = Arrays.asList(
                new Employee(1, "John Doe", 5000, 20, ""),
                new Employee(2, "Jane Smith", 6000, 21, "")
        );
        when(employeeClientMock.getAllEmployees()).thenReturn(mockEmployees);

        Integer result = employeeService.getHighestSalaryOfEmployees();

        assertThat(result).isEqualTo(6000);
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        List<Employee> mockEmployees = Arrays.asList(
                new Employee(1, "John Doe", 5000, 20, ""),
                new Employee(2, "Jane Smith", 6000, 21, ""),
                new Employee(3, "Michael Johnson", 5500, 50, "")
        );
        when(employeeClientMock.getAllEmployees()).thenReturn(mockEmployees);

        List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();

        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isEqualTo("Jane Smith");
        assertThat(result.get(1)).isEqualTo("Michael Johnson");
        assertThat(result.get(2)).isEqualTo("John Doe"); // Order by descending salary
    }

    @Test
    void testCreateEmployee() {
        String mockResponse = "Employee created successfully";
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "John Doe");
        employeeInput.put("salary", 5000);

        when(employeeClientMock.createEmployee(anyMap())).thenReturn(mockResponse);

        String result = employeeService.createEmployee(employeeInput);

        assertThat(result).isEqualTo(mockResponse);
    }

    @Test
    void testDeleteEmployeeById() {
        String mockResponse = "Employee deleted successfully";
        when(employeeClientMock.deleteEmployeeById("1")).thenReturn(mockResponse);

        String result = employeeService.deleteEmployeeById("1");

        assertThat(result).isEqualTo(mockResponse);
    }

    @Test
    void testGetAllEmployeesClient() {
        GetEmployeeListResponse mockResponse = new GetEmployeeListResponse();
        List<Employee> mockEmployees = Arrays.asList(
                new Employee(1, "John Doe", 5000, 20, ""),
                new Employee(2, "Jane Smith", 6000, 21, ""),
                new Employee(3, "Michael Johnson", 5500, 50, "")
        );
        mockResponse.setData(mockEmployees);
        ResponseEntity<GetEmployeeListResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        doReturn(responseEntity).when(restTemplate).exchange("http://localhost:8080/employees", HttpMethod.GET, null, new ParameterizedTypeReference<GetEmployeeListResponse>() {
        });

        List<Employee> employees = employeeClient.getAllEmployees();

        assertThat(employees).isNotEmpty();
    }

    @Test
    void testGetEmployeeByIdClient() {
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setData(new Employee(1, "John Doe", 5000, 20, ""));
        ResponseEntity<EmployeeResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        doReturn(responseEntity).when(restTemplate).exchange("http://localhost:8080/employee/1", HttpMethod.GET, null, new ParameterizedTypeReference<EmployeeResponse>() {
        });

        Employee employee = employeeClient.getEmployeeById("1");

        assertThat(employee).isNotNull();
    }

    @Test
    void testCreateEmployeeClient() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "John Doe");
        employeeInput.put("salary", 50000);

        CreateEmployeeResponse mockResponse = new CreateEmployeeResponse();
        mockResponse.setStatus("Created");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(employeeInput);

        ResponseEntity<CreateEmployeeResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.CREATED);
        doReturn(responseEntity).when(restTemplate).exchange("http://localhost:8080/create", HttpMethod.POST, requestEntity, CreateEmployeeResponse.class);


        String status = employeeClient.createEmployee(employeeInput);

        assertThat(status).isEqualTo("Created");
    }

    @Test
    void testDeleteEmployeeByIdClient() {
        DeleteEmployeeResponse mockResponse = new DeleteEmployeeResponse();
        mockResponse.setMessage("Employee deleted");

        ResponseEntity<DeleteEmployeeResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        doReturn(responseEntity).when(restTemplate).exchange("http://localhost:8080/delete/1", HttpMethod.DELETE, null, DeleteEmployeeResponse.class);

        String message = employeeClient.deleteEmployeeById("1");

        assertThat(message).isEqualTo("Employee deleted");
    }

    @Test
    void testGetAllEmployees_HttpClientErrorException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null),
                any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Employee not found"));

        assertThrows(HttpClientErrorException.class, () -> employeeClient.getAllEmployees());
    }

    @Test
    void getEmployeeById_HttpClientErrorException() {
        when(restTemplate.exchange("http://localhost:8080/employee/1", HttpMethod.GET, null, new ParameterizedTypeReference<EmployeeResponse>() {
        }))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Employee not found"));

        assertThrows(HttpClientErrorException.class, () -> employeeClient.getEmployeeById("1"));
    }

    @Test
    void createEmployee_HttpClientErrorException() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "John Doe");
        employeeInput.put("salary", 50000);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(employeeInput);
        when(restTemplate.exchange("http://localhost:8080/create", HttpMethod.POST, requestEntity, CreateEmployeeResponse.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Employee not found"));

        assertThrows(HttpClientErrorException.class, () -> employeeClient.createEmployee(employeeInput));
    }

    @Test
    void deleteEmployeeById_HttpClientErrorException() {
        when(restTemplate.exchange("http://localhost:8080/delete/1", HttpMethod.DELETE, null, DeleteEmployeeResponse.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Employee not found"));

        assertThrows(HttpClientErrorException.class, () -> employeeClient.deleteEmployeeById("1"));
    }
}
