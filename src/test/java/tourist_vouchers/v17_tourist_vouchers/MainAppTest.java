package tourist_vouchers.v17_tourist_vouchers;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tourist_vouchers.v17_tourist_vouchers.util.AlertUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AlertUtilTest {

    @BeforeAll
    static void initToolkit() {
        // Запускає JavaFX runtime
        new JFXPanel();
    }

    @Test
    void testShowInfoDoesNotThrow() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() ->
                    AlertUtil.showInfo("Це інформаційне повідомлення")
            );
            latch.countDown();
        });
        latch.await(3, TimeUnit.SECONDS);
    }

    @Test
    void testShowErrorDoesNotThrow() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() ->
                    AlertUtil.showError("Це повідомлення про помилку")
            );
            latch.countDown();
        });
        latch.await(3, TimeUnit.SECONDS);
    }
}
