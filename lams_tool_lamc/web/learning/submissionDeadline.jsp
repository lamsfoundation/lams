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
			var f = document.getElementById('messageForm');
			f.submit();
		}
	</script>
</lams:head>

<body class="stripes">

	<c:set var="title" scope="request">
		<fmt:message key="activity.title" />
	</c:set>

	<lams:Page type="learner" title="${title}">

		<html:form action="/learning?method=displayMc&validate=false" method="POST" onsubmit="disableFinishButton();"
			styleId="messageForm">
			<html:hidden property="toolContentID" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="userID" />

			<lams:Alert id="submissionDeadline" type="danger" close="false">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>

			<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
				<html:hidden property="learnerFinished" value="Finished" />

				<html:link href="#nogo" styleClass="btn btn-primary pull-right voffset10 na" styleId="finishButton"
					onclick="javascript:submitForm('finish');return false">
					<c:choose>
						<c:when test="${activityPosition.last}">
							<fmt:message key="label.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.finished" />
						</c:otherwise>
					</c:choose>
				</html:link>
			</c:if>

			<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}">
				<html:submit property="forwardtoReflection" styleClass="button">
					<fmt:message key="label.continue" />
				</html:submit>
			</c:if>
			</div>
			<div id="footer"></div>
		</html:form>
	</lams:Page>
</body>
</lams:html>
