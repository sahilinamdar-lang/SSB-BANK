<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login - MyBank</title>
    <link rel="stylesheet" href="style.css"/>
</head>
<body>
<div class="container">
    <h1>Welcome Back!</h1>
    <form action="LoginServlet" method="post">
        <label for="email">Email</label>
        <input type="text" name="email" id="email" placeholder="you@example.com" required/>

        <label for="password">Password</label>
        <input type="password" name="password" id="password" placeholder="Enter your password" required/>

        <input type="submit" value="Login"/>
    </form>

    <p>Don't have an account? <a href="register.jsp">Register here</a></p>

    <% if(request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("errorMessage") %></div>
    <% } %>
</div>
</body>
</html>
