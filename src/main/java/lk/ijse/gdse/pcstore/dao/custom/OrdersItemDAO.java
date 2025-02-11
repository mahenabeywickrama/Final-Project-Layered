package lk.ijse.gdse.pcstore.dao.custom;

import lk.ijse.gdse.pcstore.dao.CrudDAO;
import lk.ijse.gdse.pcstore.entity.OrdersItem;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrdersItemDAO extends CrudDAO<OrdersItem> {
    ArrayList<String> getAllItemsFromOrders(String selectedOrdersId) throws SQLException;
    String findQty(String selectedItemId) throws SQLException;
}
