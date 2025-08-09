<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error - MyBank</title>
    <link rel="stylesheet" href="style.css"/>
</head>
<body>
<div class="container">
    <h1 class="alert alert-error">Oops! Something went wrong.</h1>
    <p><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "An unexpected error occurred." %></p>
    <a href="dashboard.jsp">Back to Dashboard</a>
</div>
</body>
</html>
