package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.EmployeeDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {
    public String getNextEmployeeId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select employee_id from employee order by employee_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("E%03d", newIdIndex);
        }

        return "E001";
    }

    public boolean saveEmployee(EmployeeDTO employeeDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into employee values (?,?,?,?,?,?)",
                employeeDTO.getEmployeeId(),
                employeeDTO.getEmployeeName(),
                employeeDTO.getEmployeeAddress(),
                employeeDTO.getEmployeeNic(),
                employeeDTO.getEmployeePhone(),
                employeeDTO.getEmployeeRole()
        );
    }

    public ArrayList<EmployeeDTO> getAllEmployees() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from employee");

        ArrayList<EmployeeDTO> employeeDTOS = new ArrayList<>();

        while (rst.next()) {
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
            employeeDTOS.add(employeeDTO);
        }
        return employeeDTOS;
    }

    public boolean updateEmployee(EmployeeDTO employeeDTO) throws SQLException {
        return SQLUtil.execute(
                "update employee set name=?, address = ?, nic=?, phone=?, role=? where employee_id=?",
                employeeDTO.getEmployeeName(),
                employeeDTO.getEmployeeAddress(),
                employeeDTO.getEmployeeNic(),
                employeeDTO.getEmployeePhone(),
                employeeDTO.getEmployeeRole(),
                employeeDTO.getEmployeeId()
        );
    }

    public boolean deleteEmployee(String employeeId) throws SQLException {
        return SQLUtil.execute("delete from employee where employee_id=?", employeeId);
    }

    public ArrayList<String> getAllEmployeeIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select employee_id from employee");

        ArrayList<String> employeeIds = new ArrayList<>();

        while (rst.next()) {
            employeeIds.add(rst.getString(1));
        }

        return employeeIds;
    }

    public ArrayList<String> getAllEmployeeNames() throws SQLException {
        ResultSet rst = SQLUtil.execute("select name from employee");

        ArrayList<String> employeeNames = new ArrayList<>();

        while (rst.next()) {
            employeeNames.add(rst.getString(1));
        }

        return employeeNames;
    }

    public EmployeeDTO findById(String selectedEmId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from employee where employee_id = ?", selectedEmId);

        if (rst.next()) {
            return new EmployeeDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
        }
        return null;
    }

    public EmployeeDTO findByName(String selectedEmName) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from employee where name = ?", selectedEmName);

        if (rst.next()) {
            return new EmployeeDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
        }
        return null;
    }
}
