package service;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDAO;
import model.User;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Login by email & password (any role)
    public User login(String email, String password) throws SQLException, ClassNotFoundException {
        User user = userDAO.getUserByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // Login by email, password & role (strict role check)
    public User login(String email, String password, String role) throws SQLException, ClassNotFoundException {
        User user = userDAO.getUserByEmail(email);
        if (user != null
                && BCrypt.checkpw(password, user.getPassword())
                && user.getRole() != null
                && user.getRole().equalsIgnoreCase(role)) {
            return user;
        }
        return null;
    }

    public boolean registerUser(User user) throws SQLException, ClassNotFoundException {
        User existingUser = userDAO.getUserByEmail(user.getEmail());
        if (existingUser != null) {
            System.out.println("❌ Registration failed: Email already exists.");
            return false;
        }

        // Hash password
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hashedPassword);

        // Set approval status
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

    public boolean updateUser(User user) throws Exception {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            if (!user.getPassword().startsWith("$2a$")) {
                String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
                user.setPassword(hashedPassword);
            }
        }
        return userDAO.updateUser(user);
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) throws Exception {
        User existingUser = userDAO.getUserById(userId);
        if (existingUser == null) {
            throw new Exception("User not found");
        }

        if (!BCrypt.checkpw(oldPassword, existingUser.getPassword())) {
            throw new Exception("Old password is incorrect");
        }

        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        existingUser.setPassword(hashedNewPassword);

        return userDAO.updateUser(existingUser);
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

    public boolean resetPasswordByEmail(String email, String newPassword) throws Exception {
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            return false; 
        }

        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        user.setPassword(hashedNewPassword);

        return userDAO.updateUser(user);
    }

    public boolean sendOtpToEmail(String email) throws ClassNotFoundException, SQLException {
        User user = userDAO.getUserByEmail(email);
        if (user == null) return false;

        String otp = String.format("%06d", new Random().nextInt(1_000_000));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        userDAO.saveOtp(user.getUserId(), otp, Timestamp.valueOf(expiry));
        sendEmail(email, otp);

        System.out.println("Debug OTP " + email + ": " + otp);
        return true;
    }

    private void sendEmail(String recipientEmail, String otp) {
        final String senderEmail = "inamdarsahil708@gmail.com";
        final String appPassword = "bokz kvad tkyg caub";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderEmail));
            msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
            msg.setSubject("SSBBank OTP Verification");
            msg.setText("Your SSBBank OTP is: " + otp + "\nThis OTP is valid for 5 minutes.");
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyOtp(String email, String otp) throws ClassNotFoundException, SQLException {
        User user = userDAO.getUserByEmail(email);
        if (user == null) return false;

        String savedOtp = userDAO.getOtpByUserId(user.getUserId());
        Timestamp expiry = userDAO.getOtpExpiryByUserId(user.getUserId());

        if (savedOtp == null || expiry == null) return false;

        if (savedOtp.equals(otp) && expiry.after(Timestamp.valueOf(LocalDateTime.now()))) {
            userDAO.clearOtp(user.getUserId());
            return true;
        }

        return false;
    }
}
