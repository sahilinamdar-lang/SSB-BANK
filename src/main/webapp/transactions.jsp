<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User, model.Transaction, java.util.List" %>
<%
    User user = (User) session.getAttribute("loggedUser");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
%>
<html>
<head>
    <title>Transaction History - MyBank</title>
    <link rel="stylesheet" href="style.css"/>
</head>
<body>
<div class="container">
    <h1>Transaction History</h1>
    <% if (transactions == null || transactions.isEmpty()) { %>
        <p>You have no transactions yet.</p>
    <% } else { %>
    <% out.println("Transaction list size: " + (transactions != null ? transactions.size() : "null")); %>
    
        <table class="table">
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Description</th>
                </tr>
            </thead>
            <tbody>
                <% for (Transaction t : transactions) { %>
                    <tr>
                        <td><%= t.getTransactionDate() %></td>
                        <td><%= t.getTransactionType() %></td>
                        <td>$<%= String.format("%.2f", t.getAmount()) %></td>
                        <td><%= t.getDescription() %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>
    <a href="dashboard.jsp">Back to Dashboard</a>
</div>
</body>
</html>
