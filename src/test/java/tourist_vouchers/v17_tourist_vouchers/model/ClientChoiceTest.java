package tourist_vouchers.v17_tourist_vouchers.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ClientChoiceTest {

    @Test
    void testDefaultConstructor() {
        ClientChoice choice = new ClientChoice();
        assertNotNull(choice);
    }

    @Test
    void testFullConstructor() {
        LocalDate date = LocalDate.of(2025, 11, 23);

        ClientChoice choice = new ClientChoice(
                1,
                "Alice",
                "123456",
                "7890",
                date,
                10
        );

        assertEquals(1, choice.getId_client());
        assertEquals("Alice", choice.getName());
        assertEquals(123456, choice.getPhone());
        assertEquals(7890, choice.getPassword());
        assertEquals(date, choice.getBookingDate());
        assertEquals(10, choice.getId_selectedTour());
    }

    @Test
    void testSettersAndGetters() {
        ClientChoice choice = new ClientChoice();
        LocalDate date = LocalDate.of(2025, 12, 1);

        choice.setId_client(5);
        choice.setName("Bob");
        choice.setPhone(654321);
        choice.setPassword(4321);
        choice.setBookingDate(date);
        choice.setId_selectedTour(20);

        assertEquals(5, choice.getId_client());
        assertEquals("Bob", choice.getName());
        assertEquals(654321, choice.getPhone());
        assertEquals(4321, choice.getPassword());
        assertEquals(date, choice.getBookingDate());
        assertEquals(20, choice.getId_selectedTour());
    }
}
