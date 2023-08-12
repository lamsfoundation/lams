<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
		<lams:JSImport src="learning/includes/javascript/gate-check.js" />
		
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', submitForm);
		
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
		
		function submitForm(){
			var f = document.getElementById('reflectionForm');
			f.submit();
		}
	</script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${sessionMap.title}" formID="reflectionForm">
	
	<form:form action="submitReflection.do" method="post" onsubmit="disableFinishButton();" id="reflectionForm" modelAttribute="reflectionForm">
		<form:hidden path="userID" />
		<form:hidden path="sessionMapID" />

			<lams:errors/>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
			</p>

			<textarea id="focused" rows="5" name="entryText" class="form-control">${reflectionForm.entryText}</textarea>

			<div class="activity-bottom-buttons">
				<a href="#nogo" class="btn btn-primary na" id="finishButton">
					<c:choose>
						<c:when test="${sessionMap.isLastActivity}">
							<fmt:message key="label.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.finished" />
						</c:otherwise>
					</c:choose>
				</a>
			</div>

	</form:form>
	
	<div id="footer"/>
	
	</lams:Page>

</body>

<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
</script>

</lams:html>
