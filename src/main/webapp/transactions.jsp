<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Transaction" %>

<%
    if (session.getAttribute("loggedUser") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Transaction History - SSB Bank</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css" />
</head>
<body>
<div class="container transaction-history-container">
    <h1>Transaction History</h1>

    <% if (transactions == null) { %>
        <p class="alert alert-error">Transactions data not found.</p>
    <% } else if (transactions.isEmpty()) { %>
        <p>No transactions found.</p>
    <% } else { %>
        <p><strong>Total transactions:</strong> <%= transactions.size() %></p>

        <div class="table-responsive">
            <table class="transaction-table">
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
                        <td><%= t.getDescription() == null ? "" : t.getDescription() %></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    <% } %>

    <div class="back-link">
        <a href="dashboard.jsp" class="btn-secondary">← Back to Dashboard</a>
    </div>
</div>
</body>
</html>
