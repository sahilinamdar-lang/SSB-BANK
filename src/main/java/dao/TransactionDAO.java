package dao;

import model.Transaction;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public boolean addTransaction(Transaction transaction, Connection conn) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, amount, transaction_type, description, transaction_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transaction.getAccountId());
            ps.setDouble(2, transaction.getAmount());
            ps.setString(3, transaction.getTransactionType());
            ps.setString(4, transaction.getDescription());
            ps.setTimestamp(5, transaction.getTransactionDate());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Transaction> getTransactionsByUserId(int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT t.* FROM transactions t JOIN accounts a ON t.account_id = a.account_id WHERE a.user_id = ?";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }

        return transactions;
    }

    public List<Transaction> getTransactionsByAccountId(int accountId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM transactions WHERE account_id = ?";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }

        return transactions;
    }

    public List<Transaction> getAllTransactions() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM transactions";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        }

        return transactions;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setTransactionId(rs.getInt("transaction_id"));
        t.setAccountId(rs.getInt("account_id"));
        t.setAmount(rs.getDouble("amount"));
        t.setTransactionType(rs.getString("transaction_type"));
        t.setDescription(rs.getString("description"));
        t.setTransactionDate(rs.getTimestamp("transaction_date"));
        return t;
    }
}
