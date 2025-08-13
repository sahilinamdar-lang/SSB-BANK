package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import service.UserService;

import java.io.IOException;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService(); // you said your service has default ctor
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        try {
            boolean sent = userService.sendOtpToEmail(email);
            if (sent) {
                // keep email in session between steps
                HttpSession session = req.getSession();
                session.setAttribute("resetEmail", email);
                req.getRequestDispatcher("verifyOtp.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "Email not found.");
                req.getRequestDispatcher("forgotPassword.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
