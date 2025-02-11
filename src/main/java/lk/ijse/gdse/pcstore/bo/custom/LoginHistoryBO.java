package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.LoginDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface LoginHistoryBO extends SuperBO {
    String getNextId() throws SQLException;
    ArrayList<LoginDTO> getAll() throws SQLException;
    boolean save(LoginDTO loginDTO) throws SQLException;
    String getLastLogin() throws SQLException;
}
