package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.OrdersRepairDTO;
import lk.ijse.gdse.pcstore.dto.RepairDTO;
import lk.ijse.gdse.pcstore.entity.Repair;

import java.sql.SQLException;
import java.util.ArrayList;

public interface RepairBO extends SuperBO {
    String getNextId() throws SQLException;
    ArrayList<RepairDTO> getAll() throws SQLException;
    boolean save(RepairDTO repair) throws SQLException;
    boolean update(RepairDTO repair) throws SQLException;
    boolean delete(String id) throws SQLException;
    ArrayList<String> getAllIds() throws SQLException;
    ArrayList<String> getAllNames() throws SQLException;
    RepairDTO findById(String id) throws SQLException;
    String searchRepairId(String repairId) throws SQLException;
    boolean updateRepairForOrders(OrdersRepairDTO ordersRepairDTO) throws SQLException;
}
