package lk.ijse.gdse.pcstore.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.pcstore.dto.PaymentDTO;
import lk.ijse.gdse.pcstore.dto.UserDTO;
import lk.ijse.gdse.pcstore.dto.tm.EmployeeTM;
import lk.ijse.gdse.pcstore.dto.tm.PaymentTM;
import lk.ijse.gdse.pcstore.model.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbMethod;

    @FXML
    private ComboBox<String> cmbOrdersId;

    @FXML
    private TableColumn<PaymentTM, Double> colAmount;

    @FXML
    private TableColumn<PaymentTM, Double> colBalance;

    @FXML
    private TableColumn<PaymentTM, LocalDate> colDate;

    @FXML
    private TableColumn<PaymentTM, String> colMethod;

    @FXML
    private TableColumn<PaymentTM, String> colOrdersId;

    @FXML
    private TableColumn<PaymentTM, String> colPaymentId;

    @FXML
    private TableColumn<PaymentTM, LocalTime> colTime;

    @FXML
    private Label lblCustomer;

    @FXML
    private Label lblEmployee;

    @FXML
    private Label lblPaymentId;

    @FXML
    private Label lblPrice;

    @FXML
    private TableView<PaymentTM> tblItem;

    @FXML
    private TextField txtAmount;

    private final OrdersModel ordersModel = new OrdersModel();
    private final PaymentModel paymentModel = new PaymentModel();
    private final CustomerModel customerModel = new CustomerModel();
    private final EmployeeModel employeeModel = new EmployeeModel();

    private final ObservableList<PaymentTM> paymentTMObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colOrdersId.setCellValueFactory(new PropertyValueFactory<>("ordersId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("paymentTime"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));

        try {
            refreshPage();
            loadCmb();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load item id").show();
        }
    }

    public void refreshPage() throws SQLException {
        loadNextPaymentId();
        loadCmbOrdersId();
        loadTableData();

        btnSave.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);

        cmbOrdersId.getSelectionModel().clearSelection();
        cmbMethod.getSelectionModel().clearSelection();
        txtAmount.setText("");
        lblCustomer.setText("");
        lblEmployee.setText("");
        lblPrice.setText("");
    }

    public void loadTableData() throws SQLException {
        ArrayList<PaymentDTO> paymentDTOS = paymentModel.getAllPayments();
        ObservableList<PaymentTM> paymentTMS = FXCollections.observableArrayList();

        for (PaymentDTO paymentDTO : paymentDTOS) {
            PaymentTM paymentTM = new PaymentTM(
                    paymentDTO.getPaymentId(),
                    paymentDTO.getOrdersId(),
                    paymentDTO.getPaymentDate(),
                    paymentDTO.getPaymentTime(),
                    paymentDTO.getPaymentMethod(),
                    paymentDTO.getPaymentAmount(),
                    paymentDTO.getBalance()
            );
            paymentTMS.add(paymentTM);
        }

        tblItem.setItems(paymentTMS);
    }

    public void loadNextPaymentId() throws SQLException {
        String nextPaymentId = paymentModel.getNextPaymentId();
        lblPaymentId.setText(nextPaymentId);
    }

    public void loadCmbOrdersId() throws SQLException {
        ArrayList<String> ordersIds = ordersModel.getAllOrdersIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(ordersIds);
        cmbOrdersId.setItems(observableList);
    }

    private void loadCmb() {
        String[] type={"Card", "Cash"};
        cmbMethod.getItems().addAll(type);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String paymentId = lblPaymentId.getText();
        String ordersId = cmbOrdersId.getValue();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = paymentModel.deletePayment(paymentId);
            if (isDeleted) {
                refreshPage();
                ordersModel.updateOrdersPending(ordersId);
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
        String paymentId = lblPaymentId.getText();
        String ordersId = cmbOrdersId.getValue();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String method = cmbMethod.getValue();
        double amount = Double.parseDouble(txtAmount.getText());
        double price = Double.parseDouble(lblPrice.getText());

        if (amount < price) {
            new Alert(Alert.AlertType.INFORMATION, "Insufficient funds...!").show();
            return;
        }

        double balance = amount - price;

        PaymentDTO paymentDTO = new PaymentDTO(
                paymentId,
                ordersId,
                date,
                time,
                method,
                amount,
                balance
        );

        boolean isSaved = paymentModel.savePayment(paymentDTO);
        if (isSaved) {
            refreshPage();
            ordersModel.updateOrdersPaid(ordersId);
            new Alert(Alert.AlertType.INFORMATION, "Payment saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save payment...!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String paymentId = lblPaymentId.getText();
        String ordersId = cmbOrdersId.getValue();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String method = cmbMethod.getValue();
        double amount = Double.parseDouble(txtAmount.getText());
        double price = Double.parseDouble(lblPrice.getText());

        if (amount < price) {
            new Alert(Alert.AlertType.INFORMATION, "Insufficient funds...!").show();
            return;
        }

        double balance = amount - price;

        PaymentDTO paymentDTO = new PaymentDTO(
                paymentId,
                ordersId,
                date,
                time,
                method,
                amount,
                balance
        );

        boolean isSaved = paymentModel.updatePayment(paymentDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Payment updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update payment...!").show();
        }
    }

    @FXML
    void cmbOrdersIdOnAction(ActionEvent event) throws SQLException {
        String ordersId = cmbOrdersId.getValue();

        if (ordersId != null) {
            lblCustomer.setText(ordersModel.findOrdersCustomer(ordersId));
            lblEmployee.setText(ordersModel.findOrdersEmployee(ordersId));
            lblPrice.setText(ordersModel.findOrdersPrice(ordersId));
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        PaymentTM paymentTM = tblItem.getSelectionModel().getSelectedItem();
        if (paymentTM != null) {
            cmbOrdersId.setValue(paymentTM.getOrdersId());
            lblPaymentId.setText(paymentTM.getPaymentId());
            txtAmount.setText(String.valueOf(paymentTM.getPaymentAmount()));
            cmbMethod.setValue(paymentTM.getPaymentMethod());

            btnSave.setDisable(true);

            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }
}
