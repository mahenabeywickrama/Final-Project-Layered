package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.SuppliesItemDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliesItemModel {
    private final ItemModel itemModel = new ItemModel();

    public boolean saveSuppliesDetailsList(ArrayList<SuppliesItemDTO> suppliesItemDTOS) throws SQLException {
        for (SuppliesItemDTO suppliesItemDTO : suppliesItemDTOS) {
            boolean isSuppliesDetailSaved = saveSuppliesDetail(suppliesItemDTO);
            if (!isSuppliesDetailSaved){
                return false;
            }

            boolean isItemUpdated = itemModel.increaseQty(suppliesItemDTO);
            if (!isItemUpdated){
                return false;
            }
        }
        return true;
    }

    private boolean saveSuppliesDetail(SuppliesItemDTO suppliesItemDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into supplies_item values(?,?,?,?)",
                suppliesItemDTO.getSuppliesId(),
                suppliesItemDTO.getItemId(),
                suppliesItemDTO.getQuantity(),
                suppliesItemDTO.getUnitPrice()
        );
    }
}
