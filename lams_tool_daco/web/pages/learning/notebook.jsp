<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
        

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="daco" value="${sessionMap.daco}" />
	
	<title>
		<fmt:message key="label.learning.title" />
	</title>
</lams:head>
<body class="stripes">

<script type="text/javascript" src="${lams}learning/includes/javascript/gate-check.js"></script>	
<script type="text/javascript">
	checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', submitForm);
	
    function submitForm(){
           var f = document.getElementById('messageForm');
           f.submit();
   }
</script>

	<lams:Page type="learner" title="${daco.title}">

	<form:form action="submitReflection.do" modelAttribute="messageForm" method="post" onsubmit="javascript:document.getElementById('finishButton').disabled = true;" id="messageForm">
		<form:hidden path="userId" />
		<form:hidden path="sessionId" />
		<form:hidden path="sessionMapID" />
		
			<lams:errors/>

			<p>
				<lams:out value="${daco.reflectInstructions}" escapeHtml="true" />
			</p>

			<form:textarea id="focused" rows="5" path="entryText" cssClass="form-control"></form:textarea>

			<div class="space-bottom-top align-right">
				<button type="submit" class="btn btn-primary voffset5 pull-right na" id="finishButton">
					<span class="nextActivity">
						<c:choose>
		 					<c:when test="${sessionMap.isLastActivity}">
		 						<fmt:message key="label.learning.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="label.learning.finished" />
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

<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focused").focus();
	}
</script>

