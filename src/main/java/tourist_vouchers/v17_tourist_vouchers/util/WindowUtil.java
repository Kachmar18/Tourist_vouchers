package tourist_vouchers.v17_tourist_vouchers.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class WindowUtil {
    public static void openNewWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowUtil.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Помилка", "Не вдалося відкрити вікно: " + title);
        }
    }

    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowUtil.class.getResource(fxmlPath));
            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Помилка", "Не вдалося завантажити вікно.");
        }
    }
}
