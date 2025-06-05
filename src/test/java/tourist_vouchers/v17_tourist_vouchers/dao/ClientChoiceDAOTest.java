package tourist_vouchers.v17_tourist_vouchers.dao;

import org.junit.jupiter.api.*;
import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientChoiceDAOTest {

    private final ClientChoiceDAO dao = new ClientChoiceDAO();
    private static final String TEST_NAME = "TestUser123";
    private static final String DUPLICATE_NAME = "TestUser123";
    private static final int TEST_PHONE = 987654321;
    private static final int TEST_PASSWORD = 1234;
    private static int createdClientId;

    @Test
    @Order(1)
    void testRegisterClient() {
        boolean registered = dao.register(TEST_NAME, TEST_PHONE, TEST_PASSWORD);
        assertTrue(registered);
    }

    @Test
    @Order(2)
    void testRegisterDuplicateClient() {
        boolean registered = dao.register(DUPLICATE_NAME, TEST_PHONE, TEST_PASSWORD);
        assertFalse(registered);
    }

    @Test
    @Order(3)
    void testLoginClient() {
        ClientChoice client = dao.login(TEST_NAME, TEST_PASSWORD);
        assertNotNull(client);
        assertEquals(TEST_NAME, client.getName());
        createdClientId = client.getId_client();
    }

    @Test
    @Order(4)
    void testUpdateClientTour() {
        boolean updated = dao.updateClientTour(createdClientId, 1);
        assertTrue(updated);
    }

    @Test
    @Order(5)
    void testClearClientTour() {
        boolean cleared = dao.clearClientTour(createdClientId);
        assertTrue(cleared);
    }
}
