<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>


<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', function(){
			$('#messageForm').submit();
		});
		
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
	</script>
</lams:head>
<body class="stripes">
	<form:form action="submitReflection.do" method="post" onsubmit="disableFinishButton();" id="messageForm" modelAttribute="messageForm">
		<form:hidden path="userID" />
		<form:hidden path="sessionMapID" />

		<lams:Page type="learner" title="${sessionMap.title}">

			<div class="panel">
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
			</div>

			<lams:errors/>

			<form:textarea rows="5" id="focused" path="entryText" cssClass="form-control" />

	
			<div class="activity-bottom-buttons">
				<button class="btn btn-primary na" id="finishButton" type="button">
					<fmt:message key="label.finish" />
				</button>
			</div>

		</lams:Page>
	</form:form>

	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>

</body>
</lams:html>
