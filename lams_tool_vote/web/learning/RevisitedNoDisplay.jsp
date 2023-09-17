<%@ include file="/common/taglibs.jsp"%>

<c:choose>
	<c:when test="${fn:length(requestScope.listGeneralCheckedOptionsContent) > 1}">
		<h4>
			<fmt:message key="label.learner.nominations" />
		</h4>
	</c:when>

	<c:otherwise>
		<h4>
			<fmt:message key="label.learner.nomination" />
		</h4>
	</c:otherwise>
</c:choose>

<c:forEach var="entry" items="${requestScope.listGeneralCheckedOptionsContent}">
	<div class="d-flex">
		<div class="flex-shrink-0">
			<i class="fa fa-xs fa-check text-success"></i>
		</div>
		<div class="flex-grow-1 ms-3">
			<c:out value="${entry}" escapeXml="false" />
		</div>
	</div>
</c:forEach>

<c:if test="${not empty VoteLearningForm.userEntry}">
	<div class="d-flex">
		<div class="flex-shrink-0">
			<i class="fa fa-xs fa-check text-success"></i>
		</div>
		<div class="flex-grow-1 ms-3">
			<c:out value="${VoteLearningForm.userEntry}" escapeXml="true" />
		</div>
	</div>
</c:if>
