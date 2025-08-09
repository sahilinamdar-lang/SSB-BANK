package service;

import dao.AccountDAO;
import model.Account;
import model.Transaction;

import java.sql.SQLException;
import java.util.List;

public class AccountService {

    private AccountDAO accountDAO;
    private TransactionService transactionService;

    public AccountService() {
        this.accountDAO = new AccountDAO();
        this.transactionService = new TransactionService();
    }

    // Get all accounts for a user
    public List<Account> getAccountsByUserId(int userId) throws SQLException, ClassNotFoundException {
        return accountDAO.getAccountsByUserId(userId);
    }

    // Deposit to a specific account
    public boolean deposit(int accountId, double amount, String description) throws SQLException, ClassNotFoundException {
        if (amount <= 0) return false;

        Account account = accountDAO.getAccountByAccountId(accountId);
        if (account == null) return false;

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setTransactionType("DEPOSIT");
        transaction.setDescription(description);

        return transactionService.processTransaction(transaction);
    } 

    // Withdraw from a specific account
    public boolean withdraw(int accountId, double amount, String description) throws SQLException, ClassNotFoundException {
        if (amount <= 0) return false;

        Account account = accountDAO.getAccountByAccountId(accountId);
        if (account == null) return false;

        if (account.getBalance() < amount) return false;

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(-amount); // negative amount for withdrawal
        transaction.setTransactionType("WITHDRAW");
        transaction.setDescription(description);

        return transactionService.processTransaction(transaction);
    }

    // Transfer between accounts by account IDs (better than user IDs)
    public boolean transfer(int fromAccountId, int toAccountId, double amount, String description) throws SQLException, ClassNotFoundException {
        if (amount <= 0 || fromAccountId == toAccountId) return false;

        // Withdraw from sender account
        boolean withdrawn = withdraw(fromAccountId, amount, "Transfer to account " + toAccountId + ": " + description);
        if (!withdrawn) return false;

        // Deposit to receiver account
        boolean deposited = deposit(toAccountId, amount, "Transfer from account " + fromAccountId + ": " + description);

        // If deposit fails, refund sender
        if (!deposited) {
            deposit(fromAccountId, amount, "Refund due to failed transfer: " + description);
            return false;
        }

        return true;
    }
}
