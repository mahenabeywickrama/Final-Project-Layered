package lk.ijse.gdse.pcstore.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.pcstore.dto.EmployeeDTO;
import lk.ijse.gdse.pcstore.dto.UserDTO;
import lk.ijse.gdse.pcstore.dto.tm.EmployeeTM;
import lk.ijse.gdse.pcstore.dto.tm.UserTM;
import lk.ijse.gdse.pcstore.model.EmployeeModel;
import lk.ijse.gdse.pcstore.model.UserModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbEmployeeId;

    @FXML
    private ComboBox<String> cmbEmployeeName;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TableColumn<UserTM, String> colEmail;

    @FXML
    private TableColumn<UserTM, String> colEmployeeId;

    @FXML
    private TableColumn<UserTM, String> colName;

    @FXML
    private TableColumn<UserTM, String> colNic;

    @FXML
    private TableColumn<UserTM, String> colPassword;

    @FXML
    private TableColumn<UserTM, String> colPhone;

    @FXML
    private TableColumn<UserTM, String> colRole;

    @FXML
    private TableColumn<UserTM, String> colUserId;

    @FXML
    private Label lblUserId;

    @FXML
    private TableView<UserTM> tblUser;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtUsername;

    private final EmployeeModel employeeModel = new EmployeeModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("userNic"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("userPhone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("userRole"));

        try {
            loadCmb();
            refreshPage();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load employee id").show();
        }
    }

    public void refreshPage() throws SQLException {
        loadNextUserId();
        loadTableData();

        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        loadEmployeeIds();
        loadEmployeeNames();

        cmbRole.getSelectionModel().clearSelection();
        cmbEmployeeId.getSelectionModel().clearSelection();
        cmbEmployeeName.getSelectionModel().clearSelection();

        txtUsername.setText("");
        txtPassword.setText("");
        txtNic.setText("");
        txtPhone.setText("");
        txtEmail.setText("");

        txtNic.setDisable(false);
        txtPhone.setDisable(false);
    }

    private void loadCmb() {
        String[] type={"ADMIN", "USER"};
        cmbRole.getItems().addAll(type);
    }

    UserModel userModel = new UserModel();

    public void loadTableData() throws SQLException {
        ArrayList<UserDTO> userDTOS = userModel.getAllUsers();

        ObservableList<UserTM> userTMS = FXCollections.observableArrayList();

        for (UserDTO userDTO : userDTOS) {
            UserTM userTM = new UserTM(
                    userDTO.getUserId(),
                    userDTO.getEmployeeId(),
                    userDTO.getUserName(),
                    userDTO.getPassword(),
                    userDTO.getUserNic(),
                    userDTO.getUserPhone(),
                    userDTO.getUserEmail(),
                    userDTO.getUserRole()
            );
            userTMS.add(userTM);
        }

        tblUser.setItems(userTMS);
    }

    public void loadNextUserId() throws SQLException {
        String nextUserId = userModel.getNextUserId();
        lblUserId.setText(nextUserId);
    }

    public void loadEmployeeIds() throws SQLException {
        ArrayList<String> employeeIds = employeeModel.getAllEmployeeIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(employeeIds);
        cmbEmployeeId.setItems(observableList);
    }

    public void loadEmployeeNames() throws SQLException {
        ArrayList<String> employeeNames = employeeModel.getAllEmployeeNames();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(employeeNames);
        cmbEmployeeName.setItems(observableList);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String userId = lblUserId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = userModel.deleteUser(userId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "User deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete user...!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String userId = lblUserId.getText();
        String employeeId = cmbEmployeeId.getValue();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String nic = txtNic.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String role = cmbRole.getSelectionModel().getSelectedItem();

        if (txtUsername.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Username is required!").show();
            return;
        }

        if (txtPassword.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Password is required!").show();
            return;
        }

        UserDTO userDTO = new UserDTO(
                userId,
                employeeId,
                username,
                password,
                nic,
                phone,
                email,
                role
        );

        boolean isSaved = userModel.saveUser(userDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "User saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save user...!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String userId = lblUserId.getText();
        String employeeId = cmbEmployeeId.getValue();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String nic = txtNic.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String role = cmbRole.getSelectionModel().getSelectedItem();

        UserDTO userDTO = new UserDTO(
                userId,
                employeeId,
                username,
                password,
                nic,
                phone,
                email,
                role
        );

        boolean isSaved = userModel.updateUser(userDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "User updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update user...!").show();
        }
    }

    @FXML
    void cmbEmployeeIdOnAction(ActionEvent event) throws SQLException {
        EmployeeDTO employeeDTO = employeeModel.findById(cmbEmployeeId.getValue());

        if (employeeDTO != null) {
            addInfoToTextFields(employeeDTO);
            cmbEmployeeName.getSelectionModel().select(employeeDTO.getEmployeeName());
        }
    }

    @FXML
    void cmbEmployeeNameOnAction(ActionEvent event) throws SQLException {
        EmployeeDTO employeeDTO = employeeModel.findByName(cmbEmployeeName.getValue());

        if (employeeDTO != null) {
            addInfoToTextFields(employeeDTO);
            cmbEmployeeId.getSelectionModel().select(employeeDTO.getEmployeeId());
        }
    }

    public void addInfoToTextFields(EmployeeDTO employeeDTO) {
        txtNic.setText(employeeDTO.getEmployeeNic());
        txtPhone.setText(employeeDTO.getEmployeePhone());
        txtNic.setDisable(true);
        txtPhone.setDisable(true);
    }

    @FXML
    void cmbRoleOnAction(ActionEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) {
        UserTM userTM = tblUser.getSelectionModel().getSelectedItem();
        if (userTM != null) {
            lblUserId.setText(userTM.getUserId());
            cmbEmployeeId.setValue(userTM.getEmployeeId());
            txtUsername.setText(userTM.getUserName());
            txtPassword.setText(userTM.getPassword());
            txtNic.setText(userTM.getUserNic());
            cmbRole.setValue(userTM.getUserRole());
            txtPhone.setText(userTM.getUserPhone());
            txtEmail.setText(userTM.getUserEmail());

            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }
}
