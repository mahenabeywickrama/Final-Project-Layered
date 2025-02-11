package lk.ijse.gdse.pcstore.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse.pcstore.bo.BOFactory;
import lk.ijse.gdse.pcstore.bo.custom.LoginHistoryBO;
import lk.ijse.gdse.pcstore.bo.custom.UserBO;
import lk.ijse.gdse.pcstore.dto.LoginDTO;
import lk.ijse.gdse.pcstore.dto.UserDTO;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    @FXML
    private Button btnCustomer;

    @FXML
    private Button btnDashBoard;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnItems;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnLoginHistory;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnPayment;

    @FXML
    private Button btnRepair;

    @FXML
    private Button btnReplacement;

    @FXML
    private Button btnSupplier;

    @FXML
    private Button btnSupplies;

    @FXML
    private Button btnUser;

    @FXML
    private AnchorPane content;

    @FXML
    private AnchorPane mainPane;

    LoginHistoryBO loginHistoryBO = (LoginHistoryBO) BOFactory.getInstance().getBO(BOFactory.BOType.LOGIN_HISTORY);
    UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    private boolean isAdmin = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            String lastLogin = loginHistoryBO.getLastLogin();
            UserDTO lastUser = userBO.findById(lastLogin);

            if (lastUser != null) {
                if (lastUser.getUserRole().equals("ADMIN")) {
                    isAdmin = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (isAdmin) {
            btnLoginHistory.setVisible(true);
            btnUser.setVisible(true);
            btnEmployee.setVisible(true);
            btnSupplier.setVisible(true);
            btnSupplies.setVisible(true);
            btnItems.setVisible(true);
        }else {
            btnLoginHistory.setVisible(false);
            btnUser.setVisible(false);
            btnEmployee.setVisible(false);
            btnSupplier.setVisible(false);
            btnSupplies.setVisible(false);
            btnItems.setVisible(false);
        }

        changeBtnColor(btnDashBoard);
        navigateTo("/view/DashBoardView.fxml");
    }
    @FXML
    void navigationToCustomerPage(ActionEvent event) {
        changeBtnColor(btnCustomer);
        navigateTo("/view/CustomerView.fxml");
    }

    @FXML
    void navigationToDashBoardPage(ActionEvent event) {
        changeBtnColor(btnDashBoard);
        navigateTo("/view/DashBoardView.fxml");
    }

    @FXML
    void navigationToEmployeePage(ActionEvent event) {
        changeBtnColor(btnEmployee);
        navigateTo("/view/EmployeeView.fxml");
    }

    @FXML
    void navigationToItemPage(ActionEvent event) {
        changeBtnColor(btnItems);
        navigateTo("/view/ItemView.fxml");
    }

    @FXML
    void navigationToLogInPage(ActionEvent event) throws IOException {
        changeBtnColor(btnLogOut);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            mainPane.getChildren().clear();
            mainPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/LoginView.fxml")));
        }
    }

    @FXML
    void navigationToLoginHistoryPage(ActionEvent event) {
        changeBtnColor(btnLoginHistory);
        navigateTo("/view/LoginHistoryView.fxml");
    }

    @FXML
    void navigationToOrdersPage(ActionEvent event) {
        changeBtnColor(btnOrders);
        navigateTo("/view/OrdersView.fxml");
    }

    @FXML
    void navigationToPaymentPage(ActionEvent event) {
        changeBtnColor(btnPayment);
        navigateTo("/view/PaymentView.fxml");
    }

    @FXML
    void navigationToRepairPage(ActionEvent event) {
        changeBtnColor(btnRepair);
        navigateTo("/view/RepairView.fxml");
    }

    @FXML
    void navigationToReplacementPage(ActionEvent event) {
        changeBtnColor(btnReplacement);
        navigateTo("/view/ReplacementView.fxml");
    }

    @FXML
    void navigationToSupplierPage(ActionEvent event) {
        changeBtnColor(btnSupplier);
        navigateTo("/view/SupplierView.fxml");
    }

    @FXML
    void navigationToSuppliesPage(ActionEvent event) {
        changeBtnColor(btnSupplies);
        navigateTo("/view/SuppliesView.fxml");
    }

    @FXML
    void navigationToUserPage(ActionEvent event) {
        changeBtnColor(btnUser);
        navigateTo("/view/UserView.fxml");
    }

    private void navigateTo(String fxmlPath){
        try {
            content.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));
            content.getChildren().add(load);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load ui!").show();
            e.printStackTrace();
        }
    }

    private void changeBtnColor(Button btn){
        Button[] buttons = {btnCustomer, btnDashBoard, btnEmployee, btnItems, btnLogOut, btnLoginHistory, btnOrders, btnPayment, btnRepair, btnReplacement, btnSupplier, btnSupplies, btnUser};
        for (Button button : buttons) {
            button.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");

        }

        btn.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");
    }
}
