<%@ include file="/common/taglibs.jsp"%>

<div id="content">
	<h1>
		<fmt:message key="activity.title" />
	</h1>
	
	<p>
		<fmt:message key="message.runOfflineSet" />
	</p>

	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
</div>

