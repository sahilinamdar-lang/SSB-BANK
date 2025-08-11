package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import dao.AccountDAO;
import dao.TransactionDAO;
import model.Account;
import model.Transaction;
import util.DBConnection;

public class TransactionService {

    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.accountDAO = new AccountDAO();
    }

    /**
     * Processes a single transaction: deposit or withdraw.
     * Ensures transactional atomicity: updates balance and logs transaction.
     *
     * @param transaction the transaction to process
     * @return true if success, false otherwise
     */
    public boolean processTransaction(Transaction transaction) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            Account account = accountDAO.getAccountByAccountId(transaction.getAccountId());
            if (account == null) {
                System.out.println("ERROR: Account not found for ID " + transaction.getAccountId());
                return false;
            }

            double newBalance = account.getBalance() + transaction.getAmount();

            // Prevent overdraft
            if (newBalance < 0) {
                System.out.println("ERROR: Insufficient balance");
                return false;
            }

            boolean balanceUpdated = accountDAO.updateBalance(transaction.getAccountId(), newBalance, conn);
            transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
            boolean transactionAdded = transactionDAO.addTransaction(transaction, conn);

            if (balanceUpdated && transactionAdded) {
                conn.commit();
                System.out.println("✅ TRANSACTION COMMITTED SUCCESSFULLY");
                return true;
            } else {
                conn.rollback();
                System.out.println("❌ TRANSACTION ROLLED BACK");
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("❌ TRANSACTION ERROR: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Transfer funds atomically from one user's account to another.
     *
     * @param fromUserId   source user ID
     * @param toUserId     destination user ID
     * @param amount       amount to transfer (must be > 0)
     * @param description  description for the transaction
     * @return true if transfer successful, false otherwise
     */
    public boolean transfer(int fromUserId, int toUserId, double amount, String description) {
        if (amount <= 0) {
            System.out.println("❌ Transfer amount must be greater than zero.");
            return false;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Fetch accounts for both users
            List<Account> fromAccounts = accountDAO.getAccountsByUserId(fromUserId);
            List<Account> toAccounts = accountDAO.getAccountsByUserId(toUserId);

            // Validate accounts existence
            if (fromAccounts == null || fromAccounts.isEmpty()) {
                System.out.println("ERROR: Source account not found for user ID " + fromUserId);
                return false;
            }
            if (toAccounts == null || toAccounts.isEmpty()) {
                System.out.println("ERROR: Destination account not found for user ID " + toUserId);
                return false;
            }

            // Pick first accounts from the lists
            Account fromAccount = fromAccounts.get(0);
            Account toAccount = toAccounts.get(0);

            // Check sufficient funds
            if (fromAccount.getBalance() < amount) {
                System.out.println("ERROR: Insufficient funds for transfer.");
                return false;
            }

            // Withdraw from source account (negative amount)
            double fromNewBalance = fromAccount.getBalance() - amount;
            boolean fromBalanceUpdated = accountDAO.updateBalance(fromAccount.getAccountId(), fromNewBalance, conn);
            Transaction withdrawTransaction = new Transaction();
            withdrawTransaction.setAccountId(fromAccount.getAccountId());
            withdrawTransaction.setAmount(-amount);
            withdrawTransaction.setTransactionType("TRANSFER_OUT");
            withdrawTransaction.setDescription(description);
            withdrawTransaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
            boolean withdrawLogged = transactionDAO.addTransaction(withdrawTransaction, conn);

            // Deposit to destination account
            double toNewBalance = toAccount.getBalance() + amount;
            boolean toBalanceUpdated = accountDAO.updateBalance(toAccount.getAccountId(), toNewBalance, conn);
            Transaction depositTransaction = new Transaction();
            depositTransaction.setAccountId(toAccount.getAccountId());
            depositTransaction.setAmount(amount);
            depositTransaction.setTransactionType("TRANSFER_IN");
            depositTransaction.setDescription(description);
            depositTransaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
            boolean depositLogged = transactionDAO.addTransaction(depositTransaction, conn);

            // Commit or rollback depending on success of updates and logs
            if (fromBalanceUpdated && withdrawLogged && toBalanceUpdated && depositLogged) {
                conn.commit();
                System.out.println("✅ TRANSFER COMMITTED SUCCESSFULLY");
                return true;
            } else {
                conn.rollback();
                System.out.println("❌ TRANSFER ROLLED BACK");
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("❌ TRANSFER ERROR: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Other utility methods:

    public List<Transaction> getTransactionsByUserId(int userId) throws SQLException, ClassNotFoundException {
        return transactionDAO.getTransactionsByUserId(userId);
    }

    public List<Transaction> getTransactionsByAccountId(int accountId) throws ClassNotFoundException, SQLException {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }

    public List<Transaction> getAllTransactions() throws ClassNotFoundException, SQLException {
        return transactionDAO.getAllTransactions();
    }
}
