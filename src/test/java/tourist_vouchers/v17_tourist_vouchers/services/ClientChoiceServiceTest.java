package tourist_vouchers.v17_tourist_vouchers.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tourist_vouchers.v17_tourist_vouchers.dao.ClientChoiceDAO;
import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientChoiceServiceTest {

    private ClientChoiceDAO mockDao;
    private ClientChoiceService service;
    private ClientChoice sampleClient;

    @BeforeEach
    void setUp() {
        mockDao = mock(ClientChoiceDAO.class);
        service = new ClientChoiceService(mockDao); // Необхідний конструктор із DAO

        sampleClient = new ClientChoice(1, "Test User", "123456789", "1234", null, 0);
    }

    @Test
    void testRegisterClient_validData() {
        when(mockDao.register("Test User", 123456789, 1234)).thenReturn(true);

        boolean result = service.registerClient("Test User", "123456789", "1234");

        assertTrue(result);
        verify(mockDao).register("Test User", 123456789, 1234);
    }

    @Test
    void testRegisterClient_invalidPhone() {
        boolean result = service.registerClient("Test User", "abc", "1234");

        assertFalse(result);
        verifyNoInteractions(mockDao);
    }

    @Test
    void testRegisterClient_invalidPassword() {
        boolean result = service.registerClient("Test User", "123456789", "abc");

        assertFalse(result);
        verifyNoInteractions(mockDao);
    }

    @Test
    void testLoginClient_valid() {
        when(mockDao.login("Test User", 1234)).thenReturn(sampleClient);

        ClientChoice result = service.loginClient("Test User", "1234");

        assertNotNull(result);
        assertEquals("Test User", result.getName());
    }

    @Test
    void testLoginClient_invalidPasswordFormat() {
        ClientChoice result = service.loginClient("Test User", "pass");

        assertNull(result);
        verifyNoInteractions(mockDao);
    }

    @Test
    void testBookTour_success() {
        when(mockDao.updateClientTour(1, 10)).thenReturn(true);

        boolean result = service.bookTour(1, 10);

        assertTrue(result);
        verify(mockDao).updateClientTour(1, 10);
    }

    @Test
    void testClearBookedTour_success() {
        when(mockDao.clearClientTour(1)).thenReturn(true);

        boolean result = service.clearBookedTour(1);

        assertTrue(result);
        verify(mockDao).clearClientTour(1);
    }


    @Test
    void testGetClientsWithTours() {
        // Arrange
        ClientChoice client1 = new ClientChoice(1, "Іван", "123456789", "1111", LocalDate.now(), 2);
        ClientChoice client2 = new ClientChoice(2, "Марія", "987654321", "2222", LocalDate.now(), 3);

        List<ClientChoice> expectedList = Arrays.asList(client1, client2);

        when(mockDao.getClientsWithTour()).thenReturn(expectedList);

        // Act
        List<ClientChoice> result = service.getClientsWithTours();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Іван", result.get(0).getName());
        assertEquals("Марія", result.get(1).getName());

        verify(mockDao, times(1)).getClientsWithTour();
    }
}

