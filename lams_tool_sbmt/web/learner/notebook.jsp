<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript">
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
	</script>
</lams:head>

<body class="stripes">

	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<lams:Page type="learner" title="${sessionMap.title}" formID="learnerForm">

		<%@ include file="/common/messages.jsp"%>

		<div class="panel">
			<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
		</div>

		<div class="form-group">
			<form:form action="learning/submitReflection.do" method="post" onsubmit="disableFinishButton();" modelAttribute="refForm" id="messageForm">
				<form:hidden path="userID" />
				<form:hidden path="sessionMapID" />
				<form:textarea path="entryText" cssClass="form-control" id="focused" rows="5" />


				<button class="btn btn-primary voffset10 pull-right na" id="finishButton" type="submit">

					<c:choose>
						<c:when test="${activityPosition.last}">
							<fmt:message key="button.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="button.finish" />
						</c:otherwise>
					</c:choose>

				</button>

			</form:form>

		</div>

		<div id="footer"></div>


	</lams:Page>

	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>

</body>
</lams:html>
