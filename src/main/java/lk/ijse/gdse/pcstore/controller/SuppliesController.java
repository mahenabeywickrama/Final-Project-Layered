package lk.ijse.gdse.pcstore.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.gdse.pcstore.dto.*;
import lk.ijse.gdse.pcstore.dto.tm.ItemCartTM;
import lk.ijse.gdse.pcstore.dto.tm.RepairCartTM;
import lk.ijse.gdse.pcstore.dto.tm.SuppliesCartTM;
import lk.ijse.gdse.pcstore.model.ItemModel;
import lk.ijse.gdse.pcstore.model.SupplierModel;
import lk.ijse.gdse.pcstore.model.SuppliesModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SuppliesController implements Initializable {

    @FXML
    private ComboBox<String> cmbItemId;

    @FXML
    private ComboBox<String> cmbItemName;

    @FXML
    private ComboBox<String> cmbSupplierId;

    @FXML
    private ComboBox<String> cmbSupplierName;

    @FXML
    private TableColumn<SuppliesCartTM, Button> colAction;

    @FXML
    private TableColumn<SuppliesCartTM, String> colItemId;

    @FXML
    private TableColumn<SuppliesCartTM, String> colName;

    @FXML
    private TableColumn<SuppliesCartTM, Integer> colNewQty;

    @FXML
    private TableColumn<SuppliesCartTM, Integer> colOldQty;

    @FXML
    private TableColumn<SuppliesCartTM, Double> colPrice;

    @FXML
    private TableColumn<SuppliesCartTM, Integer> colQuantity;

    @FXML
    private TableColumn<SuppliesCartTM, Double> colTotal;

    @FXML
    private Label lblItemPrice;

    @FXML
    private Label lblItemQty;

    @FXML
    private Label lblSuppliesId;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private Label suppliesDate;

    @FXML
    private TableView<SuppliesCartTM> tblItem;

    @FXML
    private TextField txtAddToCartQty;

    private final SuppliesModel suppliesModel = new SuppliesModel();
    private final SupplierModel supplierModel = new SupplierModel();
    private final ItemModel itemModel = new ItemModel();

    private final ObservableList<SuppliesCartTM> suppliesCartTMObservableList = FXCollections.observableArrayList();

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
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colOldQty.setCellValueFactory(new PropertyValueFactory<>("oldQuantity"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colNewQty.setCellValueFactory(new PropertyValueFactory<>("newQuantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("delete"));

        tblItem.setItems(suppliesCartTMObservableList);
    }

    public void refreshPage() throws SQLException {
        lblSuppliesId.setText(suppliesModel.getNextSuppliesId());
        suppliesDate.setText(LocalDate.now().toString());

        loadSupplierIds();
        loadSupplierNames();
        loadItemIds();
        loadItemNames();

        cmbSupplierId.getSelectionModel().clearSelection();
        cmbSupplierName.getSelectionModel().clearSelection();

        reset();

        suppliesCartTMObservableList.clear();
        tblItem.refresh();
    }

    public void reset() {
        cmbItemId.getSelectionModel().clearSelection();
        cmbItemName.getSelectionModel().clearSelection();

        lblItemQty.setText("");
        lblItemPrice.setText("");
        lblTotalPrice.setText("");
        txtAddToCartQty.setText("");
    }

    public void loadSupplierIds() throws SQLException {
        ArrayList<String> supplierIds = supplierModel.getAllSupplierIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(supplierIds);
        cmbSupplierId.setItems(observableList);
    }

    public void loadSupplierNames() throws SQLException {
        ArrayList<String> supplierNames = supplierModel.getAllSupplierNames();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(supplierNames);
        cmbSupplierName.setItems(observableList);
    }

    public void loadItemIds() throws SQLException {
        ArrayList<String> itemIds = itemModel.getAllItemIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemIds);
        cmbItemId.setItems(observableList);
    }

    public void loadItemNames() throws SQLException {
        ArrayList<String> itemNames = itemModel.getAllItemNames();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemNames);
        cmbItemName.setItems(observableList);
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
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

        for (SuppliesCartTM suppliesCartTM : suppliesCartTMObservableList) {
            if (suppliesCartTM.getItemId().equals(selectedItemId)) {
                int newQty = suppliesCartTM.getQuantity() + cartQty;
                suppliesCartTM.setQuantity(newQty);
                suppliesCartTM.setTotal(unitPrice * newQty);
                suppliesCartTM.setNewQuantity(newQty + qtyOnHand);
                tblItem.refresh();
                return;
            }
        }

        Button btn = new Button("Remove");

        SuppliesCartTM suppliesCartTM = new SuppliesCartTM(
                selectedItemId,
                itemName,
                cartQty,
                unitPrice,
                qtyOnHand,
                cartQty + qtyOnHand,
                total,
                btn
        );

        btn.setOnAction(actionEvent -> {
            suppliesCartTMObservableList.remove(suppliesCartTM);
            tblItem.refresh();
            calculateTotal();
        });

        suppliesCartTMObservableList.add(suppliesCartTM);
        reset();
        calculateTotal();
    }

    @FXML
    void btnPlaceSuppliesOnAction(ActionEvent event) throws SQLException {
        if (tblItem.getItems().isEmpty()){
            if (tblItem.getItems().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please select item..!").show();
            }
            return;
        }

        if (cmbSupplierId.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select supplier for place order..!").show();
            return;
        }

        String suppliesId = lblSuppliesId.getText();
        Date dateOfOrder = Date.valueOf(suppliesDate.getText());
        Time timeOfOrder = Time.valueOf(LocalTime.now());
        String supplierId = cmbSupplierId.getValue();
        double total = Double.parseDouble(lblTotalPrice.getText());
        String status = "PENDING";

        ArrayList<SuppliesItemDTO> suppliesItemDTOS = new ArrayList<>();

        for (SuppliesCartTM suppliesCartTM : suppliesCartTMObservableList) {
            SuppliesItemDTO suppliesItemDTO = new SuppliesItemDTO(
                    suppliesId,
                    suppliesCartTM.getItemId(),
                    suppliesCartTM.getQuantity(),
                    suppliesCartTM.getTotal()
            );

            suppliesItemDTOS.add(suppliesItemDTO);
        }

        SuppliesDTO suppliesDTO = new SuppliesDTO(
                suppliesId,
                dateOfOrder,
                timeOfOrder,
                supplierId,
                suppliesItemDTOS
        );

        boolean isSaved = suppliesModel.saveSupplies(suppliesDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Supplies saved..!").show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Supplies fail..!").show();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void cmbItemIdOnAction(ActionEvent event) throws SQLException {
        ItemDTO itemDTO = itemModel.findById(cmbItemId.getValue());

        if (itemDTO != null) {
            cmbItemName.getSelectionModel().select(itemDTO.getItemName());
        }
    }

    @FXML
    void cmbItemNameOnAction(ActionEvent event) throws SQLException {
        ItemDTO itemDTO = itemModel.findByName(cmbItemName.getValue());

        if (itemDTO != null) {
            cmbItemId.getSelectionModel().select(itemDTO.getItemId());
            lblItemQty.setText(String.valueOf(itemDTO.getQuantity()));
            lblItemPrice.setText(String.valueOf(itemDTO.getPrice()));
        }
    }

    @FXML
    void cmbSupplierIdOnAction(ActionEvent event) throws SQLException {
        SupplierDTO supplierDTO = supplierModel.findById(cmbSupplierId.getValue());

        if (supplierDTO != null) {
            cmbSupplierName.getSelectionModel().select(supplierDTO.getSupplierName());
        }
    }

    @FXML
    void cmbSupplierNameOnAction(ActionEvent event) throws SQLException {
        SupplierDTO supplierDTO = supplierModel.findByName(cmbSupplierName.getValue());

        if (supplierDTO != null) {
            cmbSupplierId.getSelectionModel().select(supplierDTO.getSupplierId());
        }
    }

    public void calculateTotal(){
        double total = 0;

        for (SuppliesCartTM suppliesCartTM : suppliesCartTMObservableList) {
            total += suppliesCartTM.getTotal();
        }

        lblTotalPrice.setText(String.format("%.2f", total));
    }
}
