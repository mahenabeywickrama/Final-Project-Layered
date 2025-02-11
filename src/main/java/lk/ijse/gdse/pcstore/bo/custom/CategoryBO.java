package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.CategoryDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CategoryBO extends SuperBO {
    String getNextId() throws SQLException;
    ArrayList<CategoryDTO> getAll() throws SQLException;
    boolean save(CategoryDTO dto) throws SQLException;
    boolean update(CategoryDTO dto) throws SQLException;
    boolean delete(String id) throws SQLException;
    ArrayList<String> getAllIds() throws SQLException;
    ArrayList<String> getAllNames() throws SQLException;
    CategoryDTO findById(String id) throws SQLException;
    CategoryDTO findByName(String name) throws SQLException;
}
