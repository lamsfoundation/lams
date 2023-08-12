<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>

<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>
		<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${sessionMap.toolSessionId}', '', submitForm);
		
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

	<form:form action="submitReflection.do" method="post" onsubmit="disableFinishButton();" modelAttribute="reflectionForm">
		<form:hidden path="userID" />
		<form:hidden path="sessionMapID" />

		<lams:Page type="learner" title="${sessionMap.title}">

			<lams:errors/>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
			</p>
			
			<form:textarea id="focused" rows="5" path="entryText" cssClass="form-control"></form:textarea>

			<div class="activity-bottom-buttons">
				<a href="#nogo" class="btn btn-primary na" id="finishButton">
					<c:choose>
						<c:when test="${sessionMap.isLastActivity}">
							<fmt:message key="label.finish" />
						</c:when>					
						<c:otherwise>
							<fmt:message key="label.finished" />
						</c:otherwise>
					</c:choose>
				</a>
			</div>
			
		</lams:Page>
	</form:form>

	<div id="footer">
	</div>
	<!--closes footer-->

<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
</script>

</body>
</lams:html>