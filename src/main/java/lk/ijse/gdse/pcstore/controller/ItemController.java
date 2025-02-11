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
import lk.ijse.gdse.pcstore.bo.custom.CategoryBO;
import lk.ijse.gdse.pcstore.bo.custom.ItemBO;
import lk.ijse.gdse.pcstore.dto.CategoryDTO;
import lk.ijse.gdse.pcstore.dto.ItemDTO;
import lk.ijse.gdse.pcstore.dto.tm.ItemTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

    @FXML
    private Button btnAddCategory;

    @FXML
    private Button btnDeleteItem;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSaveItem;

    @FXML
    private Button btnUpdateItem;

    @FXML
    private ComboBox<String> cmbCategoryId;

    @FXML
    private TableColumn<ItemTM, String> colCategoryId;

    @FXML
    private TableColumn<ItemTM, String> colItemId;

    @FXML
    private TableColumn<ItemTM, String> colName;

    @FXML
    private TableColumn<ItemTM, Double> colPrice;

    @FXML
    private TableColumn<ItemTM, Integer> colQuantity;

    @FXML
    private TableColumn<ItemTM, Integer> colWarranty;

    @FXML
    private Label lblCategoryName;

    @FXML
    private Label lblItemId;

    @FXML
    private TableView<ItemTM> tblItem;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNewCategory;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtWarranty;

    CategoryBO categoryBO = (CategoryBO) BOFactory.getInstance().getBO(BOFactory.BOType.CATEGORY);
    ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.ITEM);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colCategoryId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colWarranty.setCellValueFactory(new PropertyValueFactory<>("warranty"));

        try {
            refreshPage();
            loadCmbCategoryId();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load item id").show();
        }
    }

    public void refreshPage() throws SQLException {
        loadNextItemId();
        loadTableData();

        btnSaveItem.setDisable(false);
        btnDeleteItem.setDisable(true);
        btnUpdateItem.setDisable(true);

        cmbCategoryId.getSelectionModel().clearSelection();
        txtName.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        txtWarranty.setText("");
    }

    public void loadTableData() throws SQLException {
        ArrayList<ItemDTO> itemDTOS = itemBO.getAll();
        ObservableList<ItemTM> itemTMS = FXCollections.observableArrayList();

        for (ItemDTO itemDTO : itemDTOS) {
            ItemTM itemTM = new ItemTM(
                    itemDTO.getItemId(),
                    itemDTO.getCategoryId(),
                    itemDTO.getItemName(),
                    itemDTO.getPrice(),
                    itemDTO.getQuantity(),
                    itemDTO.getWarranty()
            );
            itemTMS.add(itemTM);
        }

        tblItem.setItems(itemTMS);
    }

    public void loadNextItemId() throws SQLException {
        String nextItemId = itemBO.getNextId();
        lblItemId.setText(nextItemId);
    }

    public void loadCmbCategoryId() throws SQLException {
        ArrayList<String> categoryIds = categoryBO.getAllIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(categoryIds);
        cmbCategoryId.setItems(observableList);
    }

    @FXML
    void btnAddCategoryOnAction(ActionEvent event) throws SQLException {
        if (txtNewCategory.getText().isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Enter a new category name").show();
        } else {
            try {
                String categoryId = categoryBO.getNextId();
                String description = txtNewCategory.getText();

                CategoryDTO categoryDTO = new CategoryDTO(categoryId, description);

                boolean isSaved = categoryBO.save(categoryDTO);
                if (isSaved) {
                    txtNewCategory.setText("");
                    loadCmbCategoryId();
                    new Alert(Alert.AlertType.INFORMATION, "Category saved...!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to save Category...!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnDeleteItemOnAction(ActionEvent event) throws SQLException {
        String itemId = lblItemId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = itemBO.delete(itemId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Item deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete item...!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void btnSaveItemOnAction(ActionEvent event) throws SQLException {
        String itemId = lblItemId.getText();
        String categoryId = categoryBO.findByName(cmbCategoryId.getValue()).getCategoryId();
        String name = txtName.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int quantity = Integer.parseInt(txtQuantity.getText());
        int warranty = Integer.parseInt(txtWarranty.getText());

        ItemDTO itemDTO = new ItemDTO(itemId, categoryId, name, price, quantity, warranty);

        boolean isSaved = itemBO.save(itemDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Item saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save item...!").show();
        }
    }

    @FXML
    void btnUpdateItemOnAction(ActionEvent event) throws SQLException {
        String itemId = lblItemId.getText();
        String categoryId = categoryBO.findByName(cmbCategoryId.getValue()).getCategoryId();
        String name = txtName.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int quantity = Integer.parseInt(txtQuantity.getText());
        int warranty = Integer.parseInt(txtWarranty.getText());

        ItemDTO itemDTO = new ItemDTO(itemId, categoryId, name, price, quantity, warranty);

        boolean isSaved = itemBO.update(itemDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Item updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update item...!").show();
        }
    }

    @FXML
    void cmbCategoryOnAction(ActionEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) throws SQLException {
        ItemTM itemTM = tblItem.getSelectionModel().getSelectedItem();

        if (itemTM != null) {
            lblItemId.setText(itemTM.getItemId());
            cmbCategoryId.setValue(categoryBO.findById(itemTM.getCategoryId()).getDescription());
            txtName.setText(itemTM.getItemName());
            txtPrice.setText(String.valueOf(itemTM.getPrice()));
            txtQuantity.setText(String.valueOf(itemTM.getQuantity()));
            txtWarranty.setText(String.valueOf(itemTM.getWarranty()));

            btnSaveItem.setDisable(true);
            btnDeleteItem.setDisable(false);
            btnUpdateItem.setDisable(false);
        }
    }

}
