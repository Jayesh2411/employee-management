package employee.error;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class EmpError {
    public String message;

    public EmpError(String employee_id) {
        message = "Employee with " + employee_id + " was not found";
    }

    @Override
    public String toString() {
        return "EmpError{" +
                "message='" + message + '\'' +
                '}';
    }
}
