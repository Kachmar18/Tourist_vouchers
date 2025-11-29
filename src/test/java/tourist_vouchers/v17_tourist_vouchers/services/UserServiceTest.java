package tourist_vouchers.v17_tourist_vouchers.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tourist_vouchers.v17_tourist_vouchers.dao.UserDAO;
import tourist_vouchers.v17_tourist_vouchers.model.Client;
import tourist_vouchers.v17_tourist_vouchers.model.Admin;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserDAO daoMock;
    private UserService service;

    @BeforeEach
    void setUp() {
        daoMock = mock(UserDAO.class);
        service = new UserService(daoMock);
    }

    @Test
    void testActivateUser() {
        Client client = new Client();
        client.setIdClient(1);

        Admin admin = new Admin();
        admin.setIdAdmin(2);

        service.activateUser(client);
        verify(daoMock).activateClient(1);

        service.activateUser(admin);
        verify(daoMock).activateAdmin(2);
    }

    @Test
    void testIsPhoneUnique() {
        when(daoMock.phoneExists("123")).thenReturn(true);
        assertFalse(service.isPhoneUnique("123"));
        when(daoMock.phoneExists("456")).thenReturn(false);
        assertTrue(service.isPhoneUnique("456"));
    }

    @Test
    void testIsClientNameUnique() {
        when(daoMock.clientExists("Alice")).thenReturn(true);
        assertFalse(service.isClientNameUnique("Alice"));
        when(daoMock.clientExists("Bob")).thenReturn(false);
        assertTrue(service.isClientNameUnique("Bob"));
    }

    @Test
    void testLoginUser() {
        Client client = new Client();
        when(daoMock.authenticate("Alice", "pass")).thenReturn(Optional.of(client));

        Object user = service.loginUser("Alice", "pass");
        assertEquals(client, user);
        verify(daoMock).activateClient(client.getIdClient());
    }

    @Test
    void testRegisterClient() {
        when(daoMock.phoneExists("123")).thenReturn(false);
        when(daoMock.clientExists("Alice")).thenReturn(false);
        when(daoMock.registerClient("Alice", "123", "pass")).thenReturn(true);

        assertTrue(service.registerClient("Alice", "123", "pass"));
    }

    @Test
    void testIsAdminAndIsClient() {
        Client client = new Client();
        Admin admin = new Admin();

        assertTrue(service.isClient(client));
        assertFalse(service.isClient(admin));

        assertTrue(service.isAdmin(admin));
        assertFalse(service.isAdmin(client));
    }

    @Test
    void testIsAdminPhoneUnique() {
        when(daoMock.adminPhoneExists("123")).thenReturn(true);
        assertFalse(service.isAdminPhoneUnique("123"));
        when(daoMock.adminPhoneExists("456")).thenReturn(false);
        assertTrue(service.isAdminPhoneUnique("456"));
    }

    @Test
    void testIsValidAdminPassword() {
        when(daoMock.validateAdminPassword("secret")).thenReturn(true);
        assertTrue(service.isValidAdminPassword("secret"));
        when(daoMock.validateAdminPassword("wrong")).thenReturn(false);
        assertFalse(service.isValidAdminPassword("wrong"));
    }

    @Test
    void testRegisterAdmin() {
        when(daoMock.adminPhoneExists("123")).thenReturn(false);
        when(daoMock.registerAdmin("Admin", "123", "pass")).thenReturn(true);

        assertTrue(service.registerAdmin("Admin", "123", "pass"));
    }

    @Test
    void testGetAllClients() {
        List<Client> clients = List.of(new Client());
        when(daoMock.getAllClients()).thenReturn(clients);

        List<Client> result = service.getAllClients();
        assertEquals(clients, result);
        verify(daoMock).getAllClients();
    }
}
