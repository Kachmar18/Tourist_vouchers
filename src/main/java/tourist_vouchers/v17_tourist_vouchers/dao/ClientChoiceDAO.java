package tourist_vouchers.v17_tourist_vouchers.dao;

import tourist_vouchers.v17_tourist_vouchers.model.*;
import tourist_vouchers.v17_tourist_vouchers.util.DBConnection;
import tourist_vouchers.v17_tourist_vouchers.util.LogUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ClientChoiceDAO {
    private static final Logger logger = LogUtil.getLogger();

    public boolean updateClientTour(int clientId, int tourId) {
        String checkSql = "SELECT COUNT(*) FROM client_choices WHERE client_id = ?";
        String insertSql = "INSERT INTO client_choices (client_id, tour_id, booking_date) VALUES (?, ?, CURRENT_TIMESTAMP)";
        String updateSql = "UPDATE client_choices SET tour_id = ?, booking_date = CURRENT_TIMESTAMP WHERE client_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            boolean recordExists = false;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, clientId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    recordExists = rs.getInt(1) > 0;
                }
            }

            if (recordExists) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, tourId);
                    updateStmt.setInt(2, clientId);
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, clientId);
                    insertStmt.setInt(2, tourId);
                    return insertStmt.executeUpdate() > 0;
                }
            }

        } catch (SQLException e) {
            logger.severe("Помилка бронювання туру: " + e.getMessage());
            return false;
        }
    }


    public boolean clearClientTour(int clientId) {
        String sql = "DELETE FROM client_choices WHERE client_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Помилка скасування туру: " + e.getMessage());
            return false;
        }
    }

    public List<ClientChoice> getClientsWithTour() {
        List<ClientChoice> clients = new ArrayList<>();
        String sql = "SELECT c.id_client, c.full_name, c.phone, t.tour_id, t.title " +
                "FROM clients c " +
                "JOIN client_choices cc ON c.id_client = cc.client_id " +
                "JOIN tour t ON cc.tour_id = t.tour_id " +
                "WHERE c.is_active = TRUE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ClientChoice client = new ClientChoice();
                client.setId_client(rs.getInt("id_client"));
                client.setName(rs.getString("full_name"));
                client.setPhone(Integer.parseInt(rs.getString("phone")));
                client.setId_selectedTour(rs.getInt("tour_id"));
                clients.add(client);
            }
        } catch (SQLException e) {
            logger.severe("Помилка отримання клієнтів з турами: " + e.getMessage());
        }
        return clients;
    }


    public TourPackage getBookedTour(int clientId) {
        String sql = "SELECT t.* FROM tour t " +
                "JOIN client_choices cc ON t.tour_id = cc.tour_id " +
                "WHERE cc.client_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TourPackage tour = new TourPackage();
                tour.setId(rs.getInt("tour_id"));
                tour.setTitle(rs.getString("title"));
                tour.setDestination(rs.getString("destination"));
                tour.setPrice(rs.getDouble("price"));
                tour.setDays(rs.getInt("days"));

                try {
                    tour.setTransport(TransportType.valueOf(rs.getString("transport")));
                    tour.setFoodType(FoodType.valueOf(rs.getString("food_type")));
                    tour.setTourType(TourType.valueOf(rs.getString("tour_type")));
                } catch (IllegalArgumentException e) {
                    logger.warning("Помилка парсингу enum'ів для туру ID: " + tour.getId());
                }

                return tour;
            }
        } catch (SQLException e) {
            logger.severe("Помилка отримання заброньованого туру: " + e.getMessage());
        }
        return null;
    }
}