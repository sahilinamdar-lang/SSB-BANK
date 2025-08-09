<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("loggedUser");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
<head>
    <title>Dashboard - SSB</title>
    <link rel="stylesheet" href="style.css"/>
</head>
<body>
<div class="navbar">
    <div>SSB Bank Dashboard</div>
    <div>
        <span>Welcome, <strong><%= user.getFullName() %></strong></span>
        <a href="LogoutServlet">Logout</a>
    </div>
</div>

<div class="container">
    <h1>Your Account Overview</h1>
    <p><strong>Email:</strong> <%= user.getEmail() %></p>
    <p><strong>Phone:</strong> <%= user.getPhoneNumber() %></p>
    <p><strong>Address:</strong> <%= user.getAddress() %></p>
    <p><strong>Date of Birth:</strong> <%= user.getDateOfBirth() %></p>
    <p><strong>Gender:</strong> <%= user.getGender() %></p>
    <p><strong>Approval Status:</strong> <%= user.getApprovalStatus() %></p>

    <hr/>
    <h2>Actions</h2>
    <div>
        <a href="deposit.jsp"><button>Deposit Money</button></a>
        <a href="withdraw.jsp"><button>Withdraw Money</button></a>
        <a href="transfer.jsp"><button>Transfer Money</button></a>
        <a href="transactions.jsp"><button>View Transactions</button></a>
    </div>
</div>

<div class="footer">
    &copy; 2025 MyBank. All rights reserved.
</div>
</body>
</html>
