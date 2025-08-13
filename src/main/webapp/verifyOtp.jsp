<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Verify OTP</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css" />
</head>
<body>
<div class="container">
    <h1>Verify OTP</h1>
    <h2>Enter the OTP sent to your email</h2>

    <form action="verify-otp" method="post" class="form-card">
        <input type="hidden" name="email" value="${param.email != null ? param.email : sessionScope.resetEmail}"/>

        <label for="otp">OTP:</label>
        <input type="text" id="otp" name="otp" maxlength="6" placeholder="Enter 6-digit OTP" required/>

        <input type="submit" value="Verify OTP" class="btn-submit"/>
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
