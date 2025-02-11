package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.custom.LoginHistoryBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.custom.LoginHistoryDAO;
import lk.ijse.gdse.pcstore.dto.LoginDTO;
import lk.ijse.gdse.pcstore.entity.Login;

import java.sql.SQLException;
import java.util.ArrayList;

public class LoginHistoryBOImpl implements LoginHistoryBO {

    LoginHistoryDAO loginHistoryDAO = (LoginHistoryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.LOGIN_HISTORY);

    @Override
    public String getNextId() throws SQLException {
        return loginHistoryDAO.getNextId();
    }

    @Override
    public ArrayList<LoginDTO> getAll() throws SQLException {
        ArrayList<LoginDTO> loginDTOs = new ArrayList<>();
        ArrayList<Login> logins = loginHistoryDAO.getAll();
        for (Login login : logins) {
            LoginDTO loginDTO = new LoginDTO(login.getLoginId(), login.getUserId(), login.getDate(), login.getTime());
            loginDTOs.add(loginDTO);
        }
        return loginDTOs;
    }

    @Override
    public boolean save(LoginDTO loginDTO) throws SQLException {
        return loginHistoryDAO.save(new Login(loginDTO.getLoginId(), loginDTO.getUserId(), loginDTO.getDate(), loginDTO.getTime()));
    }

    @Override
    public String getLastLogin() throws SQLException {
        return loginHistoryDAO.getLastLogin();
    }
}
