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
import lk.ijse.gdse.pcstore.bo.custom.SupplierBO;
import lk.ijse.gdse.pcstore.dto.CustomerDTO;
import lk.ijse.gdse.pcstore.dto.SupplierDTO;
import lk.ijse.gdse.pcstore.dto.tm.CustomerTM;
import lk.ijse.gdse.pcstore.dto.tm.SupplierTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<SupplierTM, String> colAddress;

    @FXML
    private TableColumn<SupplierTM, String> colSupplierId;

    @FXML
    private TableColumn<SupplierTM, String> colEmail;

    @FXML
    private TableColumn<SupplierTM, String> colName;

    @FXML
    private TableColumn<SupplierTM, String> colNic;

    @FXML
    private TableColumn<SupplierTM, String> colPhone;

    @FXML
    private Label lblSupplierId;

    @FXML
    private TableView<SupplierTM> tblSupplier;

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

    SupplierBO supplierBO = (SupplierBO) BOFactory.getInstance().getBO(BOFactory.BOType.SUPPLIER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("supplierAddress"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("supplierNic"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("supplierPhone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("supplierEmail"));

        try {
            refreshPage();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load supplier id").show();
        }
    }

    public void refreshPage() throws SQLException {
        loadNextSupplierId();
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

    public void loadTableData() throws SQLException {
        ArrayList<SupplierDTO> supplierDTOS = supplierBO.getAllSuppliers();

        ObservableList<SupplierTM> supplierTMS = FXCollections.observableArrayList();

        for (SupplierDTO supplierDTO : supplierDTOS) {
            SupplierTM supplierTM = new SupplierTM(
                    supplierDTO.getSupplierId(),
                    supplierDTO.getSupplierName(),
                    supplierDTO.getSupplierAddress(),
                    supplierDTO.getSupplierNic(),
                    supplierDTO.getSupplierPhone(),
                    supplierDTO.getSupplierEmail()
            );
            supplierTMS.add(supplierTM);
        }

        tblSupplier.setItems(supplierTMS);
    }

    public void loadNextSupplierId() throws SQLException {
        String nextSupplierId = supplierBO.getNextSupplierId();
        lblSupplierId.setText(nextSupplierId);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String supplierId = lblSupplierId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = supplierBO.deleteSupplier(supplierId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Supplier deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete supplier...!").show();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String supplierId = lblSupplierId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        SupplierDTO supplierDTO = new SupplierDTO(
                supplierId,
                name,
                address,
                nic,
                phone,
                email
        );

        boolean isSaved = supplierBO.saveSupplier(supplierDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Supplier saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save supplier...!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String supplierId = lblSupplierId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();

        SupplierDTO supplierDTO = new SupplierDTO(
                supplierId,
                name,
                address,
                nic,
                phone,
                email
        );

        boolean isUpdate = supplierBO.updateSupplier(supplierDTO);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Supplier update...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update supplier...!").show();
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        SupplierTM supplierTM = tblSupplier.getSelectionModel().getSelectedItem();
        if (supplierTM != null) {
            lblSupplierId.setText(supplierTM.getSupplierId());
            txtName.setText(supplierTM.getSupplierName());
            txtNic.setText(supplierTM.getSupplierNic());
            txtEmail.setText(supplierTM.getSupplierEmail());
            txtPhone.setText(supplierTM.getSupplierPhone());
            txtAddress.setText(supplierTM.getSupplierAddress());

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
