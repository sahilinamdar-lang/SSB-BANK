package controller;

import model.User;
import service.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/TransferServlet")
public class TransferServlet extends HttpServlet {

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
        String toUserIdStr = req.getParameter("toUserId");
        String amountStr = req.getParameter("amount");
        String description = req.getParameter("description");

        try {
            int toUserId = Integer.parseInt(toUserIdStr);
            double amount = Double.parseDouble(amountStr);

            boolean success = accountService.transfer(user.getUserId(), toUserId, amount, description);

            if (success) {
                req.setAttribute("message", "Transfer successful: $" + amount + " to user ID " + toUserId);
            } else {
                req.setAttribute("errorMessage", "Transfer failed. Please check balance or account details.");
            }

            req.getRequestDispatcher("transfer.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid amount or user ID");
            req.getRequestDispatcher("transfer.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
