package employee.storage;

import employee.repository.Employee;

import java.util.HashMap;

public class EmployeeCache {
    public static HashMap<Long, Employee> employeeHashMap = new HashMap<>();

}
