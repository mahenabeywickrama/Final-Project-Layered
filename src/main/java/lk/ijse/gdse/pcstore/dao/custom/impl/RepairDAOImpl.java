package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.RepairDAO;
import lk.ijse.gdse.pcstore.dto.OrdersRepairDTO;
import lk.ijse.gdse.pcstore.dto.RepairDTO;
import lk.ijse.gdse.pcstore.entity.OrdersRepair;
import lk.ijse.gdse.pcstore.entity.Repair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RepairDAOImpl implements RepairDAO {
    @Override
    public String getNextId() throws SQLException {
        ResultSet rst= SQLUtil.execute("select repair_id from repair order by repair_id desc limit 1");
        if(rst.next()){
            return String.format("R%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "R001";
    }

    @Override
    public ArrayList<Repair> getAll() throws SQLException {
        ResultSet rst= SQLUtil.execute("select * from repair");
        ArrayList<Repair> repairs = new ArrayList<>();

        while (rst.next()){
            Repair repair = new Repair(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    LocalDate.parse(rst.getString(5)),
                    LocalDate.parse(rst.getString(6))
            );
            repairs.add(repair);
        }
        return repairs;
    }

    @Override
    public boolean save(Repair repair) throws SQLException {
        return SQLUtil.execute("insert into repair values(?,?,?,?,?,?)",
                repair.getRepairId(),
                repair.getCustomerId(),
                repair.getDescription(),
                repair.getStatus(),
                repair.getReceiveDate(),
                repair.getReturnDate()
        );
    }

    @Override
    public boolean update(Repair repair) throws SQLException {
        return SQLUtil.execute("update repair set customer_id = ?, description = ?, status = ?, received_date = ?, returned_date = ? where repair_id = ?",
                repair.getCustomerId(),
                repair.getDescription(),
                repair.getStatus(),
                repair.getReceiveDate(),
                repair.getReturnDate(),
                repair.getRepairId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return SQLUtil.execute("delete from repair where repair_id=?",id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select repair_id from repair where status = 'REPAIRING'");
        ArrayList<String> repairIds = new ArrayList<>();

        while (rst.next()) {
            repairIds.add(rst.getString(1));
        }

        return repairIds;
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        return null;
    }

    @Override
    public Repair findById(String id) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from repair where repair_id = ?", id);

        if (rst.next()) {
            LocalDate receivedDate = LocalDate.parse(rst.getString(5));
            LocalDate returnDate = LocalDate.parse(rst.getString(6));

            return new Repair(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    receivedDate,
                    returnDate
            );
        }
        return null;
    }

    @Override
    public Repair findByName(String name) throws SQLException {
        return null;
    }

    @Override
    public String searchRepairId(String repairId) throws SQLException {
        ResultSet rst= SQLUtil.execute("select customer_id from repair where repair_id = ?", repairId);
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    @Override
    public boolean updateRepairForOrders(OrdersRepair ordersRepair) throws SQLException {
        return SQLUtil.execute(
                "update repair set status = ?, returned_date = ? where repair_id = ?",
                "RETURNED",
                LocalDate.now(),
                ordersRepair.getRepairId()
        );
    }
}
