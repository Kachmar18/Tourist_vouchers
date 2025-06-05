package tourist_vouchers.v17_tourist_vouchers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientChoiceTest {
    private ClientChoice client;

    @BeforeEach
    void setUp() {
        client = new ClientChoice(1, "Ivan", "987654321", "1234", LocalDate.of(2025, 6, 3), 5);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(1, client.getId_client());
        assertEquals("Ivan", client.getName());
        assertEquals(987654321, client.getPhone());
        assertEquals(1234, client.getPassword());
        assertEquals(LocalDate.of(2025, 6, 3), client.getBookingDate());
        assertEquals(5, client.getId_selectedTour());
    }

    @Test
    void testSetters() {
        client.setId_client(2);
        client.setName("Oksana");
        client.setPhone(123456789);
        client.setPassword(5678);
        client.setBookingDate(LocalDate.of(2024, 1, 1));
        client.setId_selectedTour(10);

        assertEquals(2, client.getId_client());
        assertEquals("Oksana", client.getName());
        assertEquals(123456789, client.getPhone());
        assertEquals(5678, client.getPassword());
        assertEquals(LocalDate.of(2024, 1, 1), client.getBookingDate());
        assertEquals(10, client.getId_selectedTour());
    }

    @Test
    void testInvalidPhoneInputThrowsException() {
        assertThrows(NumberFormatException.class, () -> {
            new ClientChoice(3, "Test", "notDigits", "1234", LocalDate.now(), 0);
        });
    }

    @Test
    void testInvalidPasswordInputThrowsException() {
        assertThrows(NumberFormatException.class, () -> {
            new ClientChoice(3, "Test", "123456789", "invalid", LocalDate.now(), 0);
        });
    }
}
