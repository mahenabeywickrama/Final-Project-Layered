package lk.ijse.gdse.pcstore.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.pcstore.bo.BOFactory;
import lk.ijse.gdse.pcstore.bo.custom.EmployeeBO;
import lk.ijse.gdse.pcstore.dto.CustomerDTO;
import lk.ijse.gdse.pcstore.dto.EmployeeDTO;
import lk.ijse.gdse.pcstore.dto.tm.CustomerTM;
import lk.ijse.gdse.pcstore.dto.tm.EmployeeTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TableColumn<EmployeeTM, String> colAddress;

    @FXML
    private TableColumn<EmployeeTM, String> colEmployeeId;

    @FXML
    private TableColumn<EmployeeTM, String> colName;

    @FXML
    private TableColumn<EmployeeTM, String> colNic;

    @FXML
    private TableColumn<EmployeeTM, String> colPhone;

    @FXML
    private TableColumn<EmployeeTM, String> colRole;

    @FXML
    private Label lblEmployeeId;

    @FXML
    private TableView<EmployeeTM> tblEmployee;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtPhone;

    EmployeeBO employeeBO = (EmployeeBO) BOFactory.getInstance().getBO(BOFactory.BOType.EMPLOYEE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("employeeAddress"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("employeeNic"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("employeePhone"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("employeeRole"));

        try {
            refreshPage();
            loadCmb();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load employee id").show();
        }
    }

    public void refreshPage() throws SQLException {
        loadNextEmployeeId();
        loadTableData();

        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        cmbRole.getSelectionModel().clearSelection();

        txtName.setText("");
        txtNic.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
    }

    private void loadCmb() {
        String[] type={"Cashier","Technician","Manager"};
        cmbRole.getItems().addAll(type);
    }

    public void loadTableData() throws SQLException {
        ArrayList<EmployeeDTO> employeeDTOS = employeeBO.getAll();

        ObservableList<EmployeeTM> employeeTMS = FXCollections.observableArrayList();

        for (EmployeeDTO employeeDTO : employeeDTOS) {
            EmployeeTM employeeTM = new EmployeeTM(
                    employeeDTO.getEmployeeId(),
                    employeeDTO.getEmployeeName(),
                    employeeDTO.getEmployeeAddress(),
                    employeeDTO.getEmployeeNic(),
                    employeeDTO.getEmployeePhone(),
                    employeeDTO.getEmployeeRole()
            );
            employeeTMS.add(employeeTM);
        }

        tblEmployee.setItems(employeeTMS);
    }

    public void loadNextEmployeeId() throws SQLException {
        String nextEmployeeId = employeeBO.getNextId();
        lblEmployeeId.setText(nextEmployeeId);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String employeeId = lblEmployeeId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = employeeBO.delete(employeeId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Employee deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete employee...!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String employeeId = lblEmployeeId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String nic = txtNic.getText();
        String phone = txtPhone.getText();
        String role = cmbRole.getSelectionModel().getSelectedItem();

        txtName.setStyle(txtName.getStyle()+";-fx-border-color: #000000;");
        txtAddress.setStyle(txtAddress.getStyle()+";-fx-border-color: #000000");
        txtNic.setStyle(txtNic.getStyle()+";-fx-border-color: #000000;");
        txtPhone.setStyle(txtPhone.getStyle()+";-fx-border-color: #000000;");

        String namePattern = "^[A-Za-z ]+$";
        String addressPattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

        boolean isValidName = name.matches(namePattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidPhone = phone.matches(phonePattern);

        if (!isValidName){
            txtName.setStyle(txtName.getStyle()+";-fx-border-color: red;");
        }

        if (!isValidAddress){
            txtAddress.setStyle(txtAddress.getStyle()+";-fx-border-color: red;");
        }

        if (!isValidNic){
            txtNic.setStyle(txtNic.getStyle()+";-fx-border-color: red;");
        }

        if (!isValidPhone){
            txtPhone.setStyle(txtPhone.getStyle()+";-fx-border-color: red;");
        }

        if (isValidName && isValidAddress && isValidNic && isValidPhone) {
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    employeeId,
                    name,
                    address,
                    nic,
                    phone,
                    role
            );

            boolean isSaved = employeeBO.save(employeeDTO);
            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Employee saved...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save employee...!").show();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String employeeId = lblEmployeeId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String nic = txtNic.getText();
        String phone = txtPhone.getText();
        String role = cmbRole.getSelectionModel().getSelectedItem();

        txtName.setStyle(txtName.getStyle()+";-fx-border-color: #000000;");
        txtAddress.setStyle(txtAddress.getStyle()+";-fx-border-color: #000000");
        txtNic.setStyle(txtNic.getStyle()+";-fx-border-color: #000000;");
        txtPhone.setStyle(txtPhone.getStyle()+";-fx-border-color: #000000;");

        String namePattern = "^[A-Za-z ]+$";
        String addressPattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

        boolean isValidName = name.matches(namePattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidPhone = phone.matches(phonePattern);

        if (!isValidName){
            txtName.setStyle(txtName.getStyle()+";-fx-border-color: red;");
        }

        if (!isValidAddress){
            txtAddress.setStyle(txtAddress.getStyle()+";-fx-border-color: red;");
        }

        if (!isValidNic){
            txtNic.setStyle(txtNic.getStyle()+";-fx-border-color: red;");
        }

        if (!isValidPhone){
            txtPhone.setStyle(txtPhone.getStyle()+";-fx-border-color: red;");
        }

        if (isValidName && isValidAddress && isValidNic && isValidPhone) {
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    employeeId,
                    name,
                    address,
                    nic,
                    phone,
                    role
            );

            boolean isSaved = employeeBO.update(employeeDTO);
            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Employee updated...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update employee...!").show();
            }
        }
    }

    @FXML
    void cmbRoleOnAction(ActionEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) {
        EmployeeTM employeeTM = tblEmployee.getSelectionModel().getSelectedItem();
        if (employeeTM != null) {
            lblEmployeeId.setText(employeeTM.getEmployeeId());
            txtName.setText(employeeTM.getEmployeeName());
            txtNic.setText(employeeTM.getEmployeeNic());
            cmbRole.setValue(employeeTM.getEmployeeRole());
            txtPhone.setText(employeeTM.getEmployeePhone());
            txtAddress.setText(employeeTM.getEmployeeAddress());

            btnSave.setDisable(true);

            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }

}
