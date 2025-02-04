package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.EmployeeDAO;
import lk.ijse.gdse.pcstore.dto.EmployeeDTO;
import lk.ijse.gdse.pcstore.entity.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDAOImpl implements EmployeeDAO {
    @Override
    public String getNextId() throws SQLException {
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

    @Override
    public ArrayList<Employee> getAll() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from employee");

        ArrayList<Employee> employees = new ArrayList<>();

        while (rst.next()) {
            Employee employee = new Employee(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
            employees.add(employee);
        }
        return employees;
    }

    @Override
    public boolean save(Employee employee) throws SQLException {
        return SQLUtil.execute(
                "insert into employee values (?,?,?,?,?,?)",
                employee.getEmployeeId(),
                employee.getEmployeeName(),
                employee.getEmployeeAddress(),
                employee.getEmployeeNic(),
                employee.getEmployeePhone(),
                employee.getEmployeeRole()
        );
    }

    @Override
    public boolean update(Employee employee) throws SQLException {
        return SQLUtil.execute(
                "update employee set name=?, address = ?, nic=?, phone=?, role=? where employee_id=?",
                employee.getEmployeeName(),
                employee.getEmployeeAddress(),
                employee.getEmployeeNic(),
                employee.getEmployeePhone(),
                employee.getEmployeeRole(),
                employee.getEmployeeId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return SQLUtil.execute("delete from employee where employee_id=?", id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select employee_id from employee");

        ArrayList<String> employeeIds = new ArrayList<>();

        while (rst.next()) {
            employeeIds.add(rst.getString(1));
        }

        return employeeIds;
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        ResultSet rst = SQLUtil.execute("select name from employee");

        ArrayList<String> employeeNames = new ArrayList<>();

        while (rst.next()) {
            employeeNames.add(rst.getString(1));
        }

        return employeeNames;
    }

    @Override
    public Employee findById(String id) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from employee where employee_id = ?", id);

        if (rst.next()) {
            return new Employee(
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

    @Override
    public Employee findByName(String name) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from employee where name = ?", name);

        if (rst.next()) {
            return new Employee(
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
