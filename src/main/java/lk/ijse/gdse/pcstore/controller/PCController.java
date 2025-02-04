package lk.ijse.gdse.pcstore.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.pcstore.dto.*;
import lk.ijse.gdse.pcstore.dto.tm.*;
import lk.ijse.gdse.pcstore.model.CategoryModel;
import lk.ijse.gdse.pcstore.model.ItemModel;
import lk.ijse.gdse.pcstore.model.PCItemModel;
import lk.ijse.gdse.pcstore.model.PCModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PCController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private ComboBox<String> cmbItemName;

    @FXML
    private TableColumn<PCCartTM, Button> colAction;

    @FXML
    private TableColumn<PCCartTM, String> colCategory;

    @FXML
    private TableColumn<PCTM, String> colDescription;

    @FXML
    private TableColumn<PCCartTM, String> colItemId;

    @FXML
    private TableColumn<PCCartTM, String> colName;

    @FXML
    private TableColumn<PCTM, String> colPCId;

    @FXML
    private TableColumn<PCTM, Double> colPCTotal;

    @FXML
    private TableColumn<PCCartTM, Double> colPrice;

    @FXML
    private TableColumn<PCCartTM, Integer> colQuantity;

    @FXML
    private TableColumn<PCCartTM, Double> colTotal;

    @FXML
    private Label lblItemPrice;

    @FXML
    private Label lblItemQty;

    @FXML
    private Label lblPCId;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private TableView<PCCartTM> tblItemCart;

    @FXML
    private TableView<PCTM> tblPC;

    @FXML
    private TextField txtAddToCartQty;

    @FXML
    private TextField txtPCDescription;

    private final PCModel pcModel = new PCModel();
    private final PCItemModel pcItemModel = new PCItemModel();
    private final ItemModel itemModel = new ItemModel();
    private final CategoryModel categoryModel = new CategoryModel();

    private final ObservableList<PCCartTM> pcCartTMObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();

        try {
            refreshPage();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCellValues(){
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("cartQuantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("removeBtn"));

        tblItemCart.setItems(pcCartTMObservableList);

        colPCId.setCellValueFactory(new PropertyValueFactory<>("pcId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPCTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    public void refreshPage() throws SQLException {
        loadNextPCId();
        loadTableData();
        loadCategories();

        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        txtPCDescription.setText("");

        reset();

        pcCartTMObservableList.clear();
    }

    public void reset() {
        cmbCategory.getSelectionModel().clearSelection();
        cmbItemName.getSelectionModel().clearSelection();

        txtAddToCartQty.setText("");
        lblTotalPrice.setText("");
        lblItemQty.setText("");
        lblItemPrice.setText("");

        tblItemCart.refresh();
    }

    public void loadTableData() throws SQLException {
        ArrayList<PCDTO> pcdtos = pcModel.getAllPCs();

        ObservableList<PCTM> pctms = FXCollections.observableArrayList();

        for (PCDTO pcdto : pcdtos) {
            PCTM pctm = new PCTM(
                    pcdto.getPcId(),
                    pcdto.getDescription(),
                    pcdto.getTotalCost()
            );
            pctms.add(pctm);
        }

        tblPC.setItems(pctms);
    }

    public void loadNextPCId() throws SQLException {
        String nextPCId = pcModel.getNextPCId();
        lblPCId.setText(nextPCId);
    }

    public void loadCategories() throws SQLException {
        ArrayList<String> categories = categoryModel.getAllCategoryIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(categories);
        cmbCategory.setItems(observableList);
    }

    public void loadItemNames(String categoryId) throws SQLException {
        ArrayList<String> itemNames = itemModel.getAllItemNamesForCategory(categoryId);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemNames);
        cmbItemName.setItems(observableList);
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) throws SQLException {
        String selectedItemId = itemModel.findByName(cmbItemName.getValue()).getItemId();

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
        String category = cmbCategory.getValue();
        int cartQty = Integer.parseInt(cartQtyString);
        int qtyOnHand = Integer.parseInt(lblItemQty.getText());

        if (qtyOnHand < cartQty) {
            new Alert(Alert.AlertType.ERROR, "Not enough items..!").show();
            return;
        }

        txtAddToCartQty.setText("");

        double unitPrice = Double.parseDouble(lblItemPrice.getText());
        double total = unitPrice * cartQty;

        for (PCCartTM pcCartTM : pcCartTMObservableList) {
            if (pcCartTM.getItemId().equals(selectedItemId)) {
                int newQty = pcCartTM.getCartQuantity() + cartQty;
                pcCartTM.setCartQuantity(newQty);
                pcCartTM.setTotal(unitPrice * newQty);

                tblItemCart.refresh();
                return;
            }
        }

        Button btn = new Button("Remove");

        PCCartTM pcCartTM = new PCCartTM(
                selectedItemId,
                itemName,
                category,
                unitPrice,
                cartQty,
                total,
                btn
        );

        btn.setOnAction(actionEvent -> {
            pcCartTMObservableList.remove(pcCartTM);
            reset();
            calculateTotal();
        });

        pcCartTMObservableList.add(pcCartTM);
        reset();
        calculateTotal();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String pcId = lblPCId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = pcModel.deletePC(pcId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "PC deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete pc...!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        if (tblItemCart.getItems().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please select item..!").show();
            return;
        }

        String pcId = lblPCId.getText();
        String description = txtPCDescription.getText();
        double total = Double.parseDouble(lblTotalPrice.getText());

        ArrayList<PCItemDTO> pcItemDTOS = new ArrayList<>();

        for (PCCartTM pcCartTM : pcCartTMObservableList) {
            PCItemDTO pcItemDTO = new PCItemDTO(
                    pcId,
                    pcCartTM.getItemId(),
                    pcCartTM.getCartQuantity(),
                    pcCartTM.getUnitPrice()
            );

            pcItemDTOS.add(pcItemDTO);
        }

        PCDTO pcDTO = new PCDTO(
                pcId,
                description,
                total,
                pcItemDTOS
        );

        boolean isSaved = pcModel.savePC(pcDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "PC saved..!").show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "PC fail..!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String pcId = lblPCId.getText();
        String description = txtPCDescription.getText();
        double total = Double.parseDouble(lblTotalPrice.getText());

        ArrayList<PCItemDTO> pcItemDTOS = new ArrayList<>();

        for (PCCartTM pcCartTM : pcCartTMObservableList) {
            PCItemDTO pcItemDTO = new PCItemDTO(
                    pcId,
                    pcCartTM.getItemId(),
                    pcCartTM.getCartQuantity(),
                    pcCartTM.getUnitPrice()
            );

            pcItemDTOS.add(pcItemDTO);
        }

        PCDTO pcDTO = new PCDTO(
                pcId,
                description,
                total,
                pcItemDTOS
        );

        System.out.println(pcDTO);

        boolean isUpdated = pcModel.updatePC(pcDTO);

        if (isUpdated) {
            new Alert(Alert.AlertType.INFORMATION, "PC updated..!").show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "PC fail..!").show();
        }
    }

    @FXML
    void cmbCategoryOnAction(ActionEvent event) throws SQLException {
        String categoryId = categoryModel.findByDescription(cmbCategory.getSelectionModel().getSelectedItem());

        if (categoryId != null) {
            loadItemNames(categoryId);
        }
    }

    @FXML
    void cmbItemNameOnAction(ActionEvent event) throws SQLException {
        ItemDTO itemDTO = itemModel.findByName(cmbItemName.getValue());

        if (itemDTO != null) {
            lblItemQty.setText(String.valueOf(itemDTO.getQuantity()));
            lblItemPrice.setText(String.valueOf(itemDTO.getPrice()));
        }
    }

    public void calculateTotal(){
        double total = 0;

        for (PCCartTM pcCartTM : pcCartTMObservableList) {
            total += pcCartTM.getTotal();
        }

        lblTotalPrice.setText(String.format("%.2f", total));
    }

    @FXML
    void onClickTable(MouseEvent event) throws SQLException {
        PCTM pctm = tblPC.getSelectionModel().getSelectedItem();
        if (pctm != null) {
            refreshPage();
            ArrayList<PCItemDTO> pcItemDTOS = pcItemModel.findById(pctm.getPcId());

            for (PCItemDTO pcItemDTO : pcItemDTOS) {
                Button btn = new Button("Remove");

                PCCartTM pcCartTM = new PCCartTM(
                        pcItemDTO.getItemId(),
                        itemModel.findById(pcItemDTO.getItemId()).getItemName(),
                        categoryModel.findById(itemModel.getCategory(pcItemDTO.getItemId())),
                        pcItemDTO.getPrice(),
                        pcItemDTO.getQty(),
                        pcItemDTO.getQty() * pcItemDTO.getPrice(),
                        btn
                );

                btn.setOnAction(actionEvent -> {
                    pcCartTMObservableList.remove(pcCartTM);
                    reset();
                    calculateTotal();
                });
                pcCartTMObservableList.add(pcCartTM);
            }

            calculateTotal();

            lblPCId.setText(pctm.getPcId());
            txtPCDescription.setText(pctm.getDescription());

            btnSave.setDisable(true);

            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }

}
