package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.ReplacementDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ReplacementBO extends SuperBO {
    String getNextReplacementId() throws SQLException;
    boolean saveReplacement(ReplacementDTO replacementDTO) throws SQLException;
    ArrayList<ReplacementDTO> getAllReplacements() throws SQLException;
    boolean updateReplacement(ReplacementDTO replacementDTO) throws SQLException;
    boolean deleteReplacement(String replacementId) throws SQLException;
}
