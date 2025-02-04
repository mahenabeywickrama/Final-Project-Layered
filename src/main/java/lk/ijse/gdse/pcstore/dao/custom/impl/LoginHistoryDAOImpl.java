package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.LoginHistoryDAO;
import lk.ijse.gdse.pcstore.entity.Login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginHistoryDAOImpl implements LoginHistoryDAO {
    @Override
    public String getNextId() throws SQLException {
        ResultSet rst= SQLUtil.execute("select login_id from login_history order by login_id desc limit 1");

        if(rst.next()){
            return String.format("L%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "L001";
    }

    @Override
    public ArrayList<Login> getAll() throws SQLException {
        ResultSet rst= SQLUtil.execute("select * from login_history");
        ArrayList<Login> loginHistory = new ArrayList<>();

        while (rst.next()){
            loginHistory.add(new Login(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDate(3).toLocalDate(),
                    rst.getTime(4).toLocalTime()
            ));
        }
        return loginHistory;
    }

    @Override
    public boolean save(Login login) throws SQLException {
        return SQLUtil.execute("insert into login_history values(?, ?, ?, ?)",
                login.getLoginId(),
                login.getUserId(),
                login.getDate(),
                login.getTime()
        );
    }

    @Override
    public boolean update(Login dto) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        return null;
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        return null;
    }

    @Override
    public Login findById(String id) throws SQLException {
        return null;
    }

    @Override
    public Login findByName(String name) throws SQLException {
        return null;
    }

    @Override
    public String getLastLogin() throws SQLException {
        ResultSet rst = SQLUtil.execute("select user_id from login_history order by login_id desc limit 1");

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
