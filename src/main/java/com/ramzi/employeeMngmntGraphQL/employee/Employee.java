package com.ramzi.employeeMngmntGraphQL.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    private Integer id;
    private String name;
    private String salary;
    @Column("department_id")
    private Integer departmentId;
}
