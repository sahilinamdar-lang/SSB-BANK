package service;

import java.sql.SQLException;
import java.util.List;

import dao.UserDAO;
import model.User;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // Authenticate user by email & password
    public User login(String email, String password) throws SQLException, ClassNotFoundException {
        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }


    // Register new user with email uniqueness check
    public boolean registerUser(User user) throws SQLException, ClassNotFoundException {
        // Check if email already exists
        User existingUser = userDAO.getUserByEmail(user.getEmail());
        if (existingUser != null) {
            System.out.println("❌ Registration failed: Email already exists.");
            return false;
        }

        // Set default approval status if not set
        if (user.getApprovalStatus() == null) {
            user.setApprovalStatus("PENDING");
        }

        return userDAO.createUser(user);
    }

    // Fetch user by ID
    public User getUserById(int userId) throws SQLException, ClassNotFoundException {
        return userDAO.getUserById(userId);
    }

    // Fetch all users
    public List<User> getAllUsers() throws SQLException, ClassNotFoundException {
        return userDAO.getAllUsers();
    }

    // Update user
    public boolean updateUser(User user) throws SQLException, ClassNotFoundException {
        return userDAO.updateUser(user);
    }

    // Delete user
    public boolean deleteUser(int userId) throws SQLException, ClassNotFoundException {
        return userDAO.deleteUser(userId);
    }

    // Approve user (Admin use)
    public boolean approveUser(int userId, int adminId) throws SQLException, ClassNotFoundException {
        User user = userDAO.getUserById(userId);
        if (user == null) return false;

        user.setApprovedBy(adminId);
        user.setApprovalStatus("APPROVED");
        return userDAO.updateUser(user);
    }

    // Reject user (Admin use)
    public boolean rejectUser(int userId, int adminId) throws SQLException, ClassNotFoundException {
        User user = userDAO.getUserById(userId);
        if (user == null) return false;

        user.setApprovedBy(adminId);
        user.setApprovalStatus("REJECTED");
        return userDAO.updateUser(user);
    }
}
