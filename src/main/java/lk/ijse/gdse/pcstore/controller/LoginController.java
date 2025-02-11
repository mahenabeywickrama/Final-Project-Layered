package lk.ijse.gdse.pcstore.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse.pcstore.bo.BOFactory;
import lk.ijse.gdse.pcstore.bo.custom.LoginHistoryBO;
import lk.ijse.gdse.pcstore.bo.custom.UserBO;
import lk.ijse.gdse.pcstore.dto.LoginDTO;
import lk.ijse.gdse.pcstore.dto.UserDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class LoginController {

    @FXML
    private Label lblRole;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField txtUserName;

    LoginHistoryBO loginHistoryBO = (LoginHistoryBO) BOFactory.getInstance().getBO(BOFactory.BOType.LOGIN_HISTORY);
    UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    private boolean isAdmin = true;
    private UserDTO userDTO;

    @FXML
    void btnAdminOnAction(ActionEvent event) {
        isAdmin = true;
        lblRole.setText("ADMIN");
    }

    @FXML
    void btnLogInOnAction(ActionEvent event) throws IOException {
        try {
            if (!(txtUserName.getText().isEmpty() || pfPassword.getText().isEmpty())) {
                String userName = txtUserName.getText();
                String password = pfPassword.getText();
                String rank = isAdmin ? "ADMIN" : "USER";
                if (isAdmin) {
                    if (verify(userName, password, rank)) {
                        login(userDTO);
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Your user name, password wrong or your not an admin ...!").show();
                    }
                } else {
                    if (verify(userName, password, rank)) {
                        login(userDTO);
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Your user name, password wrong or your not an user ...!").show();
                    }
                }
            } else {
                if (txtUserName.getText().isEmpty() && pfPassword.getText().isEmpty()) {
                    txtUserName.setStyle(txtUserName.getStyle() + ";-fx-border-color: red;");
                    pfPassword.setStyle(pfPassword.getStyle() + ";-fx-border-color: red;");
                } else if (pfPassword.getText().isEmpty()) {
                    pfPassword.setStyle(pfPassword.getStyle() + ";-fx-border-color: red;");
                } else {
                    txtUserName.setStyle(txtUserName.getStyle() + ";-fx-border-color: red;");
                }
            }
        }catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUserOnAction(ActionEvent event) {
        isAdmin = false;
        lblRole.setText("USER");
    }

    public boolean verify(String username, String password, String role) throws SQLException {
        userDTO = userBO.findByName(username);
        if (userDTO != null && userDTO.getPassword().equals(password) && role.equalsIgnoreCase(userDTO.getUserRole())) {
            return true;
        }
        return false;
    }

    public void login(UserDTO userDTO) {
        try {
            if(loginHistoryBO.save(new LoginDTO(loginHistoryBO.getNextId(), userDTO.getUserId(), LocalDate.now(), LocalTime.now()))) {
                loginPane.getChildren().clear();
                loginPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/MainLayout.fxml")));
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Forms Error !\n"+e.getMessage()).show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error !\n"+e.getMessage()).show();
        }
    }
}
