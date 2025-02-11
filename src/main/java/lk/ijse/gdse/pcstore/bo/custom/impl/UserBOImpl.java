package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.custom.UserBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.custom.UserDAO;
import lk.ijse.gdse.pcstore.dto.UserDTO;
import lk.ijse.gdse.pcstore.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserBOImpl implements UserBO {

    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    @Override
    public String getNextUserId() throws SQLException {
        return userDAO.getNextId();
    }

    @Override
    public boolean saveUser(UserDTO userDTO) throws SQLException {
        return userDAO.save(new User(userDTO.getUserId(), userDTO.getEmployeeId(), userDTO.getUserName(), userDTO.getPassword(), userDTO.getUserNic(), userDTO.getUserPhone(), userDTO.getUserEmail(), userDTO.getUserRole()));
    }

    @Override
    public ArrayList<UserDTO> getAllUsers() throws SQLException {
        ArrayList<UserDTO> userDTOs = new ArrayList<>();
        ArrayList<User> users = userDAO.getAll();

        for (User user : users) {
            UserDTO userDTO = new UserDTO(
                    user.getUserId(),
                    user.getEmployeeId(),
                    user.getUserName(),
                    user.getPassword(),
                    user.getUserNic(),
                    user.getUserPhone(),
                    user.getUserEmail(),
                    user.getUserRole()
            );
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    @Override
    public boolean updateUser(UserDTO userDTO) throws SQLException {
        return userDAO.update(new User(userDTO.getUserId(), userDTO.getEmployeeId(), userDTO.getUserName(), userDTO.getPassword(), userDTO.getUserNic(), userDTO.getUserPhone(), userDTO.getUserEmail(), userDTO.getUserRole()));
    }

    @Override
    public boolean deleteUser(String userId) throws SQLException {
        return userDAO.delete(userId);
    }

    @Override
    public ArrayList<String> getAllUserIds() throws SQLException {
        return userDAO.getAllIds();
    }

    @Override
    public ArrayList<String> getAllUserNames() throws SQLException {
        return userDAO.getAllNames();
    }

    @Override
    public UserDTO findById(String selectedUserId) throws SQLException {
        User user = userDAO.findById(selectedUserId);
        return new UserDTO(user.getUserId(), user.getEmployeeId(), user.getUserName(), user.getPassword(), user.getUserNic(), user.getUserPhone(), user.getUserEmail(), user.getUserRole());
    }

    @Override
    public UserDTO findByName(String selectedUserName) throws SQLException {
        User user = userDAO.findByName(selectedUserName);
        return new UserDTO(user.getUserId(), user.getEmployeeId(), user.getUserName(), user.getPassword(), user.getUserNic(), user.getUserPhone(), user.getUserEmail(), user.getUserRole());
    }
}
