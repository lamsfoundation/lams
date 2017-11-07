<%@ include file="/taglibs.jsp"%>

<lams:css/>

<c:forEach var="user" items="${users}">
	<li id="<c:out value="${user.userId}"/>"><c:out value="${user.login}" /> (<c:out value="${user.firstName}" />&nbsp;<c:out value="${user.lastName}" />) - <c:out value="${user.email}" /></li>
</c:forEach>
