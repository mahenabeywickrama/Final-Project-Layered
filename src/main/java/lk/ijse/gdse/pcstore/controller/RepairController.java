package lk.ijse.gdse.pcstore.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.pcstore.dto.CustomerDTO;
import lk.ijse.gdse.pcstore.dto.ItemDTO;
import lk.ijse.gdse.pcstore.dto.RepairDTO;
import lk.ijse.gdse.pcstore.dto.tm.ItemTM;
import lk.ijse.gdse.pcstore.dto.tm.RepairTM;
import lk.ijse.gdse.pcstore.model.CustomerModel;
import lk.ijse.gdse.pcstore.model.RepairModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class RepairController implements Initializable {

    @FXML
    private Button btnDeleteRepair;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSaveRepair;

    @FXML
    private Button btnUpdateRepair;

    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private ComboBox<String> cmbName;

    @FXML
    private ComboBox<String> cmbState;

    @FXML
    private TableColumn<RepairTM, String> colCustomerId;

    @FXML
    private TableColumn<RepairTM, String> colDescription;

    @FXML
    private TableColumn<RepairTM, LocalDate> colReceiveDate;

    @FXML
    private TableColumn<RepairTM, String> colRepairId;

    @FXML
    private TableColumn<RepairTM, LocalDate> colReturnDate;

    @FXML
    private TableColumn<RepairTM, String> colStatus;

    @FXML
    private DatePicker dpReceiveDate;

    @FXML
    private DatePicker dpReturnDate;

    @FXML
    private Label lblRepairId;

    @FXML
    private TableView<RepairTM> tblRepair;

    @FXML
    private TextField txtDescription;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colRepairId.setCellValueFactory(new PropertyValueFactory<>("repairId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colReceiveDate.setCellValueFactory(new PropertyValueFactory<>("receiveDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        try {
            refreshPage();
            loadCmbCustomerId();
            loadCmbCustomerNames();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load repair id").show();
        }
    }

    private void refreshPage() throws SQLException {
        loadNextRepairId();
        loadTableData();

        btnSaveRepair.setDisable(false);
        btnDeleteRepair.setDisable(true);
        btnUpdateRepair.setDisable(true);

        cmbCustomerId.getSelectionModel().clearSelection();
        cmbName.getSelectionModel().clearSelection();
        txtDescription.setText("");
        dpReceiveDate.setValue(null);
        dpReturnDate.setValue(null);
    }

    RepairModel repairModel = new RepairModel();
    CustomerModel customerModel = new CustomerModel();

    private void loadTableData() throws SQLException {
        ArrayList<RepairDTO> repairDTOS = repairModel.getAllRepair();
        ObservableList<RepairTM> repairTMS = FXCollections.observableArrayList();

        for (RepairDTO repairDTO : repairDTOS) {
            RepairTM repairTM = new RepairTM(
                    repairDTO.getRepairId(),
                    repairDTO.getCustomerId(),
                    repairDTO.getDescription(),
                    repairDTO.getStatus(),
                    repairDTO.getReceiveDate(),
                    repairDTO.getReturnDate()
            );
            repairTMS.add(repairTM);
        }

        tblRepair.setItems(repairTMS);
    }

    private void loadNextRepairId() throws SQLException {
        String nextRepairId = repairModel.getNextRepairId();
        lblRepairId.setText(nextRepairId);
    }

    private void loadCmbCustomerId() throws SQLException {
        ArrayList<String> customerIds = customerModel.getAllCustomerIds();
        ObservableList<String> cmbCustomerIds = FXCollections.observableArrayList();
        cmbCustomerIds.addAll(customerIds);
        cmbCustomerId.setItems(cmbCustomerIds);
    }

    private void loadCmbCustomerNames() throws SQLException {
        ArrayList<String> customerNames = customerModel.getAllCustomerNames();
        ObservableList<String> cmbCustomerNames = FXCollections.observableArrayList();
        cmbCustomerNames.addAll(customerNames);
        cmbName.setItems(cmbCustomerNames);
    }

    @FXML
    void btnDeleteRepairOnAction(ActionEvent event) throws SQLException {
        String repairId = lblRepairId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = repairModel.deleteRepair(repairId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Repair deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete repair...!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void btnSaveRepairOnAction(ActionEvent event) throws SQLException {
        String repairId = lblRepairId.getText();
        String customerId = cmbCustomerId.getValue();
        String description = txtDescription.getText();
        String status = "REPAIRING";
        LocalDate receiveDate = dpReceiveDate.getValue();
        LocalDate returnDate = dpReturnDate.getValue();

        if (receiveDate.isAfter(returnDate)) {
            new Alert(Alert.AlertType.ERROR, "Please enter valid dates..!").show();
            return;
        }

        RepairDTO repairDTO = new RepairDTO(repairId, customerId, description, status, receiveDate, returnDate);

        boolean isSaved = repairModel.saveRepair(repairDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Repair saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save repair...!").show();
        }
    }

    @FXML
    void btnUpdateRepairOnAction(ActionEvent event) throws SQLException {
        String repairId = lblRepairId.getText();
        String customerId = cmbCustomerId.getValue();
        String description = txtDescription.getText();
        String status = "REPAIRING";
        LocalDate receiveDate = dpReceiveDate.getValue();
        LocalDate returnDate = dpReturnDate.getValue();

        RepairDTO repairDTO = new RepairDTO(repairId, customerId, description, status, receiveDate, returnDate);

        boolean isSaved = repairModel.updateRepair(repairDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Repair updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update repair...!").show();
        }
    }

    @FXML
    void cmbCustomerOnAction(ActionEvent event) throws SQLException {
        CustomerDTO customerDTO = customerModel.findById(cmbCustomerId.getValue());

        if (customerDTO != null) {
            cmbName.getSelectionModel().select(customerDTO.getCustomerName());
        }
    }

    @FXML
    void cmbNameOnAction(ActionEvent event) throws SQLException {
        CustomerDTO customerDTO = customerModel.findByName(cmbName.getValue());

        if (customerDTO != null) {
            cmbCustomerId.getSelectionModel().select(customerDTO.getCustomerId());
        }
    }

    @FXML
    void onClickTable(MouseEvent event) throws SQLException {
        RepairTM repairTM = tblRepair.getSelectionModel().getSelectedItem();

        if (repairTM != null) {
            lblRepairId.setText(repairTM.getRepairId());
            cmbCustomerId.setValue(repairTM.getCustomerId());
            cmbName.setValue(customerModel.findById(repairTM.getCustomerId()).getCustomerName());
            txtDescription.setText(repairTM.getDescription());
            dpReceiveDate.setValue(repairTM.getReceiveDate());
            dpReturnDate.setValue(repairTM.getReturnDate());

            btnSaveRepair.setDisable(true);
            btnDeleteRepair.setDisable(false);
            btnUpdateRepair.setDisable(false);
        }
    }

}
