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
		function submitForm() {
			var f = document.getElementById('reflectionForm');
			f.submit();
		}
	</script>
</lams:head>
<body class="stripes">

	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<html:form action="learning/submitReflection.do" method="post" onsubmit="disableFinishButton();" styleId="reflectionForm">
		<html:hidden property="userID" />
		<html:hidden property="sessionMapID" />

		<lams:Page type="learner" title="${sessionMap.title}">

			<%@ include file="/common/messages.jsp"%>

			<div class="panel">
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
			</div>

			<html:textarea rows="8" property="entryText" styleId="focused" styleClass="form-control" />

			<div class="voffset10 pull-right">
				<html:button property="finishButton" styleId="finishButton" onclick="submitForm()"
					styleClass="btn btn-sm btn-default">
					<fmt:message key="label.submit" />
				</html:button>
			</div>

			<div id="footer"></div>
		</lams:Page>
	</html:form>
	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>

</body>
</lams:html>
