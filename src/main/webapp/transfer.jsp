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
    <title>Transfer Funds - MyBank</title>
     <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css" />
</head>
<body>
<div class="container">
    <h1>Transfer Money</h1>
    <form action="TransferServlet" method="post">
        <label for="recipientEmail">Recipient's Email</label>
        <input type="email" name="recipientEmail" id="recipientEmail" placeholder="recipient@example.com" required/>

        <label for="amount">Amount to Transfer</label>
        <input type="number" name="amount" id="amount" step="0.01" min="1" placeholder="Enter amount" required/>

        <input type="submit" value="Transfer"/>
    </form>

    <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("message") %></div>
    <% } %>

    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("errorMessage") %></div>
    <% } %>

    <a href="dashboard.jsp" class="btn-secondary">← Back to Dashboard</a>
</div>
</body>
</html>
