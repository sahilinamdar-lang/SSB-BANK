<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Forgot Password</title>
    <!-- Link to your existing CSS -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css" />
</head>
<body>
    <div class="container">
        <h1>Forgot Password</h1>
        <h2>Enter your registered email to receive an OTP</h2>

        <form action="forgot-password" method="post">
            <label for="email">Registered Email:</label>
            <input type="email" id="email" name="email" placeholder="example@mail.com" required/>

            <input type="submit" value="Send OTP"/>
        </form>

        <!-- Display error or success messages -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("success") %></div>
        <% } %>

        <p class="back-link">
            <a href="login.jsp">Back to Login</a>
        </p>
    </div>
</body>
</html>
