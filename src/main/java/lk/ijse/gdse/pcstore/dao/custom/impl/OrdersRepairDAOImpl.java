package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.custom.OrdersRepairDAO;
import lk.ijse.gdse.pcstore.entity.OrdersRepair;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersRepairDAOImpl implements OrdersRepairDAO {
    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public ArrayList<OrdersRepair> getAll() throws SQLException {
        return null;
    }

    @Override
    public boolean save(OrdersRepair dto) throws SQLException {
        return false;
    }

    @Override
    public boolean update(OrdersRepair dto) throws SQLException {
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
    public OrdersRepair findById(String id) throws SQLException {
        return null;
    }

    @Override
    public OrdersRepair findByName(String name) throws SQLException {
        return null;
    }
}
