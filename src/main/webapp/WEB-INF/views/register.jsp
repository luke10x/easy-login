<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="dev.luke10x.easylogin.registration.UserController" %>
<%@ page import="dev.luke10x.easylogin.registration.RegisterController" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">
    <div class="container mx-auto py-10">
        <h1 class="text-3xl font-bold text-center mb-5">Registration Form</h1>

        <div class="max-w-md mx-auto bg-white p-8 border border-gray-300 shadow-sm">
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

            <c:if test="${not empty error_handle}">
                <div class="mt-4">
                    <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
                        <strong class="font-bold">Error:</strong>
                        <span class="block sm:inline">${error_handle}</span>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    (List of already registered people is here: <a href="${mvc.uri(UserController.URI)}">users list</a>)
</body>
</html>