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
		function submitForm(methodName) {
			var f = document.getElementById('messageForm');
			f.submit();
		}
	</script>
</lams:head>

<body class="stripes">

	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<lams:Page type="learner" title="${sessionMap.title}">

		<%@ include file="/common/messages.jsp"%>

		<div class="panel">
			<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
		</div>

		<div class="form-group">
			<html:form action="/learning/submitReflection" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
				<html:hidden property="userID" />
				<html:hidden property="sessionMapID" />
				<html:textarea property="entryText" styleClass="form-control" styleId="focused" rows="5" />


				<html:link href="#nogo" styleClass="btn btn-primary voffset10 pull-right na" styleId="finishButton"
					onclick="submitForm('finish')">

					<c:choose>
						<c:when test="${activityPosition.last}">
							<fmt:message key="button.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="button.finish" />
						</c:otherwise>
					</c:choose>

				</html:link>

			</html:form>

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
