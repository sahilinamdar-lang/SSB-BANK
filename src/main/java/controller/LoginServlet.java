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

        try {
            User user = userService.login(email, password);
            System.out.println("Email: " + email + ", Password: " + password);
            System.out.println("User from DB: " + user);

            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("loggedUser", user);
                System.out.println("Redirecting to: " + req.getContextPath() + "/dashboard.jsp");
                resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");

            } else {
                req.setAttribute("errorMessage", "Invalid email or password");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
