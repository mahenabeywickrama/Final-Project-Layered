package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.custom.EmployeeBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.custom.EmployeeDAO;
import lk.ijse.gdse.pcstore.dto.CustomerDTO;
import lk.ijse.gdse.pcstore.dto.EmployeeDTO;
import lk.ijse.gdse.pcstore.entity.Customer;
import lk.ijse.gdse.pcstore.entity.Employee;

import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeBOImpl implements EmployeeBO {

    EmployeeDAO employeeDAO = (EmployeeDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.EMPLOYEE);

    @Override
    public String getNextId() throws SQLException {
        return employeeDAO.getNextId();
    }

    @Override
    public ArrayList<EmployeeDTO> getAll() throws SQLException {
        ArrayList<EmployeeDTO> employeeDTOS = new ArrayList<>();
        ArrayList<Employee> employees = employeeDAO.getAll();
        for (Employee employee : employees) {
            employeeDTOS.add(new EmployeeDTO(employee.getEmployeeId(), employee.getEmployeeName(), employee.getEmployeeAddress(), employee.getEmployeeNic(), employee.getEmployeePhone(), employee.getEmployeeRole()));
        }
        return employeeDTOS;
    }

    @Override
    public boolean save(EmployeeDTO employeeDTO) throws SQLException {
        return employeeDAO.save(new Employee(employeeDTO.getEmployeeId(), employeeDTO.getEmployeeName(), employeeDTO.getEmployeeAddress(), employeeDTO.getEmployeeNic(), employeeDTO.getEmployeePhone(), employeeDTO.getEmployeeRole()));
    }

    @Override
    public boolean update(EmployeeDTO employeeDTO) throws SQLException {
        return employeeDAO.update(new Employee(employeeDTO.getEmployeeId(), employeeDTO.getEmployeeName(), employeeDTO.getEmployeeAddress(), employeeDTO.getEmployeeNic(), employeeDTO.getEmployeePhone(), employeeDTO.getEmployeeRole()));
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return employeeDAO.delete(id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        return employeeDAO.getAllIds();
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        return employeeDAO.getAllNames();
    }

    @Override
    public EmployeeDTO findById(String id) throws SQLException {
        Employee employee = employeeDAO.findById(id);
        return new EmployeeDTO(employee.getEmployeeId(), employee.getEmployeeName(), employee.getEmployeeAddress(), employee.getEmployeeNic(), employee.getEmployeePhone(), employee.getEmployeeRole());
    }

    @Override
    public EmployeeDTO findByName(String name) throws SQLException {
        Employee employee = employeeDAO.findByName(name);
        return new EmployeeDTO(employee.getEmployeeId(), employee.getEmployeeName(), employee.getEmployeeAddress(), employee.getEmployeeNic(), employee.getEmployeePhone(), employee.getEmployeeRole());
    }
}
