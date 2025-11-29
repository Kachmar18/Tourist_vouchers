package tourist_vouchers.v17_tourist_vouchers.dao;

import tourist_vouchers.v17_tourist_vouchers.model.Client;
import tourist_vouchers.v17_tourist_vouchers.model.Admin;
import tourist_vouchers.v17_tourist_vouchers.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    public boolean clientExists(String fullName) {
        String sql = "SELECT COUNT(*) FROM clients WHERE full_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            logger.severe("Помилка перевірки існування клієнта: " + e.getMessage());
            return false;
        }
    }

    public Optional<Object> authenticate(String fullName, String password) {
        Optional<Client> client = authenticateClient(fullName, password);
        if (client.isPresent()) {
            return Optional.of(client.get());
        }

        Optional<Admin> admin = authenticateAdmin(fullName, password);
        if (admin.isPresent()) {
            return Optional.of(admin.get());
        }

        return Optional.empty();
    }

    private Optional<Client> authenticateClient(String fullName, String password) {
        String sql = "SELECT c.*, t.user_type FROM clients c " +
                "JOIN type_users t ON c.id_usertype = t.id_usertype " +
                "WHERE c.full_name = ? AND c.password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Client client = new Client();
                client.setIdClient(rs.getInt("id_client"));
                client.setIdUserType(rs.getInt("id_usertype"));
                client.setFullName(rs.getString("full_name"));
                client.setPhone(rs.getString("phone"));
                client.setPassword(rs.getString("password"));
                client.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                client.setActive(rs.getBoolean("is_active"));
                return Optional.of(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<Admin> authenticateAdmin(String fullName, String password) {
        String sql = "SELECT a.*, t.user_type FROM admins a " +
                "JOIN type_users t ON a.id_usertype = t.id_usertype " +
                "WHERE a.full_name = ? AND a.password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setIdAdmin(rs.getInt("id_admin"));
                admin.setIdUserType(rs.getInt("id_usertype"));
                admin.setFullName(rs.getString("full_name"));
                admin.setPhone(rs.getString("phone"));
                admin.setPassword(rs.getString("password"));
                admin.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                admin.setActive(rs.getBoolean("is_active"));
                return Optional.of(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean registerClient(String fullName, String phone, String password) {
        String sql = "INSERT INTO clients (id_usertype, full_name, phone, password) VALUES " +
                "((SELECT id_usertype FROM type_users WHERE user_type = 'CLIENT'), ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setString(2, phone);
            stmt.setString(3, password);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean activateClient(int clientId) {
        String sql = "UPDATE clients SET is_active = TRUE WHERE id_client = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Помилка активації клієнта ID: " + clientId + " - " + e.getMessage());
            return false;
        }
    }

    public boolean activateAdmin(int adminId) {
        String sql = "UPDATE admins SET is_active = TRUE WHERE id_admin = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, adminId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Помилка активації адміна: " + e.getMessage());
            return false;
        }
    }

    public boolean deactivateClient(int clientId) {
        String sql = "UPDATE clients SET is_active = FALSE WHERE id_client = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Помилка деактивації клієнта ID: " + clientId + " - " + e.getMessage());
            return false;
        }
    }

    public boolean deactivateAdmin(int adminId) {
        String sql = "UPDATE admins SET is_active = FALSE WHERE id_admin = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, adminId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Помилка деактивації адміна ID: " + adminId + " - " + e.getMessage());
            return false;
        }
    }

    public boolean validateAdminPassword(String specialPassword) {
        String sql = "SELECT COUNT(*) FROM type_users WHERE user_type = 'ADMIN' AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, specialPassword);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            logger.severe("Помилка перевірки спеціального пароля адміна: " + e.getMessage());
            return false;
        }
    }

    public boolean adminPhoneExists(String phone) {
        String sql = "SELECT COUNT(*) FROM admins WHERE phone = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            logger.severe("Помилка перевірки телефону адміна: " + e.getMessage());
            return false;
        }
    }

    public boolean registerAdmin(String fullName, String phone, String password) {
        String sql = "INSERT INTO admins (id_usertype, full_name, phone, password) VALUES " +
                "((SELECT id_usertype FROM type_users WHERE user_type = 'ADMIN'), ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setString(2, phone);
            stmt.setString(3, password);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Помилка реєстрації адміністратора: " + e.getMessage());
            return false;
        }
    }

    public boolean phoneExists(String phone) {
        String sql = "SELECT COUNT(*) FROM (" +
                "SELECT phone FROM clients WHERE phone = ? " +
                "UNION ALL " +
                "SELECT phone FROM admins WHERE phone = ?" +
                ") AS all_users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);
            stmt.setString(2, phone);

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            logger.severe("Помилка перевірки унікальності телефону: " + e.getMessage());
            return false;
        }
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE is_active = TRUE ORDER BY full_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Client client = new Client();
                client.setIdClient(rs.getInt("id_client"));
                client.setIdUserType(rs.getInt("id_usertype"));
                client.setFullName(rs.getString("full_name"));
                client.setPhone(rs.getString("phone"));
                client.setPassword(rs.getString("password"));
                client.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                client.setActive(rs.getBoolean("is_active"));
                clients.add(client);
            }
        } catch (SQLException e) {
            logger.severe("Помилка отримання списку клієнтів: " + e.getMessage());
        }
        return clients;
    }
}