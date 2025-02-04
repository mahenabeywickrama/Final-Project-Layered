package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.UserDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserModel {
    public String getNextUserId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select user_id from user order by user_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("U%03d", newIdIndex);
        }

        return "U001";
    }

    public boolean saveUser(UserDTO userDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into user values (?,?,?,?,?,?,?,?)",
                userDTO.getUserId(),
                userDTO.getEmployeeId(),
                userDTO.getUserName(),
                userDTO.getPassword(),
                userDTO.getUserNic(),
                userDTO.getUserPhone(),
                userDTO.getUserEmail(),
                userDTO.getUserRole()
        );
    }

    public ArrayList<UserDTO> getAllUsers() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from user");

        ArrayList<UserDTO> userDTOS = new ArrayList<>();

        while (rst.next()) {
            UserDTO userDTO = new UserDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            );
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    public boolean updateUser(UserDTO userDTO) throws SQLException {
        return SQLUtil.execute(
                "update user set employee_id=?, user_name=?, password = ?, nic=?, phone=?, email=?, role=? where user_id=?",
                userDTO.getEmployeeId(),
                userDTO.getUserName(),
                userDTO.getPassword(),
                userDTO.getUserNic(),
                userDTO.getUserPhone(),
                userDTO.getUserEmail(),
                userDTO.getUserRole(),
                userDTO.getUserId()
        );
    }

    public boolean deleteUser(String userId) throws SQLException {
        return SQLUtil.execute("delete from user where user_id=?", userId);
    }

    public ArrayList<String> getAllUserIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select user_id from user");

        ArrayList<String> userIds = new ArrayList<>();

        while (rst.next()) {
            userIds.add(rst.getString(1));
        }

        return userIds;
    }

    public ArrayList<String> getAllUserNames() throws SQLException {
        ResultSet rst = SQLUtil.execute("select user_name from user");

        ArrayList<String> userNames = new ArrayList<>();

        while (rst.next()) {
            userNames.add(rst.getString(1));
        }

        return userNames;
    }

    public UserDTO findById(String selectedUserId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from user where user_id = ?", selectedUserId);

        if (rst.next()) {
            return new UserDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            );
        }
        return null;
    }

    public UserDTO findByName(String selectedUserName) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from user where user_name = ?", selectedUserName);

        if (rst.next()) {
            return new UserDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            );
        }
        return null;
    }
}
