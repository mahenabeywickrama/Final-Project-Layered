package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.BOFactory;
import lk.ijse.gdse.pcstore.bo.custom.ItemBO;
import lk.ijse.gdse.pcstore.bo.custom.OrdersItemBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.OrdersItemDAO;
import lk.ijse.gdse.pcstore.dto.OrdersItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersItemBOImpl implements OrdersItemBO {

    OrdersItemDAO ordersItemDAO = (OrdersItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDERS_ITEM);
    ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.ITEM);

    @Override
    public boolean saveOrderDetailsList(ArrayList<OrdersItemDTO> ordersItemDTOS) throws SQLException {
        for (OrdersItemDTO ordersItemDTO : ordersItemDTOS) {
            boolean isOrderDetailsSaved = saveOrderDetail(ordersItemDTO);
            if (!isOrderDetailsSaved) {
                return false;
            }

            boolean isItemUpdated = itemBO.reduceQty(ordersItemDTO);
            if (!isItemUpdated) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveOrderDetail(OrdersItemDTO ordersItemDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into orders_item values (?,?,?,?)",
                ordersItemDTO.getOrderId(),
                ordersItemDTO.getItemId(),
                ordersItemDTO.getQuantity(),
                ordersItemDTO.getPrice()
        );
    }

    @Override
    public ArrayList<String> getAllItemsFromOrders(String selectedOrdersId) throws SQLException {
        return ordersItemDAO.getAllItemsFromOrders(selectedOrdersId);
    }

    @Override
    public String findQty(String selectedItemId) throws SQLException {
        return ordersItemDAO.findQty(selectedItemId);
    }
}
