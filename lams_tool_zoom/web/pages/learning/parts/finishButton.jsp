<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
</script>

<c:set var="notebookEntry" scope="request">
	<fmt:message key="label.notebookEntry" />
</c:set>

<html:form action="/learning" method="post">
	<html:hidden property="toolSessionID" value="${toolSessionID}" />
	<html:hidden property="dispatch" value="openNotebook" />

	<c:if test="${userDTO.finishedActivity and contentDTO.reflectOnActivity}">

		<div class="panel panel-default">
			<div class="panel-heading panel-title">${notebookEntry}</div>
			<div class="panel-body">
				<div class="panel">
					<c:out value="${contentDTO.reflectInstructions}" escapeXml="true" />
				</div>

				<c:choose>
					<c:when test="${userDTO.notebookEntryDTO != null}">
						<div class="panel-body bg-warning">
							<lams:out escapeHtml="true" value="${userDTO.notebookEntryDTO.entry}" />
						</div>
					</c:when>

					<c:otherwise>
						<fmt:message key="message.no.reflection.available" />
					</c:otherwise>
				</c:choose>
				</p>

				<html:submit styleClass="btn btn-sm btn-default">
					<fmt:message key="button.edit" />
				</html:submit>

			</div>
		</div>
	</c:if>
</html:form>

<html:form action="/learning" method="post" onsubmit="disableFinishButton();">
	<html:hidden property="toolSessionID" value="${toolSessionID}" />
	<div class="pull-right voffset10">
		<c:choose>
			<c:when test="${!userDTO.finishedActivity and contentDTO.reflectOnActivity}">
				<html:hidden property="dispatch" value="openNotebook" />

				<html:submit styleClass="btn btn-primary">
					<fmt:message key="button.continue" />
				</html:submit>
			</c:when>
			<c:otherwise>
				<html:hidden property="dispatch" value="finishActivity" />
				<html:submit styleClass="btn btn-primary na" styleId="finishButton">
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
