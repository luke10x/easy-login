<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="dev.luke10x.easylogin.UserController" %>
<%@ page import="dev.luke10x.easylogin.RegisterController" %>

<form method="POST" action="${mvc.uri(RegisterController.URI)}">
    <input type="text" name="handle" />
    <c:if test="${not empty error_handle}">
        <div class="error_handle">
            handle error: "${error_handle}"
        </div>
    </c:if>
    <input type="submit" value="Register" />
</form>

(List of already registered people is here: <a href="${mvc.uri(UserController.URI)}">users list</a>)

