<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<lams:html>
	<lams:head>
		<lams:headItems/>
	</lams:head>
	<body class="stripes">
		<script type="text/javascript">
	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	function submitForm(methodName) {
		var f = document.getElementById('messageForm');
		f.submit();
	}
		</script>

		<lams:Page type="learner" title="${notebookDTO.title}">
		
			<!-- Announcements and advanced settings -->
			<lams:Alert id="submissionDeadline" type="danger" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${notebookDTO.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
			<!-- End announcements and advanced settings -->
		
			<c:if test="${mode == 'learner' || mode == 'author'}">
				<form:form action="learning/finishActivity.do" method="post" onsubmit="disableFinishButton();" modelAttribute="messageForm" id="messageForm">
					<form:hidden path="toolSessionID" />
		
					<div class="activity-bottom-buttons">
						<button href="#nogo" class="btn btn-primary na" id="finishButton" type="button"
							onclick="submitForm('finish')">
							<c:choose>
								<c:when test="${isLastActivity}">
									<fmt:message key="button.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.finish" />
								</c:otherwise>
							</c:choose>
						</button>
					</div>
				</form:form>
			</c:if>
		</lams:Page>
		<div class="footer"></div>					
	</body>
</lams:html>



