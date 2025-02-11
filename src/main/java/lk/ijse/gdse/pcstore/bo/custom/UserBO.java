package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.UserDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserBO extends SuperBO {
    String getNextUserId() throws SQLException;
    boolean saveUser(UserDTO userDTO) throws SQLException;
    ArrayList<UserDTO> getAllUsers() throws SQLException;
    boolean updateUser(UserDTO userDTO) throws SQLException;
    boolean deleteUser(String userId) throws SQLException;
    ArrayList<String> getAllUserIds() throws SQLException;
    ArrayList<String> getAllUserNames() throws SQLException;
    UserDTO findById(String selectedUserId) throws SQLException;
    UserDTO findByName(String selectedUserName) throws SQLException;
}
