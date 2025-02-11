package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.BOFactory;
import lk.ijse.gdse.pcstore.bo.custom.ItemBO;
import lk.ijse.gdse.pcstore.bo.custom.SuppliesItemBO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dto.SuppliesItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliesItemBOImpl implements SuppliesItemBO {

    ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.ITEM);

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
