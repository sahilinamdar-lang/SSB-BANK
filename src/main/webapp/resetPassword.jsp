<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Reset Password</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css" />
</head>
<body>
<div class="container">
    <h1>Reset Password</h1>
    <h2>Enter your new password below</h2>

    <form action="reset-password" method="post" class="form-card">
        <input type="hidden" name="email" value="${sessionScope.resetEmail}"/>

        <label for="password">New Password:</label>
        <input type="password" id="password" name="password" placeholder="Enter new password" required/>

        <label for="confirm">Confirm Password:</label>
        <input type="password" id="confirm" name="confirm" placeholder="Confirm new password" required/>

        <input type="submit" value="Update Password" class="btn-submit"/>
    </form>

    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>

    <div class="back-link">
        <a href="login.jsp">Back to Login</a>
    </div>
</div>
</body>
</html>
