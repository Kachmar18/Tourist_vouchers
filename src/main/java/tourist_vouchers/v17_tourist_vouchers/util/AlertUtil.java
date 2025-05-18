package tourist_vouchers.v17_tourist_vouchers.util;

import javafx.scene.control.Alert;

public class AlertUtil {
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
