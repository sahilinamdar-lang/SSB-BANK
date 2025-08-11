package controller;

import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");  // Get role from login form

        try {
            // Pass role to login method to validate user with role
            User user = userService.login(email, password, role);
            System.out.println("Email: " + email + ", Password: " + password + ", Role: " + role);
            System.out.println("User from DB: " + user);

            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("loggedUser", user); // store user object

                // Redirect based on role
                if ("MANAGER".equalsIgnoreCase(user.getRole())) {
                    resp.sendRedirect(req.getContextPath() + "/manager/dashboard");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
                }
            } else {
                req.setAttribute("errorMessage", "Invalid email, password, or role");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
