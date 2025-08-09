package controller;

import model.User;
import service.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/WithdrawServlet")
public class WithdrawServlet extends HttpServlet {

    private AccountService accountService;

    @Override
    public void init() {
        accountService = new AccountService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("loggedUser");
        String amountStr = req.getParameter("amount");

        try {
            double amount = Double.parseDouble(amountStr);

            boolean success = accountService.withdraw(user.getUserId(), amount, "Cash withdrawal");

            if (success) {
                req.setAttribute("message", "Withdrawal successful: $" + amount);
            } else {
                req.setAttribute("errorMessage", "Withdrawal failed. Please check balance or try again.");
            }

            req.getRequestDispatcher("withdraw.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid amount");
            req.getRequestDispatcher("withdraw.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
