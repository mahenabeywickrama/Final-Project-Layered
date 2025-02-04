package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.UserDAO;
import lk.ijse.gdse.pcstore.dto.UserDTO;
import lk.ijse.gdse.pcstore.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAOImpl implements UserDAO {
    @Override
    public String getNextId() throws SQLException {
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

    @Override
    public ArrayList<User> getAll() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from user");

        ArrayList<User> users = new ArrayList<>();

        while (rst.next()) {
            User user = new User(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            );
            users.add(user);
        }
        return users;
    }

    @Override
    public boolean save(User user) throws SQLException {
        return SQLUtil.execute(
                "insert into user values (?,?,?,?,?,?,?,?)",
                user.getUserId(),
                user.getEmployeeId(),
                user.getUserName(),
                user.getPassword(),
                user.getUserNic(),
                user.getUserPhone(),
                user.getUserEmail(),
                user.getUserRole()
        );
    }

    @Override
    public boolean update(User user) throws SQLException {
        return SQLUtil.execute(
                "update user set employee_id=?, user_name=?, password = ?, nic=?, phone=?, email=?, role=? where user_id=?",
                user.getEmployeeId(),
                user.getUserName(),
                user.getPassword(),
                user.getUserNic(),
                user.getUserPhone(),
                user.getUserEmail(),
                user.getUserRole(),
                user.getUserId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return SQLUtil.execute("delete from user where user_id=?", id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select user_id from user");

        ArrayList<String> userIds = new ArrayList<>();

        while (rst.next()) {
            userIds.add(rst.getString(1));
        }

        return userIds;
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        ResultSet rst = SQLUtil.execute("select user_name from user");

        ArrayList<String> userNames = new ArrayList<>();

        while (rst.next()) {
            userNames.add(rst.getString(1));
        }

        return userNames;
    }

    @Override
    public User findById(String id) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from user where user_id = ?", id);

        if (rst.next()) {
            return new User(
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

    @Override
    public User findByName(String name) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from user where user_name = ?", name);

        if (rst.next()) {
            return new User(
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
