package employee.controller;

import employee.entities.EmployeeRequest;
import employee.entities.Param;
import employee.entities.SearchRequest;
import employee.error.SearchError;
import employee.repository.Employee;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeControllerTest {

    public static final int ONE_MILLION = 1_0;

    @Test
    void filterShouldReturnErrorIfEmptyObjectInBodyOrEmptyFields() {
        EmployeeController employeeController = new EmployeeController();
        SearchRequest searchRequest = new SearchRequest();
        List<Employee> employeeList = new ArrayList<>();

        SearchError searchError = employeeController.filter(searchRequest, employeeList);

        assertNotNull(searchError);
        assertEquals("At least one search criteria should be passed.", searchError.message.get(0));
    }

    @Test
    void filterShouldReturnErrorIfFieldNameMissingInBody() {
        EmployeeController employeeController = new EmployeeController();

        List<Employee> employeeList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        Param param = new Param();
        param.eq = "Test";
        List<Param> fields = new ArrayList<>();
        fields.add(param);
        searchRequest.fields = fields;

        SearchError searchError = employeeController.filter(searchRequest, employeeList);

        assertNotNull(searchError);
        assertEquals("fieldName must be set.", searchError.message.get(0));
    }

    @Test
    void filterShouldReturnErrorIfEqMissingInBody() {
        EmployeeController employeeController = new EmployeeController();

        List<Employee> employeeList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        Param param = new Param();
        param.fieldName = "city";

        List<Param> fields = new ArrayList<>();
        fields.add(param);
        searchRequest.fields = fields;

        SearchError searchError = employeeController.filter(searchRequest, employeeList);

        assertNotNull(searchError);
        assertEquals("city: At least one of eq, neq must be set.", searchError.message.get(0));
    }

    @Test
    void filterShouldReturnTwoErrorsIfFieldNameAndEqMissingInBody() {
        EmployeeController employeeController = new EmployeeController();

        List<Employee> employeeList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        Param param = new Param();
        param.fieldName = "";
        param.eq = "";
        List<Param> fields = new ArrayList<>();
        fields.add(param);
        searchRequest.fields = fields;

        SearchError searchError = employeeController.filter(searchRequest, employeeList);

        assertNotNull(searchError);
        assertEquals("fieldName must be set.", searchError.message.get(0));
        assertEquals(": At least one of eq, neq must be set.", searchError.message.get(1));
    }

    @Test
    void filterShouldReturnTwoErrorsIfFieldNameAndEqMissingInDifferentIndexesBody() {
        EmployeeController employeeController = new EmployeeController();

        List<Employee> employeeList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        List<Param> fields = new ArrayList<>();

        Param param1 = new Param();
        param1.fieldName = "name";
        param1.eq = "";
        fields.add(param1);

        Param param2 = new Param();
        param2.fieldName = "";
        param2.eq = "test";
        fields.add(param2);

        searchRequest.fields = fields;

        SearchError searchError = employeeController.filter(searchRequest, employeeList);

        assertNotNull(searchError);

        assertEquals("name: At least one of eq, neq must be set.", searchError.message.get(0));
        assertEquals("fieldName must be set.", searchError.message.get(1));
    }

    @Test
    void filterShouldNullIfProperFieldsInBody() {
        EmployeeController employeeController = new EmployeeController();
        SearchRequest searchRequest = new SearchRequest();
        Param param = new Param();
        param.fieldName = "name";
        param.eq = "Test";
        List<Param> fields = new ArrayList<>();
        fields.add(param);
        searchRequest.fields = fields;

        List<Employee> employeeList = new ArrayList<>();
        SearchError searchError = employeeController.filter(searchRequest, employeeList);

        assertNull(searchError);
    }

    @Test
    void filterShouldReturnNullErrorAndEmptyEmployeeListIfProperFieldsInBody() {
        EmployeeController employeeController = new EmployeeController();
        SearchRequest searchRequest = new SearchRequest();
        Param param = new Param();
        param.fieldName = "name";
        param.eq = "Test";
        List<Param> fields = new ArrayList<>();
        fields.add(param);
        searchRequest.fields = fields;

        List<Employee> employeeList = new ArrayList<>();
        SearchError searchError = employeeController.filter(searchRequest, employeeList);

        assertNull(searchError);
        assertEquals(Collections.emptyList(), employeeList);
    }

    @Test
    void filterShouldReturnNullErrorAndOneEmployeeIfProperFieldsInBody() {
        EmployeeController employeeController = new EmployeeController();
        SearchRequest searchRequest = new SearchRequest();
        Param param = new Param();
        param.fieldName = "name";
        param.eq = "Test";
        List<Param> fields = new ArrayList<>();
        fields.add(param);
        searchRequest.fields = fields;

        List<Employee> employeeList = new ArrayList<>();
        SearchError searchError = employeeController.filter(searchRequest, employeeList);

        assertNull(searchError);
        assertEquals(Collections.emptyList(), employeeList);
    }


    @Test
    void create() {
        String[] names = {"John", "Emma", "Michael", "Sophia", "William", "Olivia", "James", "Ava", "Robert", "Isabella", "David", "Mia", "Joseph", "Emily", "Daniel", "Charlotte", "Alexander", "Amelia", "Matthew", "Abigail", "Ethan", "Harper", "Andrew", "Ella", "Christopher", "Sofia", "Benjamin", "Avery", "Samuel", "Scarlett", "Jackson", "Grace", "Logan", "Chloe", "Sebastian", "Lily", "Elijah", "Mila", "Carter", "Aria"};


        String[] cities = {"New York City", "London", "Tokyo", "Paris", "Sydney", "Beijing", "Rio de Janeiro", "Moscow", "Cape Town", "Mumbai", "Berlin", "Mexico City", "Dubai", "Istanbul", "Toronto", "Rome", "Buenos Aires", "Seoul", "Cairo", "Singapore"};
        EmployeeController employeeController = new EmployeeController();
        for (int i = 0; i < ONE_MILLION; i++) {
            Random random = new Random();

            // Generate random name
            String randomName = names[random.nextInt(names.length)];

            // Generate random city
            String randomCity = cities[random.nextInt(cities.length)];
            EmployeeRequest employee = new EmployeeRequest();
            employee.setName(randomName);
            employee.setCity(randomCity);

            employeeController.create(employee);

        }

    }

}
