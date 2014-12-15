<%@ include file="/common/taglibs.jsp"%>

<c:choose>
	 <c:when test="${fn:length(requestScope.listGeneralCheckedOptionsContent) > 1}">
	    <h2><fmt:message key="label.learner.nominations" /></h2>
	 </c:when>
	 
	 <c:otherwise>
	    <h2><fmt:message key="label.learner.nomination" /></h2>
	 </c:otherwise>
</c:choose>

<c:forEach var="entry" items="${requestScope.listGeneralCheckedOptionsContent}">
	<div>
		<c:out value="${entry}" escapeXml="false" />
	</div>
</c:forEach>

<div>
	<lams:out value="${VoteLearningForm.userEntry}" escapeHtml="true" />
</div>
