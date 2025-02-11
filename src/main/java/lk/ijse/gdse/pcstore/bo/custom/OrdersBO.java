package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.DashBoardDTO;
import lk.ijse.gdse.pcstore.dto.OrdersDTO;
import lk.ijse.gdse.pcstore.dto.OrdersItemDTO;
import lk.ijse.gdse.pcstore.dto.OrdersRepairDTO;
import lk.ijse.gdse.pcstore.entity.OrdersItem;
import lk.ijse.gdse.pcstore.entity.OrdersRepair;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrdersBO extends SuperBO {
    String getNextOrderId() throws SQLException;
    boolean saveOrder(OrdersDTO ordersDTO) throws SQLException;
    ArrayList<OrdersDTO> getAllOrders() throws SQLException;
    ArrayList<OrdersItemDTO> getAllOrderItems(ArrayList<OrdersItem> ordersItems) throws SQLException;
    ArrayList<OrdersRepairDTO> getAllOrderRepairs(ArrayList<OrdersRepair> ordersRepairs) throws SQLException;
    ArrayList<String> getAllOrdersIds() throws SQLException;
    ArrayList<String> getAllOrdersIdsPaid() throws SQLException;
    String findOrdersCustomer(String selectedOrdersId) throws SQLException;
    String findOrdersEmployee(String selectedOrdersId) throws SQLException;
    String findOrdersPrice(String selectedOrdersId) throws SQLException;
    String findOrdersDate(String selectedOrdersId) throws SQLException;
    boolean updateOrdersPaid(String ordersId) throws SQLException;
    boolean updateOrdersPending(String ordersId) throws SQLException;
}
