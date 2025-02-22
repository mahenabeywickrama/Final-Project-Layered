package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.SuppliesDTO;
import lk.ijse.gdse.pcstore.dto.SuppliesItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SuppliesBO extends SuperBO {
    String getNextSuppliesId() throws SQLException;
    boolean saveSupplies(SuppliesDTO suppliesDTO) throws SQLException;
    boolean saveSuppliesDetailsList(ArrayList<SuppliesItemDTO> suppliesItemDTOS) throws SQLException;
    boolean saveSuppliesDetail(SuppliesItemDTO suppliesItemDTO) throws SQLException;
}
