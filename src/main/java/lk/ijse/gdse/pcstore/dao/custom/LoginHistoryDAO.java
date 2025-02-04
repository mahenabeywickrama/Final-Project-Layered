package lk.ijse.gdse.pcstore.dao.custom;

import lk.ijse.gdse.pcstore.dao.CrudDAO;
import lk.ijse.gdse.pcstore.entity.Login;

import java.sql.SQLException;

public interface LoginHistoryDAO extends CrudDAO<Login> {
    String getLastLogin() throws SQLException;
}
