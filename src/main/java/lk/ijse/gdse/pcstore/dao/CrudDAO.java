package lk.ijse.gdse.pcstore.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T> extends SuperDAO{
    String getNextId() throws SQLException;
    ArrayList<T> getAll() throws SQLException;
    boolean save(T dto) throws SQLException;
    boolean update(T dto) throws SQLException;
    boolean delete(String id) throws SQLException;
    ArrayList<String> getAllIds() throws SQLException;
    ArrayList<String> getAllNames() throws SQLException;
    T findById(String id) throws SQLException;
    T findByName(String name) throws SQLException;
}
