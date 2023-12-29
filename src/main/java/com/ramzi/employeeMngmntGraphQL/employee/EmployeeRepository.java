package com.ramzi.employeeMngmntGraphQL.employee;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Integer> {
    Flux<Employee> getEmployeeByName(String name);
    Flux<Employee> getAllEmployeeByDepartmentId(Integer departmentId);
}
