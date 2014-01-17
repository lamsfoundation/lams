<%@ include file="/common/taglibs.jsp"%>

<div id="content">
	<h1>
		<fmt:message key="activity.title" />
	</h1>
			
	<div class="warning">
		<fmt:message key="authoring.info.teacher.set.restriction" >
			<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
		</fmt:message>							
	</div>						
	 	
	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
</div>

