<%@ include file="/taglibs.jsp"%>

<lams:css style="learner"/>

<c:forEach var="user" items="${users}">
	<li id="<c:out value="${user.userId}"/>"><c:out value="${user.login}" /> (<c:out value="${user.firstName}" /> <c:out value="${user.lastName}" />) - <c:out value="${user.email}" /></li>
</c:forEach>