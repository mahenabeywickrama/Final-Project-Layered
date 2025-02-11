package lk.ijse.gdse.pcstore.dao.custom;

import lk.ijse.gdse.pcstore.dao.CrudDAO;
import lk.ijse.gdse.pcstore.dto.OrdersRepairDTO;
import lk.ijse.gdse.pcstore.entity.OrdersRepair;
import lk.ijse.gdse.pcstore.entity.Repair;

import java.sql.SQLException;

public interface RepairDAO extends CrudDAO<Repair> {
    String searchRepairId(String repairId) throws SQLException;
    boolean updateRepairForOrders(OrdersRepair ordersRepair) throws SQLException;
}
