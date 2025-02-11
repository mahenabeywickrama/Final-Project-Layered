package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.BOFactory;
import lk.ijse.gdse.pcstore.bo.custom.OrdersRepairBO;
import lk.ijse.gdse.pcstore.bo.custom.RepairBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.OrdersRepairDAO;
import lk.ijse.gdse.pcstore.dao.custom.RepairDAO;
import lk.ijse.gdse.pcstore.dto.OrdersRepairDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersRepairBOImpl implements OrdersRepairBO {

    OrdersRepairDAO ordersRepairDAO = (OrdersRepairDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDERS_REPAIR);
    RepairBO repairBO = (RepairBO) BOFactory.getInstance().getBO(BOFactory.BOType.REPAIR);

    @Override
    public boolean addDetails(ArrayList<OrdersRepairDTO> ordersRepairDTOS) throws SQLException {
        for (OrdersRepairDTO ordersRepairDTO : ordersRepairDTOS) {
            boolean isRepairDetailsSaved = saveRepairDetails(ordersRepairDTO);
            if(!isRepairDetailsSaved) {
                return false;
            }

            boolean isRepairUpdated = repairBO.updateRepairForOrders(ordersRepairDTO);
            if(!isRepairUpdated) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveRepairDetails(OrdersRepairDTO ordersRepairDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into orders_repair values(?,?,?)",
                ordersRepairDTO.getOrderId(),
                ordersRepairDTO.getRepairId(),
                ordersRepairDTO.getRepairPrice()
        );
    }
}
