module tourist_vouchers.v17_tourist_vouchers {
    requires javafx.controls;
    requires javafx.fxml;


    opens tourist_vouchers.v17_tourist_vouchers to javafx.fxml;
    exports tourist_vouchers.v17_tourist_vouchers;
}