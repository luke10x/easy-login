<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="headTitle" fragment="true" %>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>

<!DOCTYPE html>
<html lang="en">
  <head>
      <meta charset="UTF-8">
      <title><jsp:invoke fragment="headTitle"/></title>
      <link rel="icon" type="image/svg+xml" href="/favicon.svg"></link>
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <link rel="stylesheet" href="https://unpkg.com/tailwindcss@2.2.19/dist/tailwind.min.css">
      <script src="https://cdn.tailwindcss.com"></script>
      <script>
        tailwind.config = {
          theme: {
            extend: {
              colors: {
                'my-link': '#3182ce',
                'my-hover-link': '#63b3ed',
              },
              opacity: ['disabled'],
            }
          }
        }
      </script>
  </head>

  <body class="bg-gray-100">
    <div>
      <jsp:invoke fragment="header" />
    </div>
    <div class="container mx-auto py-10">
      <h1 class="text-3xl font-bold text-center mb-5">
         <jsp:invoke fragment="headTitle"/>
      </h1>
    </div>
    <div class="max-w-md mx-auto bg-white p-8 border border-gray-300 shadow-sm">
      <jsp:doBody/>
    </div>
    <div>
      <jsp:invoke fragment="footer" />
    </div>
  </body>
</html>