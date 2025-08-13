package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import service.UserService;

import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        if (!password.equals(confirm)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("resetPassword.jsp").forward(req, resp);
            return;
        }

        try {
            boolean updated = userService.resetPasswordByEmail(email, password);
            if (updated) {
                // cleanup session
                req.getSession().removeAttribute("resetEmail");
                resp.sendRedirect("login.jsp?reset=success");
            } else {
                req.setAttribute("error", "Could not update password. Try again.");
                req.getRequestDispatcher("resetPassword.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
