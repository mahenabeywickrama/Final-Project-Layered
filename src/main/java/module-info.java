module lk.ijse.gdse.pcstore {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;

    opens lk.ijse.gdse.pcstore.dto.tm to javafx.base;
    opens lk.ijse.gdse.pcstore.controller to javafx.fxml;
    exports lk.ijse.gdse.pcstore;
}