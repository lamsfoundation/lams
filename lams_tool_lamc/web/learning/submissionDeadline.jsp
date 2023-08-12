<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<lams:css />

	<title><fmt:message key="activity.title" /></title>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		function disableFinishButton() {
			var elem = document.getElementById("finishButton");
			if (elem != null) {
				elem.disabled = true;
			}
		}

		function submitForm(methodName) {
			var f = document.getElementById('mcLearningForm');
			f.submit();
		}
	</script>
</lams:head>

<body class="stripes">

	<c:set var="title" scope="request">
		<fmt:message key="activity.title" />
	</c:set>

	<lams:Page type="learner" title="${title}" formID="mcLearningForm">

		<form:form action="displayMc.do" method="POST" onsubmit="disableFinishButton();"
			modelAttribute="mcLearningForm" id="mcLearningForm">
			<form:hidden path="toolContentID" />
			<form:hidden path="toolSessionID" />
			<form:hidden path="httpSessionID" />
			<form:hidden path="userID" />

			<lams:Alert id="submissionDeadline" type="danger" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>

			<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
				<form:hidden path="learnerFinished" value="Finished" />

				<a href="#nogo" class="btn btn-primary float-end voffset10 na" id="finishButton"
					onclick="javascript:submitForm('finish');return false">
					<c:choose>
						<c:when test="${isLastActivity}">
							<fmt:message key="label.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.finished" />
						</c:otherwise>
					</c:choose>
				</a>
			</c:if>

			<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}">
				<button type="submit" name="forwardtoReflection" class="btn">
					<fmt:message key="label.continue" />
				</button>
			</c:if>
			</div>
			<div id="footer"></div>
		</form:form>
	</lams:Page>
</body>
</lams:html>
