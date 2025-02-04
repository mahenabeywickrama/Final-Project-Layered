package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.db.DBConnection;
import lk.ijse.gdse.pcstore.dto.SuppliesDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SuppliesModel {
    private final SuppliesItemModel suppliesItemModel = new SuppliesItemModel();

    public String getNextSuppliesId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select supplies_id from supplies order by supplies_id desc limit 1");
        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("S%03d", newIdIndex);
        }

        return "S001";
    }

    public boolean saveSupplies(SuppliesDTO suppliesDTO) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            boolean isSuppliesSaved = SQLUtil.execute(
                    "insert into supplies values (?,?,?,?)",
                    suppliesDTO.getSuppliesId(),
                    suppliesDTO.getSupplierId(),
                    suppliesDTO.getDate(),
                    suppliesDTO.getTime()
            );
            if (isSuppliesSaved) {
                boolean isSuppliesDetailListSaved = suppliesItemModel.saveSuppliesDetailsList(suppliesDTO.getSuppliesItemDTOS());
                if (isSuppliesDetailListSaved) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
