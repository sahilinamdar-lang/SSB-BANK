<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Register - SSBBank</title>
    <!-- Correct path to CSS -->
     <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css" />
</head>
<body>
        <div class="container">
        <h1>Create Your Account</h1>
        <h2>Secure & Trusted Banking Registration</h2>

        <form action="RegisterServlet" method="post">
            <label for="fullName">Full Name</label>
            <input type="text" name="fullName" id="fullName" placeholder="Your name" required/>

            <label for="email">Email</label>
            <input type="email" name="email" id="email" placeholder="you@example.com" required/>

            <label for="password">Password</label>
            <input type="password" name="password" id="password" placeholder="Enter a strong password" required/>

            <label for="phoneNumber">Phone Number</label>
            <input type="text" name="phoneNumber" id="phoneNumber" placeholder="Phone Number"/>

            <label for="address">Address</label>
            <input type="text" name="address" id="address" placeholder="123 Main St, City, Country"/>

            <label for="dateOfBirth">Date of Birth</label>
            <input type="date" name="dateOfBirth" id="dateOfBirth"/>

            <label for="gender">Gender</label>
            <select name="gender" id="gender" required>
                <option value="" selected disabled>Choose gender</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
            </select>

            <label for="role">Role</label>
            <select name="role" id="role" required>
                <option value="" selected disabled>Choose role</option>
                <option value="USER">USER</option>          
                <option value="MANAGER">Branch Manager</option>
            </select>

            <input type="submit" value="Register"/>
        </form>

        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("errorMessage") %></div>
        <% } %>

        <% if (request.getAttribute("message") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("message") %></div>
        <% } %>

        <p>Already have an account? <a href="login.jsp">Login here</a></p>
    </div>
</body>
</html>
