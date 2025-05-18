module tourist_vouchers.v17_tourist_vouchers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens tourist_vouchers.v17_tourist_vouchers to javafx.fxml;
    opens tourist_vouchers.v17_tourist_vouchers.controller to javafx.fxml;
    opens tourist_vouchers.v17_tourist_vouchers.model to javafx.base;
    exports tourist_vouchers.v17_tourist_vouchers;
    exports tourist_vouchers.v17_tourist_vouchers.controller;
}