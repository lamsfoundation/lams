<%@ include file="/common/taglibs.jsp"%>

<c:set var="title" scope="request">
		<fmt:message key="activity.title" />
</c:set>		

<lams:Page type="learner" title="${title}">
	<lams:Alert type="danger" close="false" id="submissionDeadline">
		<fmt:message key="authoring.info.teacher.set.restriction" >
			<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
		</fmt:message>							
	</lams:Alert>
	 	
	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
</lams:Page>

