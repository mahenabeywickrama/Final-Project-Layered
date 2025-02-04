package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.LoginDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginHistoryModel {
    public String getNextLoginId() throws SQLException {
        ResultSet rst= SQLUtil.execute("select login_id from login_history order by login_id desc limit 1");

        if(rst.next()){
            return String.format("L%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "L001";
    }

    public String getLastLogin() throws SQLException {
        ResultSet rst = SQLUtil.execute("select user_id from login_history order by login_id desc limit 1");

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public boolean saveLogin(LoginDTO loginDTO) throws SQLException {
        return SQLUtil.execute("insert into login_history values(?, ?, ?, ?)",
                loginDTO.getLoginId(),
                loginDTO.getUserId(),
                loginDTO.getDate(),
                loginDTO.getTime()
        );
    }
    public ArrayList<LoginDTO> getAllLogin() throws SQLException {
        ResultSet rst= SQLUtil.execute("select * from login_history");
        ArrayList<LoginDTO> loginHistoryArrayList = new ArrayList<>();

        while (rst.next()){
            loginHistoryArrayList.add(new LoginDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDate(3).toLocalDate(),
                    rst.getTime(4).toLocalTime()
            ));
        }
        return loginHistoryArrayList;
    }
}
