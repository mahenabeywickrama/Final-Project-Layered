package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.SuppliesDTO;

import java.sql.SQLException;

public interface SuppliesBO extends SuperBO {
    String getNextSuppliesId() throws SQLException;
    boolean saveSupplies(SuppliesDTO suppliesDTO) throws SQLException;
}
