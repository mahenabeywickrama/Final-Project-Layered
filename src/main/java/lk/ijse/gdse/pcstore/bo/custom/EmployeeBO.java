package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.dao.SuperDAO;
import lk.ijse.gdse.pcstore.dto.EmployeeDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface EmployeeBO extends SuperDAO {
    String getNextId() throws SQLException;
    ArrayList<EmployeeDTO> getAll() throws SQLException;
    boolean save(EmployeeDTO dto) throws SQLException;
    boolean update(EmployeeDTO dto) throws SQLException;
    boolean delete(String id) throws SQLException;
    ArrayList<String> getAllIds() throws SQLException;
    ArrayList<String> getAllNames() throws SQLException;
    EmployeeDTO findById(String id) throws SQLException;
    EmployeeDTO findByName(String name) throws SQLException;
}
