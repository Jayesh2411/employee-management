package employee.repository;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import employee.entities.EmployeeRequest;
import employee.storage.EmployeeID;

import java.io.*;
import java.util.HashMap;

import static employee.storage.EmployeeCache.employeeHashMap;

@JsonSerialize
public class Employee implements Serializable {
    String idPath = "empid";
    public static final String HOME_EMP = "emp";

    public String employeeId;
    public String name;
    public String city;

    public Employee() {
    }


    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String createEmployee(EmployeeRequest employeeRequest) {

        EmployeeID employeeID = new EmployeeID(idPath);

        this.employeeId = String.valueOf(employeeID.getNewEmpId());
        this.name = employeeRequest.name;
        this.city = employeeRequest.city;

        saveEmp(this.employeeId + "," + this.name + "," + this.city);
        return employeeId;
    }

    public void saveEmp(String emp) {

        File f = new File(HOME_EMP);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Error while creating file");
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(f, true))) {
            // Append data to the file
            writer.write(emp);

            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Employee(String employeeId, String name, String city) {
        this.employeeId = employeeId;
        this.name = name;
        this.city = city;
    }


    public HashMap<Long, Employee> readAll() {
        if (employeeHashMap.isEmpty()) {
            try (BufferedReader br = new BufferedReader(new FileReader(HOME_EMP))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] empFields = line.split(",");
                    Employee emp = new Employee(empFields[0], empFields[1], empFields[2]);
                    employeeHashMap.put(Long.parseLong(emp.employeeId), emp);
                }
            } catch (Exception e) {
                System.out.println("Error: Error while reading file. " + HOME_EMP);
            }
        }

        return employeeHashMap;

    }

    @Override
    public String toString() {
        return "Employee{" + "employeeId='" + employeeId + '\'' + ", name='" + name + '\'' + ", city='" + city + '\'' + '}';
    }

    public int update(String id, EmployeeRequest request) throws IOException {
        File inputFile = new File(HOME_EMP);
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        StringBuilder content = new StringBuilder();
        String replacementLine = id + "," + request.name + "," + request.city;
        String line;
        int found = -1;

        while ((line = reader.readLine()) != null) {
            // Check if the line matches the line to be replaced
            if (line.contains(id)) {
                System.out.println("found id");
                // Replace the line with the new line
                content.append(replacementLine).append(System.lineSeparator());
                found = 1;
            } else {
                // Keep the original line
                content.append(line).append(System.lineSeparator());
            }
        }
        reader.close();

        // Write the updated content back to the file
        BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile));
        writer.write(content.toString());
        writer.close();
        if (found == 1) System.out.println("record replaced successfully.");
        return found;
    }

    public int delete(String id) throws IOException {
        File inputFile = new File(HOME_EMP);
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        StringBuilder content = new StringBuilder();
        String line;
        int found = -1;
        while ((line = reader.readLine()) != null) {
            // Check if the line matches the line to be replaced
            if (line.contains(id)) {
                System.out.println("found id");
                found = 1;
                // Replace the line with the new line
            } else {
                // Keep the original line
                content.append(line).append(System.lineSeparator());
            }
        }
        reader.close();

        // Write the updated content back to the file
        BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile));
        writer.write(content.toString());
        writer.close();
        System.out.println(found);
        if (found == 1) System.out.println("record replaced successfully.");
        return found;
    }
}


