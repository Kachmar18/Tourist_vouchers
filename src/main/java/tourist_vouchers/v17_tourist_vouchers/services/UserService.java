package tourist_vouchers.v17_tourist_vouchers.services;

import tourist_vouchers.v17_tourist_vouchers.dao.UserDAO;
import tourist_vouchers.v17_tourist_vouchers.model.Client;
import tourist_vouchers.v17_tourist_vouchers.model.Admin;

import java.util.List;
import java.util.logging.Logger;

public class UserService {
    private final UserDAO userDAO;
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // конструктор для тестів
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void activateUser(Object user) {
        if (user instanceof Client) {
            userDAO.activateClient(((Client) user).getIdClient());
        } else if (user instanceof Admin) {
            userDAO.activateAdmin(((Admin) user).getIdAdmin());
        }
    }

    public boolean isPhoneUnique(String phone) {
        return !userDAO.phoneExists(phone);
    }

    public boolean isClientNameUnique(String fullName) {
        return !userDAO.clientExists(fullName);
    }

    public Object loginUser(String fullName, String password) {
        Object user = userDAO.authenticate(fullName, password).orElse(null);
        if (user != null) {
            activateUser(user);
        }
        return user;
    }



    public boolean registerClient(String fullName, String phone, String password) {
        if (!isPhoneUnique(phone)) {
            return false;
        }
        if (!isClientNameUnique(fullName)) {
            return false;
        }

        return userDAO.registerClient(fullName, phone, password);
    }

    public void logoutUser(Object user) {
        if (user instanceof Client) {
            boolean success = userDAO.deactivateClient(((Client) user).getIdClient());
            if (success) {
                logger.info("Клієнт деактивований: " + ((Client) user).getFullName());
            }
        } else if (user instanceof Admin) {
            boolean success = userDAO.deactivateAdmin(((Admin) user).getIdAdmin());
            if (success) {
                logger.info("Адмін деактивований: " + ((Admin) user).getFullName());
            }
        }
    }

    public boolean isAdmin(Object user) {
        return user instanceof Admin;
    }

    public boolean isClient(Object user) {
        return user instanceof Client;
    }

    public boolean isAdminPhoneUnique(String phone) {
        return !userDAO.adminPhoneExists(phone);
    }

    public boolean isValidAdminPassword(String specialPassword) {
        return userDAO.validateAdminPassword(specialPassword);
    }

    public boolean registerAdmin(String fullName, String phone, String password) {
        if (!isAdminPhoneUnique(phone)) {
            return false;
        }

        return userDAO.registerAdmin(fullName, phone, password);
    }

    public List<Client> getAllClients() {
        return userDAO.getAllClients();
    }
}