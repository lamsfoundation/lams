<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
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
		
		function submitForm(){
			var f = document.getElementById('reflectionForm');
			f.submit();
		}
	</script>
</lams:head>
<body class="stripes">
	<lams:Page type="learner" title="${sessionMap.title}" formID="reflectionForm">
		
		<form:form action="submitReflection.do" method="post" onsubmit="disableFinishButton();" modelAttribute="reflectionForm" id="reflectionForm">
			<form:hidden path="userID" />
			<form:hidden path="sessionMapID" />

			<lams:errors/>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
			</p>

			<textarea id="focused" rows="5" name="entryText" class="form-control"></textarea>

			<a href="#nogo" class="btn btn-primary voffset5 pull-right" id="finishButton"">
				<div class="na"> 
					<c:choose>
						<c:when test="${sessionMap.isLastActivity}">
							<fmt:message key="label.submit" />
						</c:when>
						<c:otherwise>
			 				<fmt:message key="label.finished" />
						</c:otherwise>
					</c:choose>
				</div>
			</a>
				
		</form:form>
	
		<div id="footer">
		</div>
		<!--closes footer-->
	
	</lams:Page>

	<script type="text/javascript">
		window.onload = function() {
			document.getElementById("focused").focus();
		}
	</script>
</body>
</lams:html>
