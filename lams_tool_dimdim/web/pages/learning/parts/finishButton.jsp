<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
</script>

<html:form action="/learning" method="post">
	<html:hidden property="toolSessionID" value="${toolSessionID}" />
	<html:hidden property="dispatch" value="openNotebook" />

	<c:if
		test="${userDTO.finishedActivity and contentDTO.reflectOnActivity}">

		<div class="space-top">
			<h2>
				${contentDTO.reflectInstructions}
			</h2>

			<p>
				<c:choose>
					<c:when test="${userDTO.notebookEntryDTO != null}">
						<lams:out escapeHtml="true"
							value="${userDTO.notebookEntryDTO.entry}" />
					</c:when>

					<c:otherwise>
						<em><fmt:message key="message.no.reflection.available" /> </em>
					</c:otherwise>
				</c:choose>
			</p>

			<html:submit styleClass="button">
				<fmt:message key="button.edit" />
			</html:submit>
		</div>
	</c:if>
</html:form>

<html:form action="/learning" method="post"
	onsubmit="disableFinishButton();">
	<html:hidden property="toolSessionID" value="${toolSessionID}" />
	<div class="space-bottom-top align-right">
		<c:choose>
			<c:when
				test="${!userDTO.finishedActivity and contentDTO.reflectOnActivity}">
				<html:hidden property="dispatch" value="openNotebook" />

				<html:submit styleClass="button">
					<fmt:message key="button.continue" />
				</html:submit>
			</c:when>
			<c:otherwise>
				<html:hidden property="dispatch" value="finishActivity" />
				<html:submit styleClass="button" styleId="finishButton">
					<c:choose>
	 					<c:when test="${activityPosition.last}">
	 						<fmt:message key="button.submit" />
	 					</c:when>
	 					<c:otherwise>
	 		 				<fmt:message key="button.finish" />
	 					</c:otherwise>
		 			</c:choose>
				</html:submit>
			</c:otherwise>
		</c:choose>
	</div>
</html:form>
