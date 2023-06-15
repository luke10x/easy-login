<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<t:composition>
  <jsp:attribute name="headTitle">This is onboarding page!</jsp:attribute>
  <jsp:body>

    <p class="mb-4">
        When you finished with on-boarding
        you can go here:
    </p>

    <a class="text-blue-500 hover:text-blue-700 underline" href="${mvc.uri('user-list')}">users list</a>
  </jsp:body>
</t:composition>