package controller;

import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to register.jsp
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Collect user data from form
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String dobStr = request.getParameter("dateOfBirth");
        String gender = request.getParameter("gender");
        String role = request.getParameter("role");  // NEW: collect role from form

        java.sql.Date dateOfBirth = null;
        try {
            if (dobStr != null && !dobStr.isEmpty()) {
                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dobStr);
                dateOfBirth = new java.sql.Date(utilDate.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid date format.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setDateOfBirth(dateOfBirth);
        user.setGender(gender);
        user.setRole(role);  // NEW: set role in user
        user.setApprovalStatus("PENDING"); // consistent approval status

        try {
            boolean isRegistered = userService.registerUser(user);
            if (isRegistered) {
                request.setAttribute("message","Registration successful! Your account is pending approval by a manager.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Registration failed: Email already exists or invalid data.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Server error: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
