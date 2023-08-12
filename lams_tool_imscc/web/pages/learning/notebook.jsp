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
		
		function submitForm() {
			var f = document.getElementById('reflectionForm');
			f.submit();
		}

		$(document).ready(function() {
			document.getElementById("focused").focus();
		});
	</script>
</lams:head>
<body class="stripes">

	<form:form action="submitReflection.do" modelAttribute="reflectionForm" method="post" onsubmit="disableFinishButton();" id="reflectionForm">
		<form:hidden path="userID" />
		<form:hidden path="sessionMapID" />

		<lams:Page type="learner" title="${sessionMap.title}">

			<lams:errors/>

			<div class="panel">
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
			</div>

			<form:textarea id="focused" rows="5" path="entryText" cssClass="form-control" />

			<div class="voffset10 float-end">
				<a href="#nogo" class="btn btn-primary " id="finishButton">
					<span class="na">
						<c:choose>
		 					<c:when test="${sessionMap.isLastActivity}">
		 						<fmt:message key="label.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="label.finished" />
		 					</c:otherwise>
		 				</c:choose>
		 			</span>
				</a>
			</div>
		</lams:Page>
	</form:form>

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
