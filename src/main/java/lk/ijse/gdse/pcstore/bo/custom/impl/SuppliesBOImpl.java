package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.custom.SuppliesBO;
import lk.ijse.gdse.pcstore.bo.custom.SuppliesItemBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.SuppliesDAO;
import lk.ijse.gdse.pcstore.dao.custom.impl.SuppliesDAOImpl;
import lk.ijse.gdse.pcstore.db.DBConnection;
import lk.ijse.gdse.pcstore.dto.SuppliesDTO;
import lk.ijse.gdse.pcstore.entity.Supplies;

import java.sql.Connection;
import java.sql.SQLException;

public class SuppliesBOImpl implements SuppliesBO {

    SuppliesDAO suppliesDAO = (SuppliesDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SUPPLIES);
    SuppliesItemBO suppliesItemBO = new SuppliesItemBOImpl();

    @Override
    public String getNextSuppliesId() throws SQLException {
        return suppliesDAO.getNextId();
    }

    @Override
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
                boolean isSuppliesDetailListSaved = suppliesItemBO.saveSuppliesDetailsList(suppliesDTO.getSuppliesItemDTOS());
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
