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

    // Login by email & password (any role)
    public User login(String email, String password) throws SQLException, ClassNotFoundException {
        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // Login by email, password & role (strict role check)
    public User login(String email, String password, String role) throws SQLException, ClassNotFoundException {
        User user = userDAO.getUserByEmail(email);
        if (user != null
                && user.getPassword().equals(password)
                && user.getRole() != null
                && user.getRole().equalsIgnoreCase(role)) {
            return user;
        }
        return null;
    }

    // Register, fetch, update, delete, approve, reject methods unchanged...

    public boolean registerUser(User user) throws SQLException, ClassNotFoundException {
        User existingUser = userDAO.getUserByEmail(user.getEmail());
        if (existingUser != null) {
            System.out.println("❌ Registration failed: Email already exists.");
            return false;
        }
        if (user.getApprovalStatus() == null || user.getApprovalStatus().isEmpty()) {
            user.setApprovalStatus("PENDING");
        } else {
            user.setApprovalStatus(user.getApprovalStatus().toUpperCase());
        }
        return userDAO.createUser(user);
    }

    public User getUserById(int userId) throws SQLException, ClassNotFoundException {
        return userDAO.getUserById(userId);
    }

    public List<User> getAllUsers() throws SQLException, ClassNotFoundException {
        return userDAO.getAllUsers();
    }

    public boolean updateUser(User user) throws SQLException, ClassNotFoundException {
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int userId) throws SQLException, ClassNotFoundException {
        return userDAO.deleteUser(userId);
    }

    public boolean approveUser(int userId, int adminId) throws SQLException, ClassNotFoundException {
        User user = userDAO.getUserById(userId);
        if (user == null) return false;

        user.setApprovedBy(adminId);
        user.setApprovalStatus("APPROVED");
        return userDAO.updateUser(user);
    }

    public boolean rejectUser(int userId, int adminId) throws SQLException, ClassNotFoundException {
        User user = userDAO.getUserById(userId);
        if (user == null) return false;

        user.setApprovedBy(adminId);
        user.setApprovalStatus("REJECTED");
        return userDAO.updateUser(user);
    }
}
