package employee.controller;

import employee.entities.EmployeeCreateResponse;
import employee.entities.EmployeeRequest;
import employee.entities.Param;
import employee.entities.SearchRequest;
import employee.error.EmpError;
import employee.error.SearchError;
import employee.repository.Employee;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Controller()
public class EmployeeController {


    @Post("/employee")
    public HttpResponse<EmployeeCreateResponse> create(@Body EmployeeRequest request) {
        Employee employee = new Employee();
        String empId = employee.createEmployee(request);
        EmployeeCreateResponse employeeCreateResponse = new EmployeeCreateResponse(empId);

        return HttpResponse.created(employeeCreateResponse);
    }

    @Get(value = "/employees/all", produces = MediaType.APPLICATION_JSON)
    public MutableHttpResponse<List<Employee>> readAll() {
        Employee employee = new Employee();
        HashMap<Long, Employee> map = employee.readAll();
        List<Employee> employeeList = new ArrayList<>(map.values());
        employeeList.forEach(System.out::println);

        return HttpResponse.ok(employeeList);
    }

    @Get("/employee/{id}")
    public MutableHttpResponse<?> read(String id) {
        Employee employee = new Employee();
        HashMap<Long, Employee> map = employee.readAll();
        if (map.containsKey(Long.parseLong(id))) {
            Employee emp = map.get(Long.parseLong(id));
            return HttpResponse.ok(emp);
        } else {
            return HttpResponse.notFound(new EmpError(id));
        }

    }

    @Put("/employee/{id}")
    public MutableHttpResponse<?> update(String id, @Body EmployeeRequest request) {
        Employee employee = new Employee();
        Employee updatedEmp = new Employee(id, request.name, request.city);
        try {
            if (employee.update(id, request) == 1) {
                return HttpResponse.created(updatedEmp);
            }
        } catch (IOException e) {
            return HttpResponse.notFound(new EmpError(id));
        }
        return HttpResponse.notFound(new EmpError(id));

    }

    @Delete("/employee/{id}")
    public MutableHttpResponse<?> delete(String id) {

        Employee employee = new Employee();
        HashMap<Long, Employee> map = employee.readAll();
        try {
            if (employee.delete(id) == 1) {
                return HttpResponse.ok(map.get(Long.parseLong(id)));
            }
        } catch (IOException e) {
            return HttpResponse.notFound(new EmpError(id));
        }
        return HttpResponse.notFound(new EmpError(id));
    }

    @Post("/employees/search")
    public MutableHttpResponse<?> search(@Body SearchRequest request) {
        List<Employee> employeeList = new ArrayList<>();
        SearchError searchError = filter(request, employeeList);
        if (searchError != null) {
            return HttpResponse.badRequest(searchError);
        }
        return HttpResponse.ok(employeeList);
    }

    public SearchError filter(SearchRequest request, List<Employee> employeeList) {
        List<String> messages = new ArrayList<>();

        if (request == null || request.fields == null || request.fields.isEmpty()) {
            messages.add("At least one search criteria should be passed.");
            return new SearchError(messages);
        }

        Param param1 = request.fields.get(0);
        if (param1.fieldName == null || param1.fieldName.isEmpty()) {
            messages.add("fieldName must be set.");
        }
        if ((param1.eq == null) && (param1.neq == null) || (param1.eq != null) && param1.eq.isEmpty() || (param1.neq != null) && param1.neq.isEmpty()) {
            String error = param1.fieldName + ": At least one of eq, neq must be set.";
            messages.add(error);
        }
        Employee employee = new Employee();
        if (messages.isEmpty()) {

            if (request.fields.size() > 1) {
                Param param2 = request.fields.get(1);
                if (param2.fieldName == null || param2.fieldName.isEmpty()) {
                    messages.add("fieldName must be set.");
                }
                if ((param2.eq == null) && (param2.neq == null) || (param2.eq != null) && param2.eq.isEmpty() || (param2.neq != null) && param2.neq.isEmpty()) {
                    String error = param2.fieldName + ": At least one of eq, neq must be set.";
                    messages.add(error);
                }

                String condition = request.condition;
                if (condition == null) {
                    request.condition = "AND";
                }
                Collection<Employee> list = employee.readAll().values();
                filterParameter(employeeList, param1, list);
                if (request.condition.isEmpty() || request.condition.equals("AND")) {
                    filterParameter(employeeList, param2);
                } else if (request.condition.equals("OR")) {
                    filterParameter(employeeList, param2, list);
                } else {
                    employeeList.clear();
                }
            } else {
                Collection<Employee> list = employee.readAll().values();
                filterParameter(employeeList, param1, list);
            }

        }


        if (!messages.isEmpty()) return new SearchError(messages);
        return null;
    }

    private void filterParameter(List<Employee> employeeList, Param param2) {
        List<Employee> filterEmployeeList = new ArrayList<>();
        if (param2.fieldName != null && param2.fieldName.equals("name")) {
            if (param2.eq != null && !param2.eq.isEmpty()) {
                for (Employee emp : employeeList) {
                    if (emp.name.equals(param2.eq)) {
                        filterEmployeeList.add(emp);
                    }
                }
            } else if (param2.neq != null && !param2.neq.isEmpty()) {
                for (Employee emp : employeeList) {
                    if (!emp.name.equals(param2.neq)) {
                        filterEmployeeList.add(emp);
                    }
                }
            }
        } else if (param2.fieldName != null && param2.fieldName.equals("city")) {
            if (param2.eq != null && !param2.eq.isEmpty()) {
                for (Employee emp : employeeList) {
                    if (emp.city.equals(param2.eq)) {
                        filterEmployeeList.add(emp);
                    }
                }
            } else if (param2.neq != null && !param2.neq.isEmpty()) {
                for (Employee emp : employeeList) {
                    if (!emp.city.equals(param2.neq)) {
                        filterEmployeeList.add(emp);
                    }
                }
            }
        }
        employeeList.retainAll(filterEmployeeList);

    }


    private void filterParameter(List<Employee> employeeList, Param param1, Collection<Employee> list) {
        if (param1.fieldName != null && param1.fieldName.equals("name")) {
            if (param1.eq != null && !param1.eq.isEmpty()) {
                for (Employee emp : list) {
                    if (emp.name.equals(param1.eq)) {
                        employeeList.add(emp);
                    }
                }
            } else if (param1.neq != null && !param1.neq.isEmpty()) {
                for (Employee emp : list) {
                    if (!emp.name.equals(param1.neq)) {
                        employeeList.add(emp);
                    }
                }
            }
        } else if (param1.fieldName != null && param1.fieldName.equals("city")) {
            if (param1.eq != null && !param1.eq.isEmpty()) {
                for (Employee emp : list) {
                    if (emp.city.equals(param1.eq)) {
                        employeeList.add(emp);
                    }
                }
            } else if (param1.neq != null && !param1.neq.isEmpty()) {
                for (Employee emp : list) {
                    if (!emp.city.equals(param1.neq)) {
                        employeeList.add(emp);
                    }
                }
            }
        }
    }

}
