package tourist_vouchers.v17_tourist_vouchers.dao;

import tourist_vouchers.v17_tourist_vouchers.model.ClientChoice;
import tourist_vouchers.v17_tourist_vouchers.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientChoiceDAO {
    public boolean register(String name, int phone, int password) {
        String checkSql = "SELECT COUNT(*) FROM client_choices WHERE name = ?";
        String insertSql = "INSERT INTO client_choices (name, phone, password, booking_date, tour_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) return false; // вже є

            insertStmt.setString(1, name);
            insertStmt.setInt(2, phone);
            insertStmt.setInt(3, password);
            insertStmt.setDate(4, Date.valueOf(LocalDate.now()));
            insertStmt.setNull(5, java.sql.Types.INTEGER); // без обраного туру

            insertStmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ClientChoice login(String name, int password) {
        String sql = "SELECT * FROM client_choices WHERE name = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ClientChoice(
                        rs.getInt("id_client"),
                        rs.getString("name"),
                        String.valueOf(rs.getInt("phone")),
                        String.valueOf(rs.getInt("password")),
                        rs.getDate("booking_date").toLocalDate(),
                        rs.getInt("tour_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateClientTour(int clientId, int tourId) {
        String sql = "UPDATE client_choices SET tour_id = ? WHERE id_client = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tourId);
            stmt.setInt(2, clientId);
            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearClientTour(int clientId) {
        String sql = "UPDATE client_choices SET tour_id = NULL WHERE id_client = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            int updated = stmt.executeUpdate();
            return updated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ClientChoice> getClientsWithTour() {
        List<ClientChoice> clients = new ArrayList<>();
        String sql = "SELECT * FROM client_choices WHERE tour_id IS NOT NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ClientChoice client = new ClientChoice(
                        rs.getInt("id_client"),
                        rs.getString("name"),
                        String.valueOf(rs.getInt("phone")),
                        String.valueOf(rs.getInt("password")),
                        rs.getDate("booking_date").toLocalDate(),
                        rs.getInt("tour_id")
                );
                clients.add(client);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }



}