<%@ include file="/common/taglibs.jsp"%>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}learning/includes/javascript/gate-check.js"></script>
	
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${learningForm.toolSessionID}', '', function(){
			$('#finishForm').submit();
		});
		
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

<form:form action="openNotebook.do" method="post" modelAttribute="learningForm">
	<form:hidden path="toolSessionID" />

	<c:if test="${userDTO.finishedActivity and contentDTO.reflectOnActivity}">

		<div class="panel panel-default">
			<div class="panel-heading panel-title">${learningForm.notebookEntry}</div>
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
				<button type="submit" class="btn btn-sm btn-default">
					<fmt:message key="button.edit" />
				</button>
			</div>
		</div>
	</c:if>
</form:form>

<form:form action="${!userDTO.finishedActivity and contentDTO.reflectOnActivity ? 'openNotebook.do' : 'finishActivity.do'}"
		   method="post" onsubmit="disableFinishButton();" modelAttribute="learningForm" id="finishForm">
	<form:hidden path="toolSessionID" />
	<div class="pull-right voffset10">
		<c:choose>
			<c:when test="${!userDTO.finishedActivity and contentDTO.reflectOnActivity}">
				<button type="submit" class="btn btn-primary"><fmt:message key="button.continue" /></button>
			</c:when>
			<c:otherwise>
				<button type="button" class="btn btn-primary na" id="finishButton">
					<c:choose>
						<c:when test="${isLastActivity}">
							<fmt:message key="button.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="button.finish" />
						</c:otherwise>
					</c:choose>
				</button>
			</c:otherwise>
		</c:choose>
	</div>
</form:form>