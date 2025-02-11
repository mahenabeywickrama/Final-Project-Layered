package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBO extends SuperBO {
    String getNextId() throws SQLException;
    ArrayList<CustomerDTO> getAll() throws SQLException;
    boolean save(CustomerDTO customerDTO) throws SQLException;
    boolean update(CustomerDTO customerDTO) throws SQLException;
    boolean delete(String id) throws SQLException;
    ArrayList<String> getAllIds() throws SQLException;
    ArrayList<String> getAllNames() throws SQLException;
    CustomerDTO findById(String id) throws SQLException;
    CustomerDTO findByName(String name) throws SQLException;
}
