<%@ include file="/common/taglibs.jsp"%>

<c:choose>
	<c:when test="${!chatUserDTO.finishedActivity}">
		<html:form action="/learning" method="post">
			<c:set var="dispatch" value="finishActivity" />
			<c:set var="buttonLabel" value="button.finish" />
			<c:if test="${chatDTO.reflectOnActivity}">
				<c:set var="dispatch" value="openNotebook" />
				<c:set var="buttonLabel" value="button.reflect" />
			</c:if>
			<html:hidden property="dispatch" value="${dispatch}" />
			<html:hidden property="chatUserUID" value="${chatUserDTO.uid}" />
			<html:submit styleClass="button right-buttons">
				<fmt:message>${buttonLabel}</fmt:message>
			</html:submit>
		</html:form>
		<br />
	</c:when>

	<c:otherwise>
		<c:if test="${chatDTO.reflectOnActivity }">
			<div>
				<h4 style="margin-left: 12px;">
					${chatDTO.reflectInstructions}
				</h4>
				<p>
					${chatUserDTO.notebookEntry}
				</p>
			</div>
		</c:if>
	</c:otherwise>
</c:choose>
