<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("loggedUser");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
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
        <header class="dashboard-header">
            <h1>SSB Bank Dashboard</h1>
            <p>Welcome, <strong><%= user.getFullName() %></strong> | <a href="LogoutServlet">Logout</a></p>
            <hr />
        </header>

        <section class="account-overview">
            <h2>Your Account Overview</h2>
            <ul>
                <li><strong>Email:</strong> <%= user.getEmail() %></li>
                <li><strong>Phone:</strong> <%= user.getPhoneNumber() %></li>
                <li><strong>Address:</strong> <%= user.getAddress() %></li>
                <li><strong>Date of Birth:</strong> <%= user.getDateOfBirth() %></li>
                <li><strong>Gender:</strong> <%= user.getGender() %></li>
                <li><strong>Approval Status:</strong> <%= user.getApprovalStatus() %></li>
            </ul>
        </section>

        <section class="actions-section">
            <h2>Actions</h2>
            <ul class="actions-list">
                <li><a href="deposit.jsp" class="btn-action">Deposit Money</a></li>
                <li><a href="withdraw.jsp" class="btn-action">Withdraw Money</a></li>
                <li><a href="transfer.jsp" class="btn-action">Transfer Money</a></li>
                <li><a href="TransactionHistoryServlet" class="btn-action">View Transactions</a></li>
            </ul>
        </section>

        <footer class="dashboard-footer">
            <hr />
            <p>&copy; 2025 SSB Bank. All rights reserved.</p>
        </footer>
    </div>
</body>
</html>
