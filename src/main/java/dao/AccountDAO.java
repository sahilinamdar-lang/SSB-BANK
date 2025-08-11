package dao;

import model.Account;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public Account createAccountWithSequentialNumber(Account account) throws SQLException, ClassNotFoundException {
        String insertSql = "INSERT INTO accounts (user_id, account_type, balance, status, created_at, branch_id) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE accounts SET account_number = ? WHERE account_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setInt(1, account.getUserId());
                insertStmt.setString(2, account.getAccountType());
                insertStmt.setDouble(3, account.getBalance());
                insertStmt.setString(4, account.getStatus());
                insertStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                insertStmt.setInt(6, account.getBranchId());

                if (insertStmt.executeUpdate() == 0) {
                    conn.rollback();
                    throw new SQLException("Failed to insert account");
                }

                try (ResultSet keys = insertStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int accountId = keys.getInt(1);
                        String accountNumber = generateAccountNumber(accountId);

                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, accountNumber);
                            updateStmt.setInt(2, accountId);

                            if (updateStmt.executeUpdate() == 0) {
                                conn.rollback();
                                throw new SQLException("Failed to update account number");
                            }

                            conn.commit();

                            account.setAccountId(accountId);
                            account.setAccountNumber(accountNumber);
                            return account;
                        }
                    } else {
                        conn.rollback();
                        throw new SQLException("Failed to retrieve generated account ID");
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private String generateAccountNumber(int accountId) {
        // SSB + zero-padded 3 digits: SSB001, SSB002, etc.
        return String.format("SSB%03d", accountId);
    }

    public boolean updateBalance(int accountId, double newBalance, Connection conn) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Account> getAccountsByUserId(int userId) throws SQLException, ClassNotFoundException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
        }
        return accounts;
    }

    public Account getAccountByAccountId(int accountId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        }
        return null;
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setAccountType(rs.getString("account_type"));
        account.setBalance(rs.getDouble("balance"));
        account.setStatus(rs.getString("status"));
        account.setCreatedAt(rs.getTimestamp("created_at"));
        account.setBranchId(rs.getInt("branch_id"));
        return account;
    }
}
