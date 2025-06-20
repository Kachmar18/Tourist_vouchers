package tourist_vouchers.v17_tourist_vouchers.services;

import tourist_vouchers.v17_tourist_vouchers.dao.ClientChoiceDAO;
import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;

import java.util.List;

public class ClientChoiceService {
    private final ClientChoiceDAO dao;

    public ClientChoiceService() {
        this.dao = new ClientChoiceDAO();
    }

    public ClientChoiceService(ClientChoiceDAO dao) {
        this.dao = dao;
    }

    public boolean registerClient(String name, String phone, String password) {
        try {
            int phoneNum = Integer.parseInt(phone);
            int passwordNum = Integer.parseInt(password);
            return dao.register(name, phoneNum, passwordNum);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public ClientChoice loginClient(String name, String password) {
        try {
            int passwordNum = Integer.parseInt(password);
            return dao.login(name, passwordNum);
        } catch (NumberFormatException e) {
            return null;
        }
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
}

