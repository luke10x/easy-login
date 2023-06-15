<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="dev.luke10x.easylogin.registration.UserController" %>
<%@ page import="dev.luke10x.easylogin.registration.RegisterController" %>

<t:composition>
  <jsp:attribute name="headTitle">Register your handle</jsp:attribute>
  <jsp:body>
        <p class="mb-4">
            Easy Login does not ask you to enter your email or phone number.
            It does not even need you to choose a password.
            All you need to do is to choose a handle (login name) to experience
            quick and secure authentication!
        </p>

        <form method="POST" action="${mvc.uri(RegisterController.URI)}">
            <div class="mb-4">
                <label for="handle" class="block text-gray-700">
                    Enter handle (user name) which you will use for your password-less login
                </label>
                <input type="text" id="handle" name="handle" class="form-input border py-2 px-1 mt-1 block w-full" required>
            </div>

            <div class="text-center">
                <button type="submit" class="py-2 px-4 bg-blue-500 text-white rounded hover:bg-blue-600">Register</button>
            </div>
        </form>

        <c:if test="${not empty registrationModel.handleValidationError}">
            <div class="mt-4">
                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
                    <strong class="font-bold">Error:</strong>
                    <span class="block sm:inline">${registrationModel.handleValidationError}</span>
                </div>
            </div>
        </c:if>

    (List of already registered people is here: <a href="${mvc.uri(UserController.URI)}">users list</a>)
  </jsp:body>
</t:composition>

