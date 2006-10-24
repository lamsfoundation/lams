<%@ include file="/common/taglibs.jsp"%>

<c:if
	test="${scribeUserDTO.finishedActivity and scribeDTO.reflectOnActivity}">
	<div>
		<h4 style="margin-left: 12px;">
			${scribeDTO.reflectInstructions}
		</h4>
		<p>
			${scribeUserDTO.notebookEntry}
		</p>
	</div>
</c:if>

<html:form action="/learning" method="post">
	<html:hidden property="scribeUserUID" value="${scribeUserDTO.uid}" />
	<c:choose>
		<c:when
			test="${!scribeUserDTO.finishedActivity and scribeDTO.reflectOnActivity}">
			<html:hidden property="dispatch" value="openNotebook" />

			<html:submit styleClass="button right-buttons">
				<fmt:message key="button.continue" />
			</html:submit>

		</c:when>
		<c:otherwise>
			<html:hidden property="dispatch" value="finishActivity" />
			<html:submit styleClass="button right-buttons">
				<fmt:message key="button.finish" />
			</html:submit>
		</c:otherwise>
	</c:choose>
</html:form>