package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.ReplacementDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ReplacementModel {
    public String getNextReplacementId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select replacement_id from replacement order by replacement_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("R%03d", newIdIndex);
        }
        return "R001";
    }

    public boolean saveReplacement(ReplacementDTO replacementDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into replacement values (?,?,?,?,?,?,?,?)",
                replacementDTO.getReplacementId(),
                replacementDTO.getOrdersId(),
                replacementDTO.getFaultyItemId(),
                replacementDTO.getReplacedItemId(),
                replacementDTO.getQty(),
                replacementDTO.getDate(),
                replacementDTO.getTime(),
                replacementDTO.getRemarks()
        );
    }

    public ArrayList<ReplacementDTO> getAllReplacements() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from replacement");

        ArrayList<ReplacementDTO> replacementDTOS = new ArrayList<>();

        while (rst.next()) {
            ReplacementDTO replacementDTO = new ReplacementDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    Integer.parseInt(rst.getString(5)),
                    LocalDate.parse(rst.getString(6)),
                    LocalTime.parse(rst.getString(7)),
                    rst.getString(8)
            );
            replacementDTOS.add(replacementDTO);
        }
        return replacementDTOS;
    }

    public boolean updateReplacement(ReplacementDTO replacementDTO) throws SQLException {
        return SQLUtil.execute(
                "update replacement set orders_id=?, faulty_item_id=?, replacement_item_id=?, qty=?, date=?, time=?, remarks=? where replacement_id=?",
                replacementDTO.getOrdersId(),
                replacementDTO.getFaultyItemId(),
                replacementDTO.getReplacedItemId(),
                replacementDTO.getQty(),
                replacementDTO.getDate(),
                replacementDTO.getTime(),
                replacementDTO.getRemarks(),
                replacementDTO.getReplacementId()
        );
    }

    public boolean deleteReplacement(String replacementId) throws SQLException {
        return SQLUtil.execute("delete from replacement where replacement_id=?", replacementId);
    }
}
