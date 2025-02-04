package lk.ijse.gdse.pcstore.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lk.ijse.gdse.pcstore.dto.CustomerDTO;
import lk.ijse.gdse.pcstore.dto.tm.CustomerTM;
import lk.ijse.gdse.pcstore.model.CustomerModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<CustomerTM, String> colAddress;

    @FXML
    private TableColumn<CustomerTM, String> colCustomerId;

    @FXML
    private TableColumn<CustomerTM, String> colEmail;

    @FXML
    private TableColumn<CustomerTM, String> colName;

    @FXML
    private TableColumn<CustomerTM, String> colNic;

    @FXML
    private TableColumn<CustomerTM, String> colPhone;

    @FXML
    private Label lblCustomerId;

    @FXML
    private TableView<CustomerTM> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtPhone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("customerNic"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));

        try {
            refreshPage();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load customer id").show();
        }
    }

    public void refreshPage() throws SQLException {
        loadNextCustomerId();
        loadTableData();

        btnSave.setDisable(false);

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        txtName.setText("");
        txtNic.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
    }

    CustomerModel customerModel = new CustomerModel();

    public void loadTableData() throws SQLException {
        ArrayList<CustomerDTO> customerDTOS = customerModel.getAllCustomers();

        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();

        for (CustomerDTO customerDTO : customerDTOS) {
            CustomerTM customerTM = new CustomerTM(
                    customerDTO.getCustomerId(),
                    customerDTO.getCustomerName(),
                    customerDTO.getCustomerAddress(),
                    customerDTO.getCustomerNic(),
                    customerDTO.getCustomerPhone(),
                    customerDTO.getCustomerEmail()
            );
            customerTMS.add(customerTM);
        }

        tblCustomer.setItems(customerTMS);
    }

    public void loadNextCustomerId() throws SQLException {
        String nextCustomerId = customerModel.getNextCustomerId();
        lblCustomerId.setText(nextCustomerId);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String customerId = lblCustomerId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = customerModel.deleteCustomer(customerId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Customer deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete customer...!").show();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String customerId = lblCustomerId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        txtName.setStyle(txtName.getStyle()+";-fx-border-color: #000000;");
        txtAddress.setStyle(txtAddress.getStyle()+";-fx-border-color: #000000");
        txtNic.setStyle(txtNic.getStyle()+";-fx-border-color: #000000;");
        txtEmail.setStyle(txtEmail.getStyle()+";-fx-border-color: #000000;");
        txtPhone.setStyle(txtPhone.getStyle()+";-fx-border-color: #000000;");

        String namePattern = "^[A-Za-z ]+$";
        String addressPattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

        boolean isValidName = name.matches(namePattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidEmail = email.matches(emailPattern);
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

        if (!isValidEmail){
            txtEmail.setStyle(txtEmail.getStyle()+";-fx-border-color: red;");
        }

        if (!isValidPhone){
            txtPhone.setStyle(txtPhone.getStyle()+";-fx-border-color: red;");
        }

        if (isValidName && isValidAddress && isValidNic && isValidEmail && isValidPhone) {
            CustomerDTO customerDTO = new CustomerDTO(
                    customerId,
                    name,
                    address,
                    nic,
                    phone,
                    email
            );

            boolean isSaved = customerModel.saveCustomer(customerDTO);
            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Customer saved...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save customer...!").show();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String customerId = lblCustomerId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();
        txtName.setStyle(txtName.getStyle()+";-fx-border-color: #000000;");
        txtAddress.setStyle(txtAddress.getStyle()+";-fx-border-color: #000000");
        txtNic.setStyle(txtNic.getStyle()+";-fx-border-color: #000000;");
        txtEmail.setStyle(txtEmail.getStyle()+";-fx-border-color: #000000;");
        txtPhone.setStyle(txtPhone.getStyle()+";-fx-border-color: #000000;");

        String namePattern = "^[A-Za-z ]+$";
        String addressPattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

        boolean isValidName = name.matches(namePattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidEmail = email.matches(emailPattern);
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

        if (!isValidEmail){
            txtEmail.setStyle(txtEmail.getStyle()+";-fx-border-color: red;");
        }

        if (!isValidPhone){
            txtPhone.setStyle(txtPhone.getStyle()+";-fx-border-color: red;");
        }

        if (isValidName && isValidAddress && isValidNic && isValidEmail && isValidPhone) {
            CustomerDTO customerDTO = new CustomerDTO(
                    customerId,
                    name,
                    address,
                    nic,
                    phone,
                    email
            );

            boolean isUpdate = customerModel.updateCustomer(customerDTO);
            if (isUpdate) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Customer update...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update customer...!").show();
            }
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        CustomerTM customerTM = tblCustomer.getSelectionModel().getSelectedItem();
        if (customerTM != null) {
            lblCustomerId.setText(customerTM.getCustomerId());
            txtName.setText(customerTM.getCustomerName());
            txtNic.setText(customerTM.getCustomerNic());
            txtEmail.setText(customerTM.getCustomerEmail());
            txtPhone.setText(customerTM.getCustomerPhone());
            txtAddress.setText(customerTM.getCustomerAddress());

            btnSave.setDisable(true);

            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }

    @FXML
    void resetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }
}
