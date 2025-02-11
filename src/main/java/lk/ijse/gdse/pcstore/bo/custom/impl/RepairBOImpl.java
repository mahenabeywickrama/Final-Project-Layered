package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.custom.RepairBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.custom.RepairDAO;
import lk.ijse.gdse.pcstore.dao.custom.impl.RepairDAOImpl;
import lk.ijse.gdse.pcstore.dto.OrdersRepairDTO;
import lk.ijse.gdse.pcstore.dto.RepairDTO;
import lk.ijse.gdse.pcstore.entity.OrdersRepair;
import lk.ijse.gdse.pcstore.entity.Repair;

import java.sql.SQLException;
import java.util.ArrayList;

public class RepairBOImpl implements RepairBO {

    RepairDAO repairDAO = (RepairDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.REPAIR);

    @Override
    public String getNextId() throws SQLException {
        return repairDAO.getNextId();
    }

    @Override
    public ArrayList<RepairDTO> getAll() throws SQLException {
        ArrayList<RepairDTO> repairDTOS = new ArrayList<>();
        ArrayList<Repair> repairs = repairDAO.getAll();

        for (Repair repair : repairs) {
            RepairDTO repairDTO = new RepairDTO(repair.getRepairId(), repair.getCustomerId(), repair.getDescription(), repair.getStatus(), repair.getReceiveDate(), repair.getReturnDate());
            repairDTOS.add(repairDTO);
        }
        return repairDTOS;
    }

    @Override
    public boolean save(RepairDTO repair) throws SQLException {
        return repairDAO.save(new Repair(repair.getRepairId(), repair.getCustomerId(), repair.getDescription(), repair.getStatus(), repair.getReceiveDate(), repair.getReturnDate()));
    }

    @Override
    public boolean update(RepairDTO repair) throws SQLException {
        return repairDAO.update(new Repair(repair.getRepairId(), repair.getCustomerId(), repair.getDescription(), repair.getStatus(), repair.getReceiveDate(), repair.getReturnDate()));
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return repairDAO.delete(id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        return repairDAO.getAllIds();
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        return repairDAO.getAllNames();
    }

    @Override
    public RepairDTO findById(String id) throws SQLException {
        Repair repair = repairDAO.findById(id);
        return new RepairDTO(repair.getRepairId(), repair.getCustomerId(), repair.getDescription(), repair.getStatus(), repair.getReceiveDate(), repair.getReturnDate());
    }

    @Override
    public String searchRepairId(String repairId) throws SQLException {
        return repairDAO.searchRepairId(repairId);
    }

    @Override
    public boolean updateRepairForOrders(OrdersRepairDTO ordersRepairDTO) throws SQLException {
        OrdersRepair ordersRepair = new OrdersRepair(ordersRepairDTO.getOrderId(), ordersRepairDTO.getRepairId(), ordersRepairDTO.getRepairPrice());
        return repairDAO.updateRepairForOrders(ordersRepair);
    }
}
