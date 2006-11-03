<%@ include file="/includes/taglibs.jsp"%>


<div id="content">

<h1><c:out value="${NbLearnerForm.title}" escapeXml="false" /></h1>

	<p>
		<c:out value="${NbLearnerForm.content}" escapeXml="false" />
	</p>

	<c:if test="${!NbLearnerForm.readOnly}">
		<div align="right" class="space-bottom-top"><html:form
			action="/learner" target="_self">
			<html:hidden property="toolSessionID" />
			<html:hidden property="mode" />
			<c:choose>
				<c:when test="${reflectOnActivity}">
					<html:submit property="method" styleClass="button">
						<fmt:message key="button.continue" />
					</html:submit>
				</c:when>
				<c:otherwise>
					<html:submit property="method" styleClass="button">
						<fmt:message key="button.finish" />
					</html:submit>
				</c:otherwise>
			</c:choose>
		</html:form></div>
	</c:if>
</div>




