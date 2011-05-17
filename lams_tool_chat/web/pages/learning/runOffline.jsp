<%@ include file="/common/taglibs.jsp"%>

<div id="content">
	<h1>
		<fmt:message key="activity.title" />
	</h1>
	
	 <c:choose>
	 	<c:when test="${empty submissionDeadline}">
			<p>
				<fmt:message key="message.runOfflineSet" />
			</p>
		</c:when>
		<c:otherwise>
			<div class="warning">
				<fmt:message key="authoring.info.teacher.set.restriction" >
					<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
				</fmt:message>							
			</div>
		</c:otherwise>
	</c:choose>								
	 	
	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
</div>

