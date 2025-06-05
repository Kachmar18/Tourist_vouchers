package tourist_vouchers.v17_tourist_vouchers.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmailUtilTest {

    @Test
    void testSendCriticalErrorDoesNotThrow() {
        Exception testException = new RuntimeException("Тест критичної помилки");

        // Лише перевірка, що метод не кидає виняток
        assertDoesNotThrow(() -> {
            EmailUtil.sendCriticalError("Тест тема", testException);
        });
    }
}
