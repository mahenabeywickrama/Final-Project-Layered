package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.PCItemDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PCItemModel {
    public boolean savePCItemList(ArrayList<PCItemDTO> pcItemDTOS) throws SQLException {
        for (PCItemDTO pcItemDTO : pcItemDTOS) {
            boolean isPcItemsSaved = savePCItem(pcItemDTO);
            if (!isPcItemsSaved) {
                return false;
            }
        }
        return true;
    }

    private boolean savePCItem(PCItemDTO pcItemDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into pc_item values (?,?,?,?)",
                pcItemDTO.getPcId(),
                pcItemDTO.getItemId(),
                pcItemDTO.getQty(),
                pcItemDTO.getPrice()
        );
    }

    public boolean updatePCItemList(ArrayList<PCItemDTO> pcItemDTOS) throws SQLException {
        for (PCItemDTO pcItemDTO : pcItemDTOS) {
            boolean isPcItemsSaved = updatePCItem(pcItemDTO);
            if (!isPcItemsSaved) {
                return false;
            }
        }
        return true;
    }

    private boolean updatePCItem(PCItemDTO pcItemDTO) throws SQLException {
        return SQLUtil.execute(
                "update pc_item set item_id = ?, qty = ?, price = ? where pc_id = ?",
                pcItemDTO.getItemId(),
                pcItemDTO.getQty(),
                pcItemDTO.getPrice(),
                pcItemDTO.getPcId()
        );
    }

    public ArrayList<PCItemDTO> findById(String selectedPCId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from pc_item where pc_id = ?", selectedPCId);

        ArrayList<PCItemDTO> pcItemDTOS = new ArrayList<>();

        while (rst.next()) {
            PCItemDTO pcItemDTO = new PCItemDTO(
                    rst.getString(1),
                    rst.getString(2),
                    Integer.parseInt(rst.getString(3)),
                    Double.parseDouble(rst.getString(4))
            );
            pcItemDTOS.add(pcItemDTO);
        }
        return pcItemDTOS;
    }
}
