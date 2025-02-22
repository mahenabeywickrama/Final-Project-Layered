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
import lk.ijse.gdse.pcstore.bo.custom.ItemBO;
import lk.ijse.gdse.pcstore.bo.custom.OrdersBO;
import lk.ijse.gdse.pcstore.bo.custom.ReplacementBO;
import lk.ijse.gdse.pcstore.dto.ItemDTO;
import lk.ijse.gdse.pcstore.dto.ReplacementDTO;
import lk.ijse.gdse.pcstore.dto.tm.ReplacementTM;

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

    ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.ITEM);
    OrdersBO ordersBO = (OrdersBO) BOFactory.getInstance().getBO(BOFactory.BOType.ORDERS);
    ReplacementBO replacementBO = (ReplacementBO) BOFactory.getInstance().getBO(BOFactory.BOType.REPLACEMENT);

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

    public void loadTableData() throws SQLException {
        ArrayList<ReplacementDTO> replacementDTOS = replacementBO.getAllReplacements();

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
        String nextReplacementId = replacementBO.getNextReplacementId();
        lblReplacementId.setText(nextReplacementId);
    }

    public void loadOrders() throws SQLException {
        ArrayList<String> ordersIds = ordersBO.getAllOrdersIdsPaid();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(ordersIds);
        cmbOrdersId.setItems(observableList);
    }

    public void loadItemNames(String ordersId) throws SQLException {
        ArrayList<String> itemIds = ordersBO.getAllItemsFromOrders(ordersId);
        ArrayList<String> itemNames = new ArrayList<>();

        for (String itemId : itemIds) {
            String itemName = itemBO.findById(itemId).getItemName();
            if (itemName != null) {
                itemNames.add(itemName);
            }
        }

        ObservableList<String> observableList = FXCollections.observableArrayList(itemNames);
        cmbFaultyItemName.setItems(observableList);
    }

    public void loadSimilarItemNames(ItemDTO itemDTO) throws SQLException {
        String categoryId = itemDTO.getCategoryId();
        ArrayList<String> itemNames = itemBO.getAllItemNamesForCategory(categoryId);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemNames);
        cmbReplacedItemName.setItems(observableList);
    }

    public boolean isEligible() throws SQLException {
        LocalDate today = LocalDate.parse(replacementDate.getText());
        LocalDate paidDay = LocalDate.parse(ordersBO.findOrdersDate(cmbOrdersId.getValue()));
        int warranty = itemBO.findByName(cmbFaultyItemName.getValue()).getWarranty();
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
        String faultyItemId = itemBO.findByName(cmbFaultyItemName.getValue()).getItemId();
        String replacedItemId = itemBO.findByName(cmbReplacedItemName.getValue()).getItemId();
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

            boolean isDeleted = replacementBO.deleteReplacement(replacementId);
            if (isDeleted) {
                refreshPage();
                itemBO.increaseQtyByReplacement(replacementDTO);
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
        String faultyItemId = itemBO.findByName(cmbFaultyItemName.getValue()).getItemId();
        String replacedItemId = itemBO.findByName(cmbReplacedItemName.getValue()).getItemId();
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

        boolean isSaved = replacementBO.saveReplacement(replacementDTO);
        if (isSaved) {
            refreshPage();
            itemBO.reduceQtyByReplacement(replacementDTO);
            new Alert(Alert.AlertType.INFORMATION, "Replacement saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save replacement...!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String replacementId = lblReplacementId.getText();
        String ordersId = cmbOrdersId.getValue();
        String faultyItemId = itemBO.findByName(cmbFaultyItemName.getValue()).getItemId();
        String replacedItemId = itemBO.findByName(cmbReplacedItemName.getValue()).getItemId();
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

        itemBO.increaseQtyByReplacement(replacementDTO);

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

        boolean isSaved = replacementBO.updateReplacement(replacementDTO);
        if (isSaved) {
            refreshPage();
            itemBO.reduceQtyByReplacement(replacementDTO);
            new Alert(Alert.AlertType.INFORMATION, "Replacement updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update replacement...!").show();
        }
    }

    @FXML
    void cmbFaultyItemNameOnAction(ActionEvent event) throws SQLException {
        if (cmbFaultyItemName.getValue() == null) return;

        ItemDTO itemDTO = itemBO.findByName(cmbFaultyItemName.getValue());

        if (itemDTO != null) {
            txtQty.setText(ordersBO.findQty(itemDTO.getItemId()));
            txtQty.setDisable(true);
            loadSimilarItemNames(itemDTO);
        }
    }

    @FXML
    void cmbOrdersIdOnAction(ActionEvent event) throws SQLException {
        if (cmbOrdersId.getValue() == null) return;

        String ordersId = cmbOrdersId.getValue();

        if (ordersId != null) {
            loadItemNames(ordersId);
        }
    }

    @FXML
    void cmbReplacedItemNameOnAction(ActionEvent event) throws SQLException {
        if (cmbReplacedItemName.getValue() == null) return;

        ItemDTO itemDTO = itemBO.findByName(cmbReplacedItemName.getValue());

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
            cmbFaultyItemName.setValue(itemBO.findById(replacementTM.getFaultyItemId()).getItemName());
            cmbReplacedItemName.setValue(itemBO.findById(replacementTM.getReplacedItemId()).getItemName());
            itemIdForUpdate = itemBO.findById(replacementTM.getReplacedItemId()).getItemName();
            lblItemQtyOnHand.setText(Integer.toString(itemBO.findById(replacementTM.getReplacedItemId()).getQuantity()));
            lblItemPrice.setText(Double.toString(itemBO.findById(replacementTM.getReplacedItemId()).getPrice()));
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
