package lk.ijse.gdse.pcstore.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import lk.ijse.gdse.pcstore.bo.BOFactory;
import lk.ijse.gdse.pcstore.bo.custom.*;
import lk.ijse.gdse.pcstore.dto.*;
import lk.ijse.gdse.pcstore.dto.tm.DashBoardTM;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    @FXML
    private Button btnCustomerReport;

    @FXML
    private Button btnOrdersReport;

    @FXML
    private TableColumn<DashBoardTM, String> colCustomer;

    @FXML
    private TableColumn<DashBoardTM, String> colEmployee;

    @FXML
    private TableColumn<DashBoardTM, String> colStatus;

    @FXML
    private TableColumn<DashBoardTM, LocalTime> colTime;

    @FXML
    private TableColumn<DashBoardTM, Double> colTotal;

    @FXML
    private TableColumn<DashBoardTM, String> colType;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblRank;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblTotalCostomer;

    @FXML
    private Label lblTotalNonReturnedRepairs;

    @FXML
    private Label lblTotalOrders;

    @FXML
    private Label lblTotalReplacements;

    @FXML
    private Label lblTotalTodaysIncome;

    @FXML
    private Label lblTotalTodaysOrders;

    @FXML
    private TableView<DashBoardTM> tblOrders;

    CustomerBO customerBO = (CustomerBO) BOFactory.getInstance().getBO(BOFactory.BOType.CUSTOMER);
    EmployeeBO employeeBO = (EmployeeBO) BOFactory.getInstance().getBO(BOFactory.BOType.EMPLOYEE);
    LoginHistoryBO loginHistoryBO = (LoginHistoryBO) BOFactory.getInstance().getBO(BOFactory.BOType.LOGIN_HISTORY);
    OrdersBO ordersBO = (OrdersBO) BOFactory.getInstance().getBO(BOFactory.BOType.ORDERS);
    PaymentBO paymentBO = (PaymentBO) BOFactory.getInstance().getBO(BOFactory.BOType.PAYMENT);
    RepairBO repairBO = (RepairBO) BOFactory.getInstance().getBO(BOFactory.BOType.REPAIR);
    ReplacementBO replacementBO = (ReplacementBO) BOFactory.getInstance().getBO(BOFactory.BOType.REPLACEMENT);
    UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("employee"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        try {
            refreshPage();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load dashboard").show();
        }
    }

    public void refreshPage() throws SQLException {
        loadInfo();
        loadTableData();
        loadDateAndTime();
        setTotalCustomerCount();
        setTotalOrdersCount();
        setNonReturnedRepairCount();
        setTodaysOrdersCount();
        setTodaysIncome();
        setTotalReplacements();
    }

    public void loadTableData() throws SQLException {
        ArrayList<OrdersDTO> ordersDTOS = ordersBO.getAllOrders();

        ObservableList<DashBoardTM> dashBoardTMS = FXCollections.observableArrayList();

        for (OrdersDTO ordersDTO : ordersDTOS) {
            DashBoardTM dashBoardTM = new DashBoardTM(
                    customerBO.findById(ordersDTO.getCustomerId()).getCustomerName(),
                    employeeBO.findById(ordersDTO.getEmployeeId()).getEmployeeName(),
                    ordersDTO.getOrderTime().toLocalTime(),
                    ordersDTO.getType(),
                    ordersDTO.getOrderAmount(),
                    ordersDTO.getOrderStatus()
            );
            dashBoardTMS.add(dashBoardTM);
        }

        tblOrders.setItems(dashBoardTMS);
    }

    public void loadInfo() throws SQLException {
        String currentEmployee = employeeBO.findById(userBO.findById(loginHistoryBO.getLastLogin()).getEmployeeId()).getEmployeeName();
        String currentEmployeeRole = employeeBO.findByName(currentEmployee).getEmployeeRole();
        lblEmployeeName.setText(currentEmployee);
        lblRank.setText(currentEmployeeRole);
    }

    public void loadDateAndTime() {
        Timeline time = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    lblDate.setText(LocalDateTime.now().format(dateFormatter));
                    lblTime.setText(LocalDateTime.now().format(timeFormatter));
                }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void setTotalCustomerCount() throws SQLException {
        ArrayList<CustomerDTO> customerDTOS = customerBO.getAll();
        int customerCount = customerDTOS.size();
        lblTotalCostomer.setText(customerCount + "");
    }

    public void setTotalOrdersCount() throws SQLException {
        ArrayList<OrdersDTO> ordersDTOS = ordersBO.getAllOrders();
        int ordersCount = ordersDTOS.size();
        lblTotalOrders.setText(ordersCount + "");
    }

    public void setTotalReplacements() throws SQLException {
        ArrayList<ReplacementDTO> replacementDTOS = replacementBO.getAllReplacements();
        int replacementCount = replacementDTOS.size();
        lblTotalReplacements.setText(replacementCount + "");
    }

    public void setNonReturnedRepairCount() throws SQLException {
        ArrayList<String> nonReturnedRepairs = repairBO.getAllIds();
        int repairCount = nonReturnedRepairs.size();
        lblTotalNonReturnedRepairs.setText(repairCount + "");
    }

    public void setTodaysOrdersCount() throws SQLException {
        ArrayList<OrdersDTO> ordersDTOS = ordersBO.getAllOrders();
        int ordersCount = 0;

        for (OrdersDTO ordersDTO : ordersDTOS) {
            if (ordersDTO.getOrderDate().toLocalDate().equals(LocalDate.now())) {
                ordersCount++;
            }
        }
        lblTotalTodaysOrders.setText(ordersCount + "");
    }

    public void setTodaysIncome () throws SQLException {
        ArrayList<PaymentDTO> paymentDTOS = paymentBO.getAllPayments();
        double totalIncome = 0;

        for (PaymentDTO paymentDTO : paymentDTOS) {
            if (paymentDTO.getPaymentDate().equals(LocalDate.now())) {
                totalIncome += paymentDTO.getPaymentAmount() - paymentDTO.getBalance();
            }
        }
        lblTotalTodaysIncome.setText(totalIncome + "");
    }
}
