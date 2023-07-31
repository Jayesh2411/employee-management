package ai.sahaj.nano.storage;

import java.io.*;

public class EmployeeID {
    private final String EMP_ID_TXT;
    private static Long empId;

    public EmployeeID(String EMP_ID_TXT) {
        this.EMP_ID_TXT = EMP_ID_TXT;
    }

    public Long getNewEmpId() {
        File f = new File(this.EMP_ID_TXT);
        if (empId == null) {
            if (f.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(EMP_ID_TXT))) {
                    String line;
                    line = br.readLine();
                    empId = Long.parseLong(line.trim());
                } catch (Exception e) {
                    System.out.println("Some Issue reading file");
                }
            } else {
                empId = 0L;
            }
        }
        empId = empId + 1;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
            // Write data to the file
            writer.write(String.valueOf(empId));
        } catch (IOException er) {
            System.out.println("Error writing data");
        }
        return empId;
    }

}
