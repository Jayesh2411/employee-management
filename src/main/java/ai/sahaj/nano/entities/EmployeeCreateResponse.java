package ai.sahaj.nano.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class EmployeeCreateResponse {
    public String employeeId;

    public EmployeeCreateResponse(String employeeId) {
        this.employeeId = employeeId;
    }
}
