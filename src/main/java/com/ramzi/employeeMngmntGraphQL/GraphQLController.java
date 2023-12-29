package com.ramzi.employeeMngmntGraphQL;

import com.ramzi.employeeMngmntGraphQL.Config.HttpClientConfig;
import com.ramzi.employeeMngmntGraphQL.department.Department;
import com.ramzi.employeeMngmntGraphQL.department.DepartmentRepository;
import com.ramzi.employeeMngmntGraphQL.employee.AddEmployeeInput;
import com.ramzi.employeeMngmntGraphQL.employee.Employee;
import com.ramzi.employeeMngmntGraphQL.employee.EmployeeRepository;
import com.ramzi.employeeMngmntGraphQL.employee.UpdateSalaryInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GraphQLController {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final HttpClientConfig httpClientConfig;
    private HttpGraphQlClient httpGraphQlClient;
    Function<AddEmployeeInput, Employee> mapping = addEmployeeInput -> {
        var employee = new Employee();
        employee.setName(addEmployeeInput.getName());
        employee.setSalary(addEmployeeInput.getSalary());
        employee.setDepartmentId(addEmployeeInput.getDepartmentId());

        return employee;
    };

    @MutationMapping
    public Mono<Employee> addEmployee(@Argument AddEmployeeInput addEmployeeInput){
        return this.employeeRepository.save(mapping.apply(addEmployeeInput));
    }

    @QueryMapping
    public Flux<Employee> employeeByName(@Argument String employeeName){
        return this.employeeRepository.getEmployeeByName(employeeName);
    }

    @MutationMapping
    public Mono<Employee> updateSalary(@Argument UpdateSalaryInput updateSalaryInput){
        return this.employeeRepository.findById(updateSalaryInput.getEmployeeId())
                .flatMap(employee -> {
                    employee.setSalary(updateSalaryInput.getSalary());
                    return this.employeeRepository.save(employee);
                });
    }

    @QueryMapping
    public Flux<Department> allDepartments(){
        return this.departmentRepository.findAll();
    }
    // This method has multiple invocation calls to get all departments
    /**@SchemaMapping(typeName = "Department", field = "employees")
    public Flux<Employee> employees(Department department){
        log.info("department id #{}", department.getId());
        return this.employeeRepository.getAllEmployeeByDepartmentId(department.getId());
    }**/

    // This method will invoke just once to get the corresponding employees of the department
    @BatchMapping
    public Mono<Map<Department, Collection<Employee>>> employees(List<Department> departments){
        return Flux.fromIterable(departments)
                .flatMap(department -> this.employeeRepository.getAllEmployeeByDepartmentId(department.getId()))
                .collectMultimap(employee -> departments.stream().filter(department -> department.getId().equals(employee.getDepartmentId())).findFirst().get());
    }

    // Getting many records in real time
    @SubscriptionMapping
    public Flux<Employee> allEmployees(){
        return this.employeeRepository.findAll().delayElements(Duration.ofSeconds(3));
    }

    // Test HTTP end point
    @GetMapping("/employeeName")
    public Mono<List<Employee>> getEmployeeByName(){
        var document = "query{\n" +
                "  employeeByName(employeeName: \"ramzi\"){\n" +
                "    id,name,salary, departmentId\n" +
                "  }\n" +
                "}";
        return this.httpClientConfig.httpGraphQlClient().document(document)
                .retrieve("/employeeName")
                .toEntityList(Employee.class);
    }
}
