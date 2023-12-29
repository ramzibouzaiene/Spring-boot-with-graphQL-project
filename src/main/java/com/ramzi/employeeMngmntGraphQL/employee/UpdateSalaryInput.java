package com.ramzi.employeeMngmntGraphQL.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSalaryInput {
    private Integer employeeId;
    private String salary;
}
