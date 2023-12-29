package com.ramzi.employeeMngmntGraphQL.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddEmployeeInput {
    private String name;
    private String salary;
    private Integer departmentId;
}
