package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.OrdersItemDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersItemModel {
    private final ItemModel itemModel = new ItemModel();

    public boolean saveOrderDetailsList(ArrayList<OrdersItemDTO> ordersItemDTOS) throws SQLException {
        for (OrdersItemDTO ordersItemDTO : ordersItemDTOS) {
            boolean isOrderDetailsSaved = saveOrderDetail(ordersItemDTO);
            if (!isOrderDetailsSaved) {
                return false;
            }

            boolean isItemUpdated = itemModel.reduceQty(ordersItemDTO);
            if (!isItemUpdated) {
                return false;
            }
        }
        return true;
    }

    private boolean saveOrderDetail(OrdersItemDTO ordersItemDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into orders_item values (?,?,?,?)",
                ordersItemDTO.getOrderId(),
                ordersItemDTO.getItemId(),
                ordersItemDTO.getQuantity(),
                ordersItemDTO.getPrice()
        );
    }

    public ArrayList<String> getAllItemsFromOrders(String selectedOrdersId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select item_id from orders_item where orders_id=?", selectedOrdersId);

        ArrayList<String> itemIds = new ArrayList<>();

        while (rst.next()) {
            itemIds.add(rst.getString(1));
        }

        return itemIds;
    }

    public String findQty(String selectedItemId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select qty from orders_item where item_id = ?", selectedItemId);

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
