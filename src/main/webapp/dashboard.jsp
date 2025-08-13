<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="dao.AccountDAO" %>
<%@ page import="model.Account" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.TransactionDAO" %>
<%@ page import="model.Transaction" %>

<%
    User user = (User) session.getAttribute("loggedUser");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Fetch user accounts
    AccountDAO accountDAO = new AccountDAO();
    List<Account> accounts = accountDAO.getAccountsByUserId(user.getUserId());
    double totalBalance = 0.0;
    if (accounts != null) {
        for (Account acc : accounts) {
            totalBalance += acc.getBalance();
        }
    }

    // Determine approval status color
    String approvalStatus = user.getApprovalStatus();
    String statusColor = "gray";
    if ("APPROVED".equalsIgnoreCase(approvalStatus)) {
        statusColor = "green";
    } else if ("PENDING".equalsIgnoreCase(approvalStatus)) {
        statusColor = "orange";
    } else if ("REJECTED".equalsIgnoreCase(approvalStatus)) {
        statusColor = "red";
    }

    // Fetch last transaction from first account
    Transaction lastTransaction = null;
    if (accounts != null && !accounts.isEmpty()) {
        TransactionDAO transactionDAO = new TransactionDAO();
        List<Transaction> txns = transactionDAO.getTransactionsByAccountId(accounts.get(0).getAccountId());
        if (!txns.isEmpty()) {
            lastTransaction = txns.get(txns.size() - 1);
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Dashboard - SSB Bank</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css" />
</head>
<body>
<div class="container dashboard-container">

    <!-- Header -->
    <header class="dashboard-header">
        <h1>SSB Bank Dashboard</h1>
        <p>
            Welcome, <strong><%= user.getFullName() %></strong> |
            <a href="LogoutServlet">Logout</a>
        </p>
        <hr />
    </header>

    <!-- Account Overview -->
  <section class="account-overview" style="margin-bottom: 25px;">
    <h2>Your Account Overview</h2>
    <ul>
        <li><strong>Email:</strong> <%= user.getEmail() %></li>
        <li><strong>Phone:</strong> <%= user.getPhoneNumber() %></li>
        <li><strong>Address:</strong> <%= user.getAddress() %></li>
        <li><strong>Date of Birth:</strong> <%= user.getDateOfBirth() %></li>
        <li><strong>Gender:</strong> <%= user.getGender() %></li>
        <li>
            <strong>Approval Status:</strong>
            <span style="color:<%= statusColor %>;">
                <%= approvalStatus %>
            </span>
        </li>
        <li><strong>Total Balance:</strong> ₹<%= String.format("%.2f", totalBalance) %></li>
        <li>
            <strong>Account No:</strong>
            <% if (accounts != null) {
                for (Account acc : accounts) { %>
                    <%= acc.getAccountNumber() %>&nbsp;
            <% } } %>
        </li>
    </ul>
</section>

<% if (lastTransaction != null) { %>
    <section class="last-transaction" style="margin-bottom: 25px;">
        <h3>Last Transaction</h3>
        <p>
            <strong>Type:</strong> <%= lastTransaction.getTransactionType() %><br>
            <strong>Amount:</strong> ₹<%= String.format("%.2f", lastTransaction.getAmount()) %><br>
            <strong>Balance After:</strong> ₹<%= String.format("%.2f", lastTransaction.getBalanceAfterTransaction()) %><br>
            <strong>Date:</strong> <%= lastTransaction.getTransactionDate() %>
        </p>
    </section>
<% } %>

    <!-- Actions -->
    <section class="actions-section">
        <h2>Actions</h2>
        <ul class="actions-list">
            <li><a href="deposit.jsp" class="btn-action">Deposit Money</a></li>
            <li><a href="withdraw.jsp" class="btn-action">Withdraw Money</a></li>
            <li><a href="transfer.jsp" class="btn-action">Transfer Money</a></li>
            <li><a href="TransactionHistoryServlet" class="btn-action">View Transactions</a></li>
        </ul>
    </section>

    <!-- Footer -->
    <footer class="dashboard-footer">
        <hr />
        <p>&copy; 2025 SSB Bank. All rights reserved.</p>
    </footer>

</div>
</body>
</html>
