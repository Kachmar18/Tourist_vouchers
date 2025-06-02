package tourist_vouchers.v17_tourist_vouchers.dao;

import tourist_vouchers.v17_tourist_vouchers.model.FoodType;
import tourist_vouchers.v17_tourist_vouchers.model.TourPackage;
import tourist_vouchers.v17_tourist_vouchers.model.TourType;
import tourist_vouchers.v17_tourist_vouchers.model.TransportType;
import tourist_vouchers.v17_tourist_vouchers.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourPackageDAO {
    public List<TourPackage> getAllTours() throws SQLException {
        List<TourPackage> tours = new ArrayList<>();
        String sql = "SELECT * FROM tour";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TourPackage tour = new TourPackage(
                        rs.getInt("tour_id"),
                        rs.getString("title"),
                        rs.getString("destination"),
                        rs.getDouble("price"),
                        rs.getInt("days"),
                        TransportType.valueOf(rs.getString("transport")),
                        FoodType.valueOf(rs.getString("food_type")),
                        TourType.valueOf(rs.getString("tour_type"))
                );
                tours.add(tour);
            }
        }
        return tours;
    }

    public void addTour(TourPackage tour) throws SQLException {
        String sql = "INSERT INTO `tour`(`title`, `destination`, `price`, `days`, `transport`, `food_type`, `tour_type`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tour.getTitle());
            pstmt.setString(2, tour.getDestination());
            pstmt.setDouble(3, tour.getPrice());
            pstmt.setInt(4, tour.getDays());
            pstmt.setString(5, tour.getTransport().name());
            pstmt.setString(6, tour.getFoodType().name());
            pstmt.setString(7, tour.getTourType().name());

            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();                                        // або логування через логер!!!!!!!
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public void updateTour(TourPackage tour) throws SQLException {
        String sql = "UPDATE `tour` " +
                "SET `title`=?, `destination`=?, `price`=?, `days`=?, `transport`=?, `food_type`=?, `tour_type`=? " +
                "WHERE `tour_id`=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tour.getTitle());
            pstmt.setString(2, tour.getDestination());
            pstmt.setDouble(3, tour.getPrice());
            pstmt.setInt(4, tour.getDays());
            pstmt.setString(5, tour.getTransport().name());
            pstmt.setString(6, tour.getFoodType().name());
            pstmt.setString(7, tour.getTourType().name());
            pstmt.setInt(8, tour.getId());

            pstmt.executeUpdate();
        }
    }

    public void deleteTour(int id) throws SQLException {
        String sql = "DELETE FROM `tour` WHERE `tour_id`=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<String> getAllDestinations() throws SQLException {
        List<String> destinations = new ArrayList<>();
        String sql = "SELECT DISTINCT destination FROM tour";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                destinations.add(rs.getString("destination"));
            }
        }
        return destinations;
    }
}