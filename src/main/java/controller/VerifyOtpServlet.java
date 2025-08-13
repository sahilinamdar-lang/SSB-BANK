package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import service.UserService;

import java.io.IOException;

@WebServlet("/verify-otp")
public class VerifyOtpServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String otp = req.getParameter("otp");

        try {
            boolean ok = userService.verifyOtp(email, otp);
            if (ok) {
                // OTP valid — allow reset
                req.getRequestDispatcher("resetPassword.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "Invalid or expired OTP");
                req.getRequestDispatcher("verifyOtp.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
