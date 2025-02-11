package lk.ijse.gdse.pcstore.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.pcstore.bo.BOFactory;
import lk.ijse.gdse.pcstore.bo.custom.EmployeeBO;
import lk.ijse.gdse.pcstore.bo.custom.LoginHistoryBO;
import lk.ijse.gdse.pcstore.bo.custom.UserBO;
import lk.ijse.gdse.pcstore.dto.EmployeeDTO;
import lk.ijse.gdse.pcstore.dto.LoginDTO;
import lk.ijse.gdse.pcstore.dto.UserDTO;
import lk.ijse.gdse.pcstore.dto.tm.LoginTM;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class LoginHistoryController implements Initializable {

    @FXML
    private TableColumn<LoginTM, LocalDate> colDate;

    @FXML
    private TableColumn<LoginTM, String> colLoginId;

    @FXML
    private TableColumn<LoginTM, String> colName;

    @FXML
    private TableColumn<LoginTM, String> colRole;

    @FXML
    private TableColumn<LoginTM, LocalTime> colTime;

    @FXML
    private TableColumn<LoginTM, String> colUserId;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblEmployeeId;

    @FXML
    private Label lblName;

    @FXML
    private Label lblNic;

    @FXML
    private Label lblPhone;

    @FXML
    private Label lblRole;

    @FXML
    private TableView<LoginTM> tblLoginHistory;

    EmployeeBO employeeBO = (EmployeeBO) BOFactory.getInstance().getBO(BOFactory.BOType.EMPLOYEE);
    LoginHistoryBO loginHistoryBO = (LoginHistoryBO) BOFactory.getInstance().getBO(BOFactory.BOType.LOGIN_HISTORY);
    UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    ObservableList<LoginTM> loginTMS= FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colLoginId.setCellValueFactory((new PropertyValueFactory<>("loginId")));
        colUserId.setCellValueFactory((new PropertyValueFactory<>("userId")));
        colName.setCellValueFactory((new PropertyValueFactory<>("userName")));
        colDate.setCellValueFactory((new PropertyValueFactory<>("date")));
        colTime.setCellValueFactory((new PropertyValueFactory<>("time")));
        colRole.setCellValueFactory((new PropertyValueFactory<>("role")));

        try {
            for (LoginDTO loginDTO : loginHistoryBO.getAll()) {
                UserDTO userDTO = userBO.findById(loginDTO.getUserId());
                LocalDate localDate = loginDTO.getDate();
                LocalTime localTime = loginDTO.getTime();
                loginTMS.add(new LoginTM(
                        loginDTO.getLoginId(),
                        loginDTO.getUserId(),
                        userDTO.getUserName(),
                        localDate,
                        localTime,
                        userDTO.getUserRole()
                ));
            }
            tblLoginHistory.setItems(loginTMS);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).show();
        }
    }

    @FXML
    void onClickTable(MouseEvent event) throws SQLException {
        LoginTM loginTM = tblLoginHistory.getSelectionModel().getSelectedItem();
        UserDTO userDTO = userBO.findById(loginTM.getUserId());
        EmployeeDTO employeeDTO = employeeBO.findById(userDTO.getEmployeeId());

        if (loginTM != null) {
            lblEmployeeId.setText(userDTO.getEmployeeId());
            lblName.setText(employeeDTO.getEmployeeName());
            lblNic.setText(employeeDTO.getEmployeeNic());
            lblPhone.setText(employeeDTO.getEmployeePhone());
            lblRole.setText(employeeDTO.getEmployeeRole());
            lblEmail.setText(userDTO.getUserEmail());
        }
    }
}
