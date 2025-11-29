package tourist_vouchers.v17_tourist_vouchers.services;

import tourist_vouchers.v17_tourist_vouchers.dao.ClientChoiceDAO;
import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;
import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;

import java.util.List;

public class ClientChoiceService {
    private final ClientChoiceDAO dao;

    public ClientChoiceService() {
        this.dao = new ClientChoiceDAO();
    }

    // додатковий конструктор для тестів
    public ClientChoiceService(ClientChoiceDAO dao) {
        this.dao = dao;
    }

    public boolean bookTour(int clientId, int tourId) {
        return dao.updateClientTour(clientId, tourId);
    }

    public boolean clearBookedTour(int clientId) {
        return dao.clearClientTour(clientId);
    }

    public List<ClientChoice> getClientsWithTours() {
        return dao.getClientsWithTour();
    }

    public TourPackage getBookedTour(int clientId) {
        return dao.getBookedTour(clientId);
    }


}

