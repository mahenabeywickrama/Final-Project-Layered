package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.custom.SuppliesItemDAO;
import lk.ijse.gdse.pcstore.entity.SuppliesItem;

import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliesItemDAOImpl implements SuppliesItemDAO {
    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public ArrayList<SuppliesItem> getAll() throws SQLException {
        return null;
    }

    @Override
    public boolean save(SuppliesItem dto) throws SQLException {
        return false;
    }

    @Override
    public boolean update(SuppliesItem dto) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        return null;
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        return null;
    }

    @Override
    public SuppliesItem findById(String id) throws SQLException {
        return null;
    }

    @Override
    public SuppliesItem findByName(String name) throws SQLException {
        return null;
    }
}
