package service;

import dao.AccountDAO;
import dao.UserDAO;
import model.Account;
import model.User;

import java.util.List;

public class ManagerService {

    private final UserDAO userDAO = new UserDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Approves a pending user and creates their account with a sequential account number.
     *
     * @param userId    ID of the user to approve
     * @param managerId ID of the manager approving the user
     * @return true if both approval and account creation succeed
     * @throws Exception if database operations fail
     */
    public boolean approveUser(int userId, int managerId) throws Exception {
        // First, approve the user
        boolean isApproved = userDAO.updateUserApproval(userId, "approved", managerId);

        if (!isApproved) {
            return false;
        }

        // Fetch the newly approved user to ensure they exist
        User user = userDAO.getUserById(userId);
        if (user == null) {
            return false;
        }

        // Create account with sequential account number
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountType("SAVINGS");
        account.setBalance(0.0);
        account.setStatus("ACTIVE");
        account.setBranchId(1); // Default branch ID

        Account createdAccount = accountDAO.createAccountWithSequentialNumber(account);

        return createdAccount != null;
    }

    /**
     * Rejects a pending user.
     *
     * @param userId    ID of the user to reject
     * @param managerId ID of the manager rejecting the user
     * @return true if the rejection succeeds
     * @throws Exception if database operations fail
     */
    public boolean rejectUser(int userId, int managerId) throws Exception {
        return userDAO.updateUserApproval(userId, "rejected", managerId);
    }

    /**
     * Gets all users whose approval status is "pending".
     *
     * @return list of pending users
     * @throws Exception if database operations fail
     */
    public List<User> getPendingUsers() throws Exception {
        return userDAO.getUsersByApprovalStatus("pending");
    }
}
