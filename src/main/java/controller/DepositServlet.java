package controller;

import dao.AccountDAO;
import model.Account;
import model.User;
import service.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/DepositServlet")
public class DepositServlet extends HttpServlet {

    private AccountService accountService;
    private AccountDAO accountDAO;

    @Override
    public void init() {
        accountService = new AccountService();
        accountDAO = new AccountDAO();
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

            // Get list of accounts for user
            List<Account> accounts = accountDAO.getAccountsByUserId(user.getUserId());
            if (accounts == null || accounts.isEmpty()) {
                req.setAttribute("errorMessage", "No account found for your profile.");
                req.getRequestDispatcher("deposit.jsp").forward(req, resp);
                return;
            }

            // For simplicity, use the first account in the list
            Account account = accounts.get(0);

            boolean success = accountService.deposit(account.getAccountId(), amount, "Cash deposit");

            if (success) {
                req.setAttribute("message", "Deposit successful: $" + amount);
            } else {
                req.setAttribute("errorMessage", "Deposit failed. Please try again.");
            }

            req.getRequestDispatcher("deposit.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid amount");
            req.getRequestDispatcher("deposit.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}