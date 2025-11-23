package tourist_vouchers.v17_tourist_vouchers.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tourist_vouchers.v17_tourist_vouchers.dao.ClientChoiceDAO;
import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;
import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientChoiceServiceTest {

    private ClientChoiceDAO daoMock;
    private ClientChoiceService service;

    @BeforeEach
    void setUp() {
        daoMock = mock(ClientChoiceDAO.class);
        // Використовуємо конструктор з DAO
        service = new ClientChoiceService(daoMock);
    }

    @Test
    void testBookTour() {
        when(daoMock.updateClientTour(1, 100)).thenReturn(true);
        assertTrue(service.bookTour(1, 100));
        verify(daoMock).updateClientTour(1, 100);
    }

    @Test
    void testClearBookedTour() {
        when(daoMock.clearClientTour(1)).thenReturn(true);
        assertTrue(service.clearBookedTour(1));
        verify(daoMock).clearClientTour(1);
    }

    @Test
    void testGetClientsWithTours() {
        List<ClientChoice> mockList = List.of(new ClientChoice());
        when(daoMock.getClientsWithTour()).thenReturn(mockList);

        List<ClientChoice> result = service.getClientsWithTours();
        assertEquals(mockList, result);
        verify(daoMock).getClientsWithTour();
    }

    @Test
    void testGetBookedTour() {
        TourPackage mockTour = new TourPackage();
        when(daoMock.getBookedTour(1)).thenReturn(mockTour);

        TourPackage result = service.getBookedTour(1);
        assertEquals(mockTour, result);
        verify(daoMock).getBookedTour(1);
    }
}
