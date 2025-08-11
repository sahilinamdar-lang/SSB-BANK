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
    <title>Withdraw Funds - SSB Bank</title>
 <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css" />
</head>
<body>
<div class="container withdraw-container">
    <h1>Withdraw Money</h1>
    <form action="WithdrawServlet" method="post">
        <label for="amount">Amount to Withdraw</label>
        <input type="number" name="amount" id="amount" step="0.01" min="1" placeholder="Enter amount" required/>

        <input type="submit" value="Withdraw" class="btn-primary"/>
    </form>

    <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("message") %></div>
    <% } %>

    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("errorMessage") %></div>
    <% } %>

    <div class="back-link">
        <a href="dashboard.jsp" class="btn-secondary">← Back to Dashboard</a>
    </div>
</div>
</body>
</html>
