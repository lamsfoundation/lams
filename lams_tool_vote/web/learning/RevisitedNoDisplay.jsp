<%@ include file="/common/taglibs.jsp"%>

<div class="card lcard">
	<div class="card-header"> 
		<c:choose>
			<c:when test="${fn:length(requestScope.listGeneralCheckedOptionsContent) > 1}">
				<fmt:message key="label.learner.nominations" />
			</c:when>
			<c:otherwise>
				<fmt:message key="label.learner.nomination" />
			</c:otherwise>
		</c:choose>
	</div>
	
	<div class="card-body">
		<c:forEach var="entry" items="${requestScope.listGeneralCheckedOptionsContent}">
			<div class="d-flex">
				<div class="flex-shrink-0">
					<i class="fa fa-check text-success"></i>
				</div>
				<div class="flex-grow-1 ms-3">
					<c:out value="${entry}" escapeXml="false" />
				</div>
			</div>
		</c:forEach>
		
		<c:if test="${not empty voteLearningForm.userEntry}">
			<div class="d-flex">
				<div class="flex-shrink-0">
					<i class="fa fa-check text-success"></i>
				</div>
				<div class="flex-grow-1 ms-3">
					<c:out value="${voteLearningForm.userEntry}" escapeXml="true" />
				</div>
			</div>
		</c:if>
	</div>
</div>