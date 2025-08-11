package dao;

import model.User;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User getUserByEmail(String email) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public boolean createUser(User user) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO users (full_name, email, password, phone_number, address, date_of_birth, gender, approved_by, approval_status, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhoneNumber());
            ps.setString(5, user.getAddress());
            ps.setDate(6, user.getDateOfBirth());
            ps.setString(7, user.getGender());

            if (user.getApprovedBy() != null) {
                ps.setInt(8, user.getApprovedBy());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.setString(9, user.getApprovalStatus());
            ps.setString(10, user.getRole());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setUserId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public User getUserById(int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException, ClassNotFoundException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        }
        return users;
    }

    public boolean updateUser(User user) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET full_name=?, email=?, password=?, phone_number=?, address=?, date_of_birth=?, gender=?, approved_by=?, approval_status=?, role=? WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhoneNumber());
            ps.setString(5, user.getAddress());
            ps.setDate(6, user.getDateOfBirth());
            ps.setString(7, user.getGender());

            if (user.getApprovedBy() != null) {
                ps.setInt(8, user.getApprovedBy());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.setString(9, user.getApprovalStatus());
            ps.setString(10, user.getRole());
            ps.setInt(11, user.getUserId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteUser(int userId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<User> getUsersByApprovalStatus(String status) throws SQLException, ClassNotFoundException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE approval_status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(extractUserFromResultSet(rs));
                }
            }
        }
        return users;
    }

    public boolean updateUserApproval(int userId, String status, int managerId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET approval_status = ?, approved_by = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, managerId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getDate("date_of_birth"),
                rs.getString("gender"),
                rs.getTimestamp("created_at"),
                rs.getObject("approved_by") != null ? rs.getInt("approved_by") : null,
                rs.getString("approval_status"),
                rs.getString("role")
        );
    }
}
