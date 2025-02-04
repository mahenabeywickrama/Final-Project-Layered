package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.OrdersRepairDTO;
import lk.ijse.gdse.pcstore.dto.RepairDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RepairModel {
    public String getNextRepairId() throws SQLException {
        ResultSet rst= SQLUtil.execute("select repair_id from repair order by repair_id desc limit 1");
        if(rst.next()){
            return String.format("R%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "R001";
    }

    public ArrayList<RepairDTO> getAllRepair() throws SQLException {
        ResultSet rst= SQLUtil.execute("select * from repair");
        ArrayList<RepairDTO> repairDTOS = new ArrayList<>();

        while (rst.next()){
            RepairDTO repairDTO = new RepairDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    LocalDate.parse(rst.getString(5)),
                    LocalDate.parse(rst.getString(6))
            );
            repairDTOS.add(repairDTO);
        }
        return repairDTOS;
    }

    public boolean deleteRepair(String repairId) throws SQLException {
        return SQLUtil.execute("delete from repair where repair_id=?",repairId);
    }

    public boolean saveRepair(RepairDTO repairDTO) throws SQLException {
        return SQLUtil.execute("insert into repair values(?,?,?,?,?,?)",
                repairDTO.getRepairId(),
                repairDTO.getCustomerId(),
                repairDTO.getDescription(),
                repairDTO.getStatus(),
                repairDTO.getReceiveDate(),
                repairDTO.getReturnDate()
        );
    }

    public boolean updateRepair(RepairDTO repairDTO) throws SQLException {
        return SQLUtil.execute("update repair set customer_id = ?, description = ?, status = ?, received_date = ?, returned_date = ? where repair_id = ?",
                repairDTO.getCustomerId(),
                repairDTO.getDescription(),
                repairDTO.getStatus(),
                repairDTO.getReceiveDate(),
                repairDTO.getReturnDate(),
                repairDTO.getRepairId()
        );
    }

    public ArrayList<String> getAllRepairIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select repair_id from repair where status = 'REPAIRING'");
        ArrayList<String> repairIds = new ArrayList<>();

        while (rst.next()) {
            repairIds.add(rst.getString(1));
        }

        return repairIds;
    }

    public RepairDTO findById(String selectedRepairId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from repair where repair_id = ?", selectedRepairId);

        if (rst.next()) {
            LocalDate receivedDate = LocalDate.parse(rst.getString(5));
            LocalDate returnDate = LocalDate.parse(rst.getString(6));

            return new RepairDTO(
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

    public String searchRepairId(String repairId) throws SQLException {
        ResultSet rst= SQLUtil.execute("select customer_id from repair where repair_id = ?", repairId);
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public boolean updateRepairForOrders(OrdersRepairDTO ordersRepairDTO) throws SQLException {
        return SQLUtil.execute(
                "update repair set status = ?, returned_date = ? where repair_id = ?",
                "RETURNED",
                LocalDate.now(),
                ordersRepairDTO.getRepairId()
        );
    }
}
