package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.OrdersItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrdersItemBO extends SuperBO {
    boolean saveOrderDetailsList(ArrayList<OrdersItemDTO> ordersItemDTOS) throws SQLException;
    boolean saveOrderDetail(OrdersItemDTO ordersItemDTO) throws SQLException;
    ArrayList<String> getAllItemsFromOrders(String selectedOrdersId) throws SQLException;
    String findQty(String selectedItemId) throws SQLException;
}
