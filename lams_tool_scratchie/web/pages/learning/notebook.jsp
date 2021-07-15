<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>


<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript" src="${lams}learning/includes/javascript/gate-check.js"></script>
	
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', submitForm);
		
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

	<form:form action="/lams/tool/lascrt11/learning/submitReflection.do" modelAttribute="reflectionForm" method="post" onsubmit="disableFinishButton();" id="reflectionForm">
		<form:hidden path="userID" />
		<form:hidden path="sessionMapID" />

		<lams:Page type="learner" title="${sessionMap.title}">

			<lams:errors/>

			<div class="panel">
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
			</div>

			<form:textarea rows="8" path="entryText" id="focused" cssClass="form-control" />

			<div class="voffset10 pull-right">
				<button name="finishButton" id="finishButton"
					class="btn btn-sm btn-default">
					<fmt:message key="label.submit" />
				</buttun>
			</div>

			<div id="footer"></div>
		</lams:Page>
	</form:form>
	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>

</body>
</lams:html>
