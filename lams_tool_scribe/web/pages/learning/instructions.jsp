<%@ include file="/common/taglibs.jsp"%>

<div id="content">
	<h1>
		<c:out value="${scribeDTO.title}" escapeXml="true" />
	</h1>

	<c:set var="appointedScribe">
		<c:out value="${scribeSessionDTO.appointedScribe}" escapeXml="true"/>
	</c:set>

	<c:if test="${role == 'scribe'}">
		<p>
			<fmt:message key="message.scribeInstructions">
				<fmt:param value="${appointedScribe}"></fmt:param>
			</fmt:message>
		</p>
	</c:if>

	<c:if test="${role == 'learner'}">
		<p>
			<fmt:message key="message.learnerInstructions">
				<fmt:param value="${appointedScribe}"></fmt:param>
			</fmt:message>
		</p>
	</c:if>

	<html:form action="/learning.do">
		<p>
			<html:hidden property="dispatch" value="startActivity" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="mode" value="${MODE}" />
			<html:submit styleClass="button right-buttons">
				<fmt:message key="button.continue" />
			</html:submit>
		</p>
	</html:form>
	
	<div class="space-bottom"> </div>
</div>


