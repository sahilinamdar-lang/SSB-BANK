<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error - SSBBank</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css" />
    <style>
        body {
            background: linear-gradient(135deg, #0a3759, #1e6091);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            color: #fff;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .error-card {
            background: #fff;
            color: #22313f;
            padding: 2rem 2.5rem;
            border-radius: 15px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.15);
            text-align: center;
            max-width: 400px;
            width: 100%;
        }
        .error-logo {
            font-size: 2rem;
            font-weight: bold;
            color: #0a3759;
            margin-bottom: 1rem;
        }
        .error-title {
            font-size: 1.5rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
        }
        .error-message {
            font-size: 1rem;
            margin-bottom: 1.5rem;
            color: #555;
        }
        .btn {
            display: inline-block;
            background-color: #0a3759;
            color: #fff;
            padding: 0.6rem 1.2rem;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            transition: background 0.3s ease;
        }
        .btn:hover {
            background-color: #1e6091;
        }
        .error-code {
            margin-top: 1rem;
            font-size: 0.85rem;
            color: #888;
        }
    </style>
</head>
<body>

<div class="error-card">
    <div class="error-logo">SSB BANK</div>
    <div class="error-title">Oops! Something went wrong.</div>
    <div class="error-message">
        We’re experiencing technical difficulties. Please try again later or contact our support team.
    </div>
    <a href="<%= request.getContextPath() %>/dashboard.jsp" class="btn">Back to Dashboard</a>

    <div class="error-code">
        <%-- Show details in dev mode (optional) --%>
        <%
            if (pageContext.getErrorData() != null) {
        %>
            Code: <%= pageContext.getErrorData().getStatusCode() %> |
            Path: <%= pageContext.getErrorData().getRequestURI() %>
        <%
            }
        %>
    </div>
</div>

</body>
</html>
