package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.OrdersRepairDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrdersRepairBO extends SuperBO {
    boolean addDetails(ArrayList<OrdersRepairDTO> ordersRepairDTOS) throws SQLException;
    boolean saveRepairDetails(OrdersRepairDTO ordersRepairDTO) throws SQLException;
}
