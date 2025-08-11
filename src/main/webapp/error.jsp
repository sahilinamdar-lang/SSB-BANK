<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
      <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css" />
    <title>Error - MyBank</title>
  
</head>
<body>
<div class="container">
    <h1 class="alert alert-error">Oops! Something went wrong.</h1>
    <p class="error-message">
        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "An unexpected error occurred." %>
    </p>
    <a href="dashboard.jsp" class="btn">Back to Dashboard</a>
</div>
</body>
</html>
