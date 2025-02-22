package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.BOFactory;
import lk.ijse.gdse.pcstore.bo.custom.ItemBO;
import lk.ijse.gdse.pcstore.bo.custom.SuppliesBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.SuppliesDAO;
import lk.ijse.gdse.pcstore.db.DBConnection;
import lk.ijse.gdse.pcstore.dto.SuppliesDTO;
import lk.ijse.gdse.pcstore.dto.SuppliesItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliesBOImpl implements SuppliesBO {

    SuppliesDAO suppliesDAO = (SuppliesDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SUPPLIES);
    ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.ITEM);

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
                boolean isSuppliesDetailListSaved = saveSuppliesDetailsList(suppliesDTO.getSuppliesItemDTOS());
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

    @Override
    public boolean saveSuppliesDetailsList(ArrayList<SuppliesItemDTO> suppliesItemDTOS) throws SQLException {
        for (SuppliesItemDTO suppliesItemDTO : suppliesItemDTOS) {
            boolean isSuppliesDetailSaved = saveSuppliesDetail(suppliesItemDTO);
            if (!isSuppliesDetailSaved){
                return false;
            }

            boolean isItemUpdated = itemBO.increaseQty(suppliesItemDTO);
            if (!isItemUpdated){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveSuppliesDetail(SuppliesItemDTO suppliesItemDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into supplies_item values(?,?,?,?)",
                suppliesItemDTO.getSuppliesId(),
                suppliesItemDTO.getItemId(),
                suppliesItemDTO.getQuantity(),
                suppliesItemDTO.getUnitPrice()
        );
    }
}
