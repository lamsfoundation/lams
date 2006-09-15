<%@ include file="/common/taglibs.jsp"%>

<c:choose>
	<c:when test="${!scribeUserDTO.finishedActivity}">
		<html:form action="/learning" method="post">
			<c:set var="dispatch" value="finishActivity" />
			<c:set var="buttonLabel" value="button.finish" />
			<c:if test="${scribeDTO.reflectOnActivity}">
				<c:set var="dispatch" value="openNotebook" />
				<c:set var="buttonLabel" value="button.reflect" />
			</c:if>
			<html:hidden property="dispatch" value="${dispatch}" />
			<html:hidden property="scribeUserUID" value="${scribeUserDTO.uid}" />
			<html:submit styleClass="button right-buttons">
				<fmt:message>${buttonLabel}</fmt:message>
			</html:submit>
		</html:form>
		<br />
	</c:when>

	<c:otherwise>
		<c:if test="${scribeDTO.reflectOnActivity }">
			<div>
				<h4 style="margin-left: 12px;">
					${scribeDTO.reflectInstructions}
				</h4>
				<p>
					${scribeUserDTO.notebookEntry}
				</p>
			</div>
		</c:if>
	</c:otherwise>
</c:choose>
