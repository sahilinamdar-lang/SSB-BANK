package controller;

import model.User;
import model.Account;
import model.Transaction;
import service.AccountService;
import service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/TransactionHistoryServlet")
public class TransactionHistoryServlet extends HttpServlet {

    private TransactionService transactionService;
    private AccountService accountService;

    @Override
    public void init() {
        transactionService = new TransactionService();
        accountService = new AccountService();  // Make sure you have this service implemented
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("loggedUser");
        System.out.println("=== TransactionHistoryServlet Debug ===");
        System.out.println("Logged user: " + user);

        if (user == null) {
            System.out.println("No logged user, redirecting to login");
            resp.sendRedirect("login.jsp");
            return;
        }

        System.out.println("User ID: " + user.getUserId());

        try {
            // Fetch all accounts of this user
            List<Account> accounts = accountService.getAccountsByUserId(user.getUserId());

            List<Transaction> allTransactions = new ArrayList<>();

            if (accounts != null && !accounts.isEmpty()) {
                for (Account account : accounts) {
                    List<Transaction> transactions = transactionService.getTransactionsByAccountId(account.getAccountId());
                    if (transactions != null) {
                        allTransactions.addAll(transactions);
                    }
                }
            }

            System.out.println("Found total " + allTransactions.size() + " transactions across all accounts.");

            // Set the combined list to the request attribute
            req.setAttribute("transactions", allTransactions);
            req.getRequestDispatcher("transactions.jsp").forward(req, resp);

        } catch (Exception e) {
            System.out.println("Error getting transactions: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
