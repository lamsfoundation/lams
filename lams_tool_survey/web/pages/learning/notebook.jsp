<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>


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

	<html:form action="/learning/submitReflection" method="post" onsubmit="disableFinishButton();" styleId="messageForm">
		<html:hidden property="userID" />
		<html:hidden property="sessionMapID" />

		<lams:Page type="learner" title="${sessionMap.title}">

			<div class="panel">
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
			</div>

			<%@ include file="/common/messages.jsp"%>

			<html:textarea rows="5" styleId="focused" property="entryText" styleClass="form-control" />

			<div class="voffset10">
				<html:link href="#nogo" styleClass="btn btn-primary pull-right na" styleId="finishButton"
					onclick="submitForm('finish')">
					<fmt:message key="label.finish" />
				</html:link>
			</div>


			<div id="footer"></div>
			<!--closes footer-->
		</lams:Page>
	</html:form>

	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>

</body>
</lams:html>
