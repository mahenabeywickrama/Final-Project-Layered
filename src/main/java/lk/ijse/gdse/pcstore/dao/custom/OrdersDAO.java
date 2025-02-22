package lk.ijse.gdse.pcstore.dao.custom;

import lk.ijse.gdse.pcstore.dao.CrudDAO;
import lk.ijse.gdse.pcstore.entity.Orders;
import lk.ijse.gdse.pcstore.entity.OrdersItem;
import lk.ijse.gdse.pcstore.entity.OrdersRepair;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrdersDAO extends CrudDAO<Orders> {
    ArrayList<OrdersItem> getAllOrderItems(String id) throws SQLException;
    ArrayList<OrdersRepair> getAllOrderRepairs(String id) throws SQLException;
    ArrayList<String> getAllOrdersIds() throws SQLException;
    ArrayList<String> getAllOrdersIdsPaid() throws SQLException;
    String findOrdersCustomer(String selectedOrdersId) throws SQLException;
    String findOrdersEmployee(String selectedOrdersId) throws SQLException;
    String findOrdersPrice(String selectedOrdersId) throws SQLException;
    String findOrdersDate(String selectedOrdersId) throws SQLException;
    boolean updateOrdersPaid(String ordersId) throws SQLException;
    boolean updateOrdersPending(String ordersId) throws SQLException;
}
