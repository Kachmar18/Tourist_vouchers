package tourist_vouchers.v17_tourist_vouchers.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void testDefaultConstructor() {
        Client client = new Client();
        assertNotNull(client);
    }

    @Test
    void testFullConstructor() {
        LocalDateTime now = LocalDateTime.now();

        Client client = new Client(
                1,
                2,
                "Alice Smith",
                "123456789",
                "secret",
                now,
                true
        );

        assertEquals(1, client.getIdClient());
        assertEquals(2, client.getIdUserType());
        assertEquals("Alice Smith", client.getFullName());
        assertEquals("123456789", client.getPhone());
        assertEquals("secret", client.getPassword());
        assertEquals(now, client.getCreatedAt());
        assertTrue(client.isActive());
    }

    @Test
    void testSettersAndGetters() {
        Client client = new Client();

        LocalDateTime now = LocalDateTime.now();

        client.setIdClient(10);
        client.setIdUserType(20);
        client.setFullName("Bob Johnson");
        client.setPhone("987654321");
        client.setPassword("mypassword");
        client.setCreatedAt(now);
        client.setActive(false);

        assertEquals(10, client.getIdClient());
        assertEquals(20, client.getIdUserType());
        assertEquals("Bob Johnson", client.getFullName());
        assertEquals("987654321", client.getPhone());
        assertEquals("mypassword", client.getPassword());
        assertEquals(now, client.getCreatedAt());
        assertFalse(client.isActive());
    }
}
