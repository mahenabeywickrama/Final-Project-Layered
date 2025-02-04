package lk.ijse.gdse.pcstore.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.pcstore.dto.ItemDTO;
import lk.ijse.gdse.pcstore.dto.PaymentDTO;
import lk.ijse.gdse.pcstore.dto.ReplacementDTO;
import lk.ijse.gdse.pcstore.dto.tm.PaymentTM;
import lk.ijse.gdse.pcstore.dto.tm.ReplacementTM;
import lk.ijse.gdse.pcstore.model.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReplacementController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbFaultyItemName;

    @FXML
    private ComboBox<String> cmbOrdersId;

    @FXML
    private ComboBox<String> cmbReplacedItemName;

    @FXML
    private TableColumn<ReplacementTM, LocalDate> colDate;

    @FXML
    private TableColumn<ReplacementTM, String> colFaultyItem;

    @FXML
    private TableColumn<ReplacementTM, String> colOrdersId;

    @FXML
    private TableColumn<ReplacementTM, Integer> colQty;

    @FXML
    private TableColumn<ReplacementTM, String> colRemarks;

    @FXML
    private TableColumn<ReplacementTM, String> colReplacedItem;

    @FXML
    private TableColumn<ReplacementTM, String> colReplacementId;

    @FXML
    private TableColumn<ReplacementTM, LocalTime> colTime;

    @FXML
    private Label lblItemPrice;

    @FXML
    private Label lblItemQtyOnHand;

    @FXML
    private Label lblReplacementId;

    @FXML
    private Label replacementDate;

    @FXML
    private TableView<ReplacementTM> tblReplacement;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtRemarks;

    private final OrdersModel ordersModel = new OrdersModel();
    private final OrdersItemModel ordersItemModel = new OrdersItemModel();
    private final ItemModel itemModel = new ItemModel();

    private int qtyForUpdate = 0;
    private String itemIdForUpdate = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colReplacementId.setCellValueFactory(new PropertyValueFactory<>("replacementId"));
        colOrdersId.setCellValueFactory(new PropertyValueFactory<>("ordersId"));
        colFaultyItem.setCellValueFactory(new PropertyValueFactory<>("faultyItemId"));
        colReplacedItem.setCellValueFactory(new PropertyValueFactory<>("replacedItemId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colRemarks.setCellValueFactory(new PropertyValueFactory<>("remarks"));

        try {
            refreshPage();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load replacement id").show();
        }
    }

    public void refreshPage() throws SQLException {
        loadNextReplacementId();
        loadTableData();
        replacementDate.setText(LocalDate.now().toString());

        cmbFaultyItemName.getSelectionModel().clearSelection();
        cmbOrdersId.getSelectionModel().clearSelection();
        cmbReplacedItemName.getSelectionModel().clearSelection();

        loadOrders();

        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        lblItemQtyOnHand.setText("");
        lblItemPrice.setText("");

        txtQty.setText("");
        txtQty.setDisable(false);
        txtRemarks.setText("");
    }

    ReplacementModel replacementModel = new ReplacementModel();

    public void loadTableData() throws SQLException {
        ArrayList<ReplacementDTO> replacementDTOS = replacementModel.getAllReplacements();

        ObservableList<ReplacementTM> replacementTMS = FXCollections.observableArrayList();

        for (ReplacementDTO replacementDTO : replacementDTOS) {
            ReplacementTM replacementTM = new ReplacementTM(
                    replacementDTO.getReplacementId(),
                    replacementDTO.getOrdersId(),
                    replacementDTO.getFaultyItemId(),
                    replacementDTO.getReplacedItemId(),
                    replacementDTO.getQty(),
                    replacementDTO.getDate(),
                    replacementDTO.getTime(),
                    replacementDTO.getRemarks()
            );
            replacementTMS.add(replacementTM);
        }

        tblReplacement.setItems(replacementTMS);
    }

    public void loadNextReplacementId() throws SQLException {
        String nextReplacementId = replacementModel.getNextReplacementId();
        lblReplacementId.setText(nextReplacementId);
    }

    public void loadOrders() throws SQLException {
        ArrayList<String> ordersIds = ordersModel.getAllOrdersIdsPaid();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(ordersIds);
        cmbOrdersId.setItems(observableList);
    }

    public void loadItemNames(String ordersId) throws SQLException {
        ArrayList<String> itemIds = ordersItemModel.getAllItemsFromOrders(ordersId);
        ArrayList<String> itemNames = new ArrayList<>();

        for (String itemId : itemIds) {
            String itemName = itemModel.findById(itemId).getItemName();
            if (itemName != null) {
                itemNames.add(itemName);
            }
        }

        ObservableList<String> observableList = FXCollections.observableArrayList(itemNames);
        cmbFaultyItemName.setItems(observableList);
    }

    public void loadSimilarItemNames(ItemDTO itemDTO) throws SQLException {
        String categoryId = itemDTO.getCategoryId();
        ArrayList<String> itemNames = itemModel.getAllItemNamesForCategory(categoryId);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemNames);
        cmbReplacedItemName.setItems(observableList);
    }

    public boolean isEligible() throws SQLException {
        LocalDate today = LocalDate.parse(replacementDate.getText());
        LocalDate paidDay = LocalDate.parse(ordersModel.findOrdersDate(cmbOrdersId.getValue()));
        int warranty = itemModel.findByName(cmbFaultyItemName.getValue()).getWarranty();
        LocalDate warrantyExpiryDate = paidDay.plusMonths(warranty);

        if (today.isAfter(warrantyExpiryDate)) {
            return true;
        } else {
            return false;
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String replacementId = lblReplacementId.getText();
        String ordersId = cmbOrdersId.getValue();
        String faultyItemId = itemModel.findByName(cmbFaultyItemName.getValue()).getItemId();
        String replacedItemId = itemModel.findByName(cmbReplacedItemName.getValue()).getItemId();
        int qty = Integer.parseInt(txtQty.getText());
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String remarks = txtRemarks.getText();
        int qtyOnHand = Integer.parseInt(lblItemQtyOnHand.getText());

        if (qtyOnHand < qty) {
            new Alert(Alert.AlertType.INFORMATION, "Insufficient qty...!").show();
            return;
        }

        ReplacementDTO replacementDTO = new ReplacementDTO(
                replacementId,
                ordersId,
                faultyItemId,
                replacedItemId,
                qty,
                date,
                time,
                remarks
        );

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = replacementModel.deleteReplacement(replacementId);
            if (isDeleted) {
                refreshPage();
                itemModel.increaseQtyByReplacement(replacementDTO);
                new Alert(Alert.AlertType.INFORMATION, "Replacement deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete replacement...!").show();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String replacementId = lblReplacementId.getText();
        String ordersId = cmbOrdersId.getValue();
        String faultyItemId = itemModel.findByName(cmbFaultyItemName.getValue()).getItemId();
        String replacedItemId = itemModel.findByName(cmbReplacedItemName.getValue()).getItemId();
        int qty = Integer.parseInt(txtQty.getText());
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String remarks = txtRemarks.getText();
        int qtyOnHand = Integer.parseInt(lblItemQtyOnHand.getText());

        if (qtyOnHand < qty) {
            new Alert(Alert.AlertType.INFORMATION, "Insufficient qty...!").show();
            return;
        }

        if (isEligible()){
            new Alert(Alert.AlertType.INFORMATION, "This item is not eligible for a warranty...!").show();
            return;
        }

        ReplacementDTO replacementDTO = new ReplacementDTO(
                replacementId,
                ordersId,
                faultyItemId,
                replacedItemId,
                qty,
                date,
                time,
                remarks
        );

        boolean isSaved = replacementModel.saveReplacement(replacementDTO);
        if (isSaved) {
            refreshPage();
            itemModel.reduceQtyByReplacement(replacementDTO);
            new Alert(Alert.AlertType.INFORMATION, "Replacement saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save replacement...!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String replacementId = lblReplacementId.getText();
        String ordersId = cmbOrdersId.getValue();
        String faultyItemId = itemModel.findByName(cmbFaultyItemName.getValue()).getItemId();
        String replacedItemId = itemModel.findByName(cmbReplacedItemName.getValue()).getItemId();
        int qty = Integer.parseInt(txtQty.getText());
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String remarks = txtRemarks.getText();
        int qtyOnHand = Integer.parseInt(lblItemQtyOnHand.getText());

        if (qtyOnHand < qty) {
            new Alert(Alert.AlertType.INFORMATION, "Insufficient qty...!").show();
            return;
        }

        ReplacementDTO replacementDTO = new ReplacementDTO(
                replacementId,
                ordersId,
                faultyItemId,
                itemIdForUpdate,
                qtyForUpdate,
                date,
                time,
                remarks
        );

        itemModel.increaseQtyByReplacement(replacementDTO);

        replacementDTO = new ReplacementDTO(
                replacementId,
                ordersId,
                faultyItemId,
                replacedItemId,
                qty,
                date,
                time,
                remarks
        );

        boolean isSaved = replacementModel.updateReplacement(replacementDTO);
        if (isSaved) {
            refreshPage();
            itemModel.reduceQtyByReplacement(replacementDTO);
            new Alert(Alert.AlertType.INFORMATION, "Replacement updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update replacement...!").show();
        }
    }

    @FXML
    void cmbFaultyItemNameOnAction(ActionEvent event) throws SQLException {
        ItemDTO itemDTO = itemModel.findByName(cmbFaultyItemName.getValue());

        if (itemDTO != null) {
            txtQty.setText(ordersItemModel.findQty(itemDTO.getItemId()));
            txtQty.setDisable(true);
            loadSimilarItemNames(itemDTO);
        }
    }

    @FXML
    void cmbOrdersIdOnAction(ActionEvent event) throws SQLException {
        String ordersId = cmbOrdersId.getValue();

        if (ordersId != null) {
            loadItemNames(ordersId);
        }
    }

    @FXML
    void cmbReplacedItemNameOnAction(ActionEvent event) throws SQLException {
        ItemDTO itemDTO = itemModel.findByName(cmbReplacedItemName.getValue());

        if (itemDTO != null) {
            lblItemPrice.setText(Double.toString(itemDTO.getPrice()));
            lblItemQtyOnHand.setText(Integer.toString(itemDTO.getQuantity()));
        }
    }

    @FXML
    void onClickTable(MouseEvent event) throws SQLException {
        ReplacementTM replacementTM = tblReplacement.getSelectionModel().getSelectedItem();
        if (replacementTM != null) {
            lblReplacementId.setText(replacementTM.getReplacementId());
            cmbOrdersId.setValue(replacementTM.getOrdersId());
            cmbFaultyItemName.setValue(itemModel.findById(replacementTM.getFaultyItemId()).getItemName());
            cmbReplacedItemName.setValue(itemModel.findById(replacementTM.getReplacedItemId()).getItemName());
            itemIdForUpdate = itemModel.findById(replacementTM.getReplacedItemId()).getItemName();
            lblItemQtyOnHand.setText(Integer.toString(itemModel.findById(replacementTM.getReplacedItemId()).getQuantity()));
            lblItemPrice.setText(Double.toString(itemModel.findById(replacementTM.getReplacedItemId()).getPrice()));
            txtQty.setText(Integer.toString(replacementTM.getQty()));
            qtyForUpdate = replacementTM.getQty();
            txtRemarks.setText(replacementTM.getRemarks());

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
