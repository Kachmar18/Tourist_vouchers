package tourist_vouchers.v17_tourist_vouchers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApp.class.getResource("/tourist_vouchers/v17_tourist_vouchers/login_view.fxml")
        );
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Туристична агенція");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}