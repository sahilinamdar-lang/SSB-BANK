<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manager Dashboard - SSB Bank</title>
     <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css" />
</head>
<body>
<div class="container">
    <h1>Pending User Approvals</h1>

    <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("message") %></div>
    <% } %>

    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("errorMessage") %></div>
    <% } %>

    <table class="approval-table">
        <thead>
        <tr>
            <th>User ID</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Approval Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            java.util.List<model.User> pendingUsers = (java.util.List<model.User>) request.getAttribute("pendingUsers");
            if (pendingUsers == null || pendingUsers.isEmpty()) {
        %>
            <tr>
                <td colspan="5" style="text-align:center;">No pending users</td>
            </tr>
        <%
            } else {
                for (model.User user : pendingUsers) {
        %>
            <tr>
                <td><%= user.getUserId() %></td>
                <td><%= user.getFullName() %></td>
                <td><%= user.getEmail() %></td>
                <td><%= user.getApprovalStatus() %></td>
                <td>
                    <form action="<%= request.getContextPath() %>/manager/ApproveUserServlet" method="post" class="inline-form">
                        <input type="hidden" name="userId" value="<%= user.getUserId() %>"/>
                        <button type="submit" name="action" value="approve" class="btn btn-approve">Approve</button>
                    </form>
                    <form action="<%= request.getContextPath() %>/manager/ApproveUserServlet" method="post" class="inline-form">
                        <input type="hidden" name="userId" value="<%= user.getUserId() %>"/>
                        <button type="submit" name="action" value="reject" class="btn btn-reject">Reject</button>
                    </form>
                </td>
            </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>
