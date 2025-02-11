package lk.ijse.gdse.pcstore.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.gdse.pcstore.bo.BOFactory;
import lk.ijse.gdse.pcstore.bo.custom.*;
import lk.ijse.gdse.pcstore.dto.*;
import lk.ijse.gdse.pcstore.dto.tm.ItemCartTM;
import lk.ijse.gdse.pcstore.dto.tm.RepairCartTM;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {

    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private ComboBox<String> cmbCustomerName;

    @FXML
    private ComboBox<String> cmbEmployeeId;

    @FXML
    private ComboBox<String> cmbEmployeeName;

    @FXML
    private ComboBox<String> cmbItemId;

    @FXML
    private ComboBox<String> cmbItemName;

    @FXML
    private ComboBox<String> cmbRepairId;

    @FXML
    private TableColumn<RepairCartTM, String> colDescription;

    @FXML
    private TableColumn<ItemCartTM, Button> colItemAction;

    @FXML
    private TableColumn<ItemCartTM, String> colItemId;

    @FXML
    private TableColumn<ItemCartTM, String> colName;

    @FXML
    private TableColumn<ItemCartTM, Double> colPrice;

    @FXML
    private TableColumn<ItemCartTM, Integer> colQuantity;

    @FXML
    private TableColumn<RepairCartTM, Button> colRepairAction;

    @FXML
    private TableColumn<RepairCartTM, String> colRepairId;

    @FXML
    private TableColumn<RepairCartTM, Double> colRepairPrice;

    @FXML
    private TableColumn<ItemCartTM, Double> colTotal;

    @FXML
    private Label lblItemPrice;

    @FXML
    private Label lblItemQty;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblRepairDescription;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private Label orderDate;

    @FXML
    private TableView<ItemCartTM> tblItem;

    @FXML
    private TableView<RepairCartTM> tblRepair;

    @FXML
    private TextField txtAddToCartPrice;

    @FXML
    private TextField txtAddToCartQty;

    CustomerBO customerBO = (CustomerBO) BOFactory.getInstance().getBO(BOFactory.BOType.CUSTOMER);
    EmployeeBO employeeBO = (EmployeeBO) BOFactory.getInstance().getBO(BOFactory.BOType.EMPLOYEE);
    ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.ITEM);
    OrdersBO ordersBO = (OrdersBO) BOFactory.getInstance().getBO(BOFactory.BOType.ORDERS);
    RepairBO repairBO = (RepairBO) BOFactory.getInstance().getBO(BOFactory.BOType.REPAIR);

    private final ObservableList<ItemCartTM> itemCartTMObservableList = FXCollections.observableArrayList();
    private final ObservableList<RepairCartTM> repairCartTMObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();

        try {
            refreshPage();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
        }
    }

    public void setCellValues(){
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("cartQuantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colItemAction.setCellValueFactory(new PropertyValueFactory<>("removeBtn"));

        colRepairId.setCellValueFactory(new PropertyValueFactory<>("repairId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colRepairPrice.setCellValueFactory(new PropertyValueFactory<>("repairPrice"));
        colRepairAction.setCellValueFactory(new PropertyValueFactory<>("removeBtn"));

        tblItem.setItems(itemCartTMObservableList);
        tblRepair.setItems(repairCartTMObservableList);
    }

    public void refreshPage() throws SQLException {
        lblOrderId.setText(ordersBO.getNextOrderId());
        orderDate.setText(LocalDate.now().toString());

        loadCustomerIds();
        loadCustomerNames();
        loadEmployeeIds();
        loadEmployeeNames();
        loadItemIds();
        loadItemNames();
        loadRepairIds();

        reset();
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbCustomerName.getSelectionModel().clearSelection();
        cmbEmployeeId.getSelectionModel().clearSelection();
        cmbEmployeeName.getSelectionModel().clearSelection();

        itemCartTMObservableList.clear();
        repairCartTMObservableList.clear();

    }

    public void reset() {
        cmbItemId.getSelectionModel().clearSelection();
        cmbItemName.getSelectionModel().clearSelection();
        cmbRepairId.getSelectionModel().clearSelection();

        lblItemQty.setText("");
        lblItemPrice.setText("");
        lblRepairDescription.setText("");
        lblTotalPrice.setText("");
        txtAddToCartPrice.setText("");
        txtAddToCartQty.setText("");

        tblItem.refresh();
        tblRepair.refresh();
    }

    public void loadCustomerIds() throws SQLException {
        ArrayList<String> customerIds = customerBO.getAllIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(customerIds);
        cmbCustomerId.setItems(observableList);
    }

    public void loadCustomerNames() throws SQLException {
        ArrayList<String> customerNames = customerBO.getAllNames();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(customerNames);
        cmbCustomerName.setItems(observableList);
    }

    public void loadEmployeeIds() throws SQLException {
        ArrayList<String> employeeIds = employeeBO.getAllIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(employeeIds);
        cmbEmployeeId.setItems(observableList);
    }

    public void loadEmployeeNames() throws SQLException {
        ArrayList<String> employeeNames = employeeBO.getAllNames();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(employeeNames);
        cmbEmployeeName.setItems(observableList);
    }

    public void loadItemIds() throws SQLException {
        ArrayList<String> itemIds = itemBO.getAllIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemIds);
        cmbItemId.setItems(observableList);
    }

    public void loadItemNames() throws SQLException {
        ArrayList<String> itemNames = itemBO.getAllNames();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemNames);
        cmbItemName.setItems(observableList);
    }

    public void loadRepairIds() throws SQLException {
        ArrayList<String> repairIds = repairBO.getAllIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(repairIds);
        cmbRepairId.setItems(observableList);
    }

    @FXML
    void btnItemAddToCartOnAction(ActionEvent event) {
        String selectedItemId = cmbItemId.getValue();

        if (selectedItemId == null) {
            new Alert(Alert.AlertType.ERROR, "Please select item..!").show();
            return;
        }

        String cartQtyString = txtAddToCartQty.getText();

        String qtyPattern = "^[0-9]+$";

        if (!cartQtyString.matches(qtyPattern)){
            new Alert(Alert.AlertType.ERROR, "Please enter valid quantity..!").show();
            return;
        }

        String itemName = cmbItemName.getValue();
        int cartQty = Integer.parseInt(cartQtyString);
        int qtyOnHand = Integer.parseInt(lblItemQty.getText());

        if (qtyOnHand < cartQty) {
            new Alert(Alert.AlertType.ERROR, "Not enough items..!").show();
            return;
        }

        txtAddToCartQty.setText("");

        double unitPrice = Double.parseDouble(lblItemPrice.getText());
        double total = unitPrice * cartQty;

        for (ItemCartTM itemCartTM : itemCartTMObservableList) {
            if (itemCartTM.getItemId().equals(selectedItemId)) {
                int newQty = itemCartTM.getCartQuantity() + cartQty;
                itemCartTM.setCartQuantity(newQty);
                itemCartTM.setTotal(unitPrice * newQty);

                tblItem.refresh();
                return;
            }
        }

        Button btn = new Button("Remove");

        ItemCartTM newItemCartTM = new ItemCartTM(
                selectedItemId,
                itemName,
                cartQty,
                unitPrice,
                total,
                btn
        );

        btn.setOnAction(actionEvent -> {
            itemCartTMObservableList.remove(newItemCartTM);
            tblItem.refresh();
            calculateTotal();
        });

        itemCartTMObservableList.add(newItemCartTM);
        reset();
        calculateTotal();
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws SQLException {
        if (tblItem.getItems().isEmpty() && tblRepair.getItems().isEmpty()){
            if (tblItem.getItems().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please select item..!").show();
            } else if (tblRepair.getItems().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please select repair..!").show();
            }
            return;
        }

        if (cmbCustomerId.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select customer for place order..!").show();
            return;
        }

        if (cmbEmployeeId.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select employee for place order..!").show();
            return;
        }

        String orderId = lblOrderId.getText();
        Date dateOfOrder = Date.valueOf(orderDate.getText());
        Time timeOfOrder = Time.valueOf(LocalTime.now());
        String customerId = cmbCustomerId.getValue();
        String employeeId = cmbEmployeeId.getValue();
        String type = "";
        double total = Double.parseDouble(lblTotalPrice.getText());
        String status = "PENDING";

        ArrayList<OrdersItemDTO> ordersItemDTOs = new ArrayList<>();
        ArrayList<OrdersRepairDTO> ordersRepairDTOS = new ArrayList<>();

        for (ItemCartTM itemCartTM : itemCartTMObservableList) {
            OrdersItemDTO ordersItemDTO = new OrdersItemDTO(
                    orderId,
                    itemCartTM.getItemId(),
                    itemCartTM.getCartQuantity(),
                    itemCartTM.getUnitPrice()
            );

            ordersItemDTOs.add(ordersItemDTO);
        }

        for (RepairCartTM repairCartTM : repairCartTMObservableList) {
            OrdersRepairDTO ordersRepairDTO = new OrdersRepairDTO(
                    orderId,
                    repairCartTM.getRepairId(),
                    repairCartTM.getRepairPrice()
            );

            ordersRepairDTOS.add(ordersRepairDTO);
        }

        type = !ordersItemDTOs.isEmpty() && !ordersRepairDTOS.isEmpty() ? "items/repair" : !ordersItemDTOs.isEmpty() ? "items" : !ordersRepairDTOS.isEmpty() ? "repair" : "";

        OrdersDTO ordersDTO = new OrdersDTO(
                orderId,
                customerId,
                employeeId,
                dateOfOrder,
                timeOfOrder,
                type,
                total,
                status,
                ordersItemDTOs,
                ordersRepairDTOS
        );

        boolean isSaved = ordersBO.saveOrder(ordersDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Order saved..!").show();
            refreshPage();
            customerCmbDisable(false);
        } else {
            new Alert(Alert.AlertType.ERROR, "Order fail..!").show();
        }
    }

    @FXML
    void btnRepairAddToCartOnAction(ActionEvent event) throws SQLException {
        String selectedRepairId = cmbRepairId.getValue();

        if (selectedRepairId == null) {
            new Alert(Alert.AlertType.ERROR, "Please select repair..!").show();
            return;
        }

        if (!repairCartTMObservableList.isEmpty()) {
            String currentCustomerId = cmbCustomerId.getValue();
            String firstCustomerId = repairBO.searchRepairId(repairCartTMObservableList.get(0).getRepairId());

            if (!currentCustomerId.equals(firstCustomerId)){
                new Alert(Alert.AlertType.ERROR, "Please select a repair with a different customer..!").show();
                return;
            }
        }

        String cartPriceString = txtAddToCartPrice.getText();

        String pricePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

        if (!cartPriceString.matches(pricePattern)){
            new Alert(Alert.AlertType.ERROR, "Please enter valid price..!").show();
            return;
        }

        String repairDescription = lblRepairDescription.getText();

        double repairPrice = Double.parseDouble(String.valueOf(txtAddToCartPrice.getText()));

        txtAddToCartPrice.setText("");

        for (RepairCartTM repairCartTM : repairCartTMObservableList) {
            if (repairCartTM.getRepairId().equals(selectedRepairId)) {
                repairCartTM.setRepairPrice(repairPrice);

                tblRepair.refresh();
                return;
            }
        }

        Button btn = new Button("Remove");

        RepairCartTM newRepairCartTM = new RepairCartTM(
                selectedRepairId,
                repairDescription,
                repairPrice,
                btn
        );

        btn.setOnAction(actionEvent -> {
            repairCartTMObservableList.remove(newRepairCartTM);
            tblRepair.refresh();
            calculateTotal();
            customerCmbDisable(false);
            clearCmbCustomer();
        });

        repairCartTMObservableList.add(newRepairCartTM);
        reset();
        calculateTotal();
    }


    public void calculateTotal(){
        double total = 0;

        for (ItemCartTM itemCartTM : itemCartTMObservableList) {
            total += itemCartTM.getTotal();
        }

        for (RepairCartTM repairCartTM : repairCartTMObservableList) {
            total += repairCartTM.getRepairPrice();
        }

        lblTotalPrice.setText(String.format("%.2f", total));
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    public void customerCmbDisable(boolean b) {
        cmbCustomerId.setDisable(b);
        cmbCustomerName.setDisable(b);
    }

    public void clearCmbCustomer() {
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbCustomerName.getSelectionModel().clearSelection();
    }

    @FXML
    void cmbCustomerIdOnAction(ActionEvent event) throws SQLException {
        if (cmbCustomerId.getValue() == null) return;

        CustomerDTO customerDTO = customerBO.findById(cmbCustomerId.getValue());

        if (customerDTO != null) {
            cmbCustomerName.getSelectionModel().select(customerDTO.getCustomerName());
        }
    }

    @FXML
    void cmbCustomerNameOnAction(ActionEvent event) throws SQLException {
        if (cmbCustomerName.getValue() == null) return;

        CustomerDTO customerDTO = customerBO.findByName(cmbCustomerName.getValue());

        if (customerDTO != null) {
            cmbCustomerId.getSelectionModel().select(customerDTO.getCustomerId());
        }
    }

    @FXML
    void cmbEmployeeIdOnAction(ActionEvent event) throws SQLException {
        if (cmbEmployeeId.getValue() == null) return;

        EmployeeDTO employeeDTO = employeeBO.findById(cmbEmployeeId.getValue());

        if (employeeDTO != null) {
            cmbEmployeeName.getSelectionModel().select(employeeDTO.getEmployeeName());
        }
    }

    @FXML
    void cmbEmployeeNameOnAction(ActionEvent event) throws SQLException {
        if (cmbEmployeeName.getValue() == null) return;

        EmployeeDTO employeeDTO = employeeBO.findByName(cmbEmployeeName.getValue());

        if (employeeDTO != null) {
            cmbEmployeeId.getSelectionModel().select(employeeDTO.getEmployeeId());
        }
    }

    @FXML
    void cmbItemIdOnAction(ActionEvent event) throws SQLException {
        if (cmbItemId.getValue() == null) return;

        ItemDTO itemDTO = itemBO.findById(cmbItemId.getValue());

        if (itemDTO != null) {
            cmbItemName.getSelectionModel().select(itemDTO.getItemName());
        }
    }

    @FXML
    void cmbItemNameOnAction(ActionEvent event) throws SQLException {
        if (cmbItemName.getValue() == null) return;

        ItemDTO itemDTO = itemBO.findByName(cmbItemName.getValue());

        if (itemDTO != null) {
            cmbItemId.getSelectionModel().select(itemDTO.getItemId());
            lblItemQty.setText(String.valueOf(itemDTO.getQuantity()));
            lblItemPrice.setText(String.valueOf(itemDTO.getPrice()));
        }
    }

    @FXML
    void cmbRepairOnAction(ActionEvent event) throws SQLException {
        if (cmbRepairId.getValue() == null) return;

        RepairDTO repairDTO = repairBO.findById(cmbRepairId.getValue());

        if (repairDTO != null) {
            lblRepairDescription.setText(repairDTO.getDescription());
            CustomerDTO customerDTO = customerBO.findById(repairDTO.getCustomerId());
            cmbCustomerId.setValue(customerDTO.getCustomerId());
            cmbCustomerName.setValue(customerDTO.getCustomerName());
            customerCmbDisable(true);
        }
    }
}
