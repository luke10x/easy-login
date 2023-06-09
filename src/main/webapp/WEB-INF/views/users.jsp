<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>

<head>
    <title>Users</title>
</head>
<body>
    <h1>Users ${users}</h1>
    <ul>
        <c:forEach var="user" items="${users}">
            <li>${user.name}</li>
        </c:forEach>
    </ul>
</body>
We have enough users, but <a href="${mvc.uri('register')}">Register another</a>
</html>