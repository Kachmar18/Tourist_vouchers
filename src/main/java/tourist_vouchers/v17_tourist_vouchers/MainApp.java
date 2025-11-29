package tourist_vouchers.v17_tourist_vouchers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tourist_vouchers.v17_tourist_vouchers.util.WindowUtil;

import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApp.class.getResource("/tourist_vouchers/v17_tourist_vouchers/login_view.fxml")
        );
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle(WindowUtil.NAME);
        stage.setResizable(false);
        stage.getIcons().add(
                new Image(Objects.requireNonNull(MainApp.class.getResourceAsStream(WindowUtil.ICON)))
        );
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}