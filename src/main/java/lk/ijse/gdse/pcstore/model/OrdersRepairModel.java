package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.OrdersRepairDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersRepairModel {
    RepairModel repairModel = new RepairModel();

    public OrdersRepairDTO searchRepairId(String repairId) throws SQLException {
        ResultSet rst= SQLUtil.execute("select * from orders_repair where repair_id = ?",repairId);
        if (rst.next()){
            return new OrdersRepairDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3)
            );
        }
        return null;
    }

    public boolean addDetails(ArrayList<OrdersRepairDTO> ordersRepairDTOS) throws SQLException {
        for (OrdersRepairDTO ordersRepairDTO : ordersRepairDTOS) {
            boolean isRepairDetailsSaved = saveRepairDetails(ordersRepairDTO);
            if(!isRepairDetailsSaved) {
                return false;
            }

            boolean isRepairUpdated = repairModel.updateRepairForOrders(ordersRepairDTO);
            if(!isRepairUpdated) {
                return false;
            }
        }
        return true;
    }

    public boolean saveRepairDetails(OrdersRepairDTO ordersRepairDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into orders_repair values(?,?,?)",
                ordersRepairDTO.getOrderId(),
                ordersRepairDTO.getRepairId(),
                ordersRepairDTO.getRepairPrice()
        );
    }

    public ArrayList<OrdersRepairDTO> search(String ordersId) throws SQLException {
        ArrayList<OrdersRepairDTO> ordersRepairDTOS = new ArrayList<>();
        ResultSet rst= SQLUtil.execute("select * from orders_repair where orders_id = ?",ordersId);

        while (rst.next()){
            ordersRepairDTOS.add(new OrdersRepairDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3)
            ));
        }
        return ordersRepairDTOS;
    }
}
