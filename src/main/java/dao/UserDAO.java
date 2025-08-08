package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.User;
import util.DBConnection;

public class UserDAO {

	public User getUserByEmailAndPassword(String email, String password) {
		String sql = "select * from users where email =? AND password =?";

		try {
			Connection conn = DBConnection.getConnection();

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, email);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return extractUserFromResultSet(rs);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return null;
	}

	public boolean createUser(User user) {
		String sql = "INSERT INTO users (full_name, email, password, phone_number, address, date_of_birth, gender, approved_by, approval_status) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, user.getFullName());

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

			int rows = ps.executeUpdate();
			if (rows > 0) {
				ResultSet keys = ps.getGeneratedKeys();

				if (keys.next()) {
					user.setUserId(keys.getInt(1));
				}
				return true;
			}

		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;

	}
	
	
	 public User getUserById(int userId) {
	        String sql = "SELECT * FROM users WHERE user_id = ?";
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setInt(1, userId);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                return extractUserFromResultSet(rs);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        return null;
	    }

	 public List<User> getAllUsers() {
	        List<User> users = new ArrayList<>();
	        String sql = "SELECT * FROM users";

	        try (Connection conn = DBConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {

	            while (rs.next()) {
	                users.add(extractUserFromResultSet(rs));
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        return users;
	 }
	 // update user
	  public boolean updateUser(User user) {
	        String sql = "UPDATE users SET full_name=?, email=?, password=?, phone_number=?, address=?, date_of_birth=?, gender=?, approved_by=?, approval_status=? WHERE user_id=?";
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
	            ps.setInt(10, user.getUserId());

	            return ps.executeUpdate() > 0;

	        } catch (SQLException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        return false;
	    }

	    //  Delete user
	    public boolean deleteUser(int userId) {
	        String sql = "DELETE FROM users WHERE user_id = ?";
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setInt(1, userId);
	            return ps.executeUpdate() > 0;

	        } catch (SQLException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        return false;
	    }

	 
	 
	 
	 
	private User extractUserFromResultSet(ResultSet rs) throws SQLException {

		return new User(rs.getInt("user_id"), rs.getString("full_name"), rs.getString("email"),
				rs.getString("password"), rs.getString("phone_number"), rs.getString("address"),
				rs.getDate("date_of_birth"), rs.getString("gender"), rs.getTimestamp("created_at"),
				rs.getObject("approved_by") != null ? rs.getInt("approved_by") : null, rs.getString("approval_status")

		);
	}

}
