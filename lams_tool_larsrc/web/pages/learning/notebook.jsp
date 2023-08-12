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
		checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', function(){
			$('#reflectionForm').submit();
		});
		
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
	</script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${sessionMap.title}">
	
	<form:form action="submitReflection.do" method="post" onsubmit="disableFinishButton();" modelAttribute="reflectionForm" id="reflectionForm">
		<form:hidden path="userID" />
		<form:hidden path="sessionMapID" />

		<div id="content">
			<lams:errors/>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
			</p>

			<form:textarea rows="8" path="entryText" id="focused" cssClass="form-control" />

			<div class="activity-bottom-buttons">
				<button type="submit" class="btn btn-primary na" id="finishButton">
					<span class="nextActivity">
						<c:choose>
		 					<c:when test="${sessionMap.isLastActivity}">
		 						<fmt:message key="label.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="label.finished" />
		 					</c:otherwise>
		 				</c:choose>
		 			</span>
				</button>
			</div>
		</div>
	</form:form>

	<div id="footer">
	</div>
	<!--closes footer-->

	</lams:Page>
	
</body>
</lams:html>
