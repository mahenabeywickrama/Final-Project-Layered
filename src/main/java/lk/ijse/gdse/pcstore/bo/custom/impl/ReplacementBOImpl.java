package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.custom.ReplacementBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.custom.ReplacementDAO;
import lk.ijse.gdse.pcstore.dto.ReplacementDTO;
import lk.ijse.gdse.pcstore.entity.Replacement;

import java.sql.SQLException;
import java.util.ArrayList;

public class ReplacementBOImpl implements ReplacementBO {

    ReplacementDAO replacementDAO = (ReplacementDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.REPLACEMENT);

    @Override
    public String getNextReplacementId() throws SQLException {
        return replacementDAO.getNextId();
    }

    @Override
    public boolean saveReplacement(ReplacementDTO replacementDTO) throws SQLException {
        return replacementDAO.save(new Replacement(replacementDTO.getReplacementId(), replacementDTO.getOrdersId(), replacementDTO.getFaultyItemId(), replacementDTO.getReplacedItemId(), replacementDTO.getQty(), replacementDTO.getDate(), replacementDTO.getTime(), replacementDTO.getRemarks()));
    }

    @Override
    public ArrayList<ReplacementDTO> getAllReplacements() throws SQLException {
        ArrayList<ReplacementDTO> replacementDTOs = new ArrayList<>();
        ArrayList<Replacement> replacements = replacementDAO.getAll();

        for (Replacement replacement : replacements) {
            ReplacementDTO replacementDTO = new ReplacementDTO(
                    replacement.getReplacementId(),
                    replacement.getOrdersId(),
                    replacement.getFaultyItemId(),
                    replacement.getReplacedItemId(),
                    replacement.getQty(),
                    replacement.getDate(),
                    replacement.getTime(),
                    replacement.getRemarks()
            );
            replacementDTOs.add(replacementDTO);
        }
        return replacementDTOs;
    }

    @Override
    public boolean updateReplacement(ReplacementDTO replacementDTO) throws SQLException {
        return replacementDAO.update(new Replacement(replacementDTO.getReplacementId(), replacementDTO.getOrdersId(), replacementDTO.getFaultyItemId(), replacementDTO.getReplacedItemId(), replacementDTO.getQty(), replacementDTO.getDate(), replacementDTO.getTime(), replacementDTO.getRemarks()));
    }

    @Override
    public boolean deleteReplacement(String replacementId) throws SQLException {
        return replacementDAO.delete(replacementId);
    }
}
