<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<t:composition>
  <jsp:attribute name="headTitle">This is onboarding page!</jsp:attribute>
  <jsp:body>


      <c:if test="${not empty flashMessage}">

<div class="bg-green-100 text-green-700 px-4 py-2 rounded-md">
  <p class="text-sm">Oops! Something went wrong. Please try again later or contact support.</p>
                ${flashMessage}

</div>




    </c:if>
    <p class="mb-4">
        When you finished with onboarding
        you can go here:
    </p>

    <a class="text-blue-500 hover:text-blue-700 underline" href="${mvc.uri('user-list')}">users list</a>
  </jsp:body>
</t:composition>