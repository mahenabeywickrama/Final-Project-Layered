package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.custom.OrdersItemDAO;
import lk.ijse.gdse.pcstore.entity.OrdersItem;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersItemDAOImpl implements OrdersItemDAO {
    @Override
    public String getNextId() throws SQLException {
        return "";
    }

    @Override
    public ArrayList<OrdersItem> getAll() throws SQLException {
        return null;
    }

    @Override
    public boolean save(OrdersItem dto) throws SQLException {
        return false;
    }

    @Override
    public boolean update(OrdersItem dto) throws SQLException {
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
    public OrdersItem findById(String id) throws SQLException {
        return null;
    }

    @Override
    public OrdersItem findByName(String name) throws SQLException {
        return null;
    }
}
