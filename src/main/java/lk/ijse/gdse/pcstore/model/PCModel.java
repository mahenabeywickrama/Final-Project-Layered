package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.db.DBConnection;
import lk.ijse.gdse.pcstore.dto.PCDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PCModel {
    private final PCItemModel pcItem = new PCItemModel();

    public String getNextPCId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select pc_id from pc order by pc_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("P%03d", newIdIndex);
        }
        return "P001";
    }

    public boolean savePC(PCDTO pcdto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            boolean isPCSaved = SQLUtil.execute(
                    "insert into pc values (?,?,?)",
                    pcdto.getPcId(),
                    pcdto.getDescription(),
                    pcdto.getTotalCost()
            );
            if (isPCSaved) {
                boolean isPCItemListSaved = pcItem.savePCItemList(pcdto.getPcItemDTOS());

                if (isPCItemListSaved) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (SQLException e) {
            connection.rollback();
            System.out.println(e.getMessage());
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public boolean updatePC(PCDTO pcdto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            boolean isPCUpdated = SQLUtil.execute(
                    "update pc set description = ?, total_cost = ? where pc_id = ?",
                    pcdto.getDescription(),
                    pcdto.getTotalCost(),
                    pcdto.getPcId()
            );
            if (isPCUpdated) {
                boolean isPCItemListUpdated = pcItem.updatePCItemList(pcdto.getPcItemDTOS());

                if (isPCItemListUpdated) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (SQLException e) {
            connection.rollback();
            System.out.println(e.getMessage());
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public boolean deletePC(String pcId) throws SQLException {
        return SQLUtil.execute("delete from pc where pc_id=?", pcId);
    }

    public ArrayList<PCDTO> getAllPCs() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from pc");

        ArrayList<PCDTO> pcdtos = new ArrayList<>();

        while (rst.next()) {
            PCDTO pcdto = new PCDTO(
                    rst.getString(1),
                    rst.getString(2),
                    Double.parseDouble(rst.getString(3)),
                    pcItem.findById(rst.getString(1))
            );
            pcdtos.add(pcdto);
        }
        return pcdtos;
    }

    public ArrayList<String> getAllPCIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select pc_id from pc");

        ArrayList<String> pcIds = new ArrayList<>();

        while (rst.next()) {
            pcIds.add(rst.getString(1));
        }

        return pcIds;
    }
}
