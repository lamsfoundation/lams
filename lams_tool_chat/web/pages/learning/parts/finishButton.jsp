<%@ include file="/common/taglibs.jsp"%>

<c:if
	test="${chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}">
	<div>
		<h4 style="margin-left: 12px;">
			${chatDTO.reflectInstructions}
		</h4>
		<p>
			${chatUserDTO.notebookEntry}
		</p>
	</div>
</c:if>

<html:form action="/learning" method="post">
	<html:hidden property="chatUserUID" value="${chatUserDTO.uid}" />
	<c:choose>
		<c:when
			test="${!chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}">
			<html:hidden property="dispatch" value="openNotebook" />

			<html:submit styleClass="button right-buttons">
				<fmt:message key="button.continue" />
			</html:submit>

		</c:when>
		<c:otherwise>
			<html:hidden property="dispatch" value="finishActivity" />

			<div class="small-space-bottom" align="right">
				<html:submit styleClass="button">
					<fmt:message key="button.finish" />
				</html:submit>
			</div>
		</c:otherwise>
	</c:choose>
</html:form>
