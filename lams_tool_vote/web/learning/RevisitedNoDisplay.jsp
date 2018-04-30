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
	<div class="media">
		<div class="media-left">
			<i class="fa fa-xs fa-check text-success"></i>
		</div>
		<div class="media-body">
			<c:out value="${entry}" escapeXml="false" />
		</div>
	</div>
</c:forEach>

<c:if test="${not empty VoteLearningForm.userEntry}">
	<div class="media">
		<div class="media-left">
			<i class="fa fa-xs fa-check text-success"></i>
		</div>
		<div class="media-body">
			<c:out value="${VoteLearningForm.userEntry}" escapeXml="true" />
		</div>
	</div>
</c:if>
