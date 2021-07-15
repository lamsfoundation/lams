<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
        
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
        
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
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

	<lams:Page type="learner" title="${sessionMap.title}">
	
	<form:form action="submitReflection.do" modelAttribute="reflectionForm" method="post" onsubmit="disableFinishButton();" id="reflectionForm">
		<form:hidden path="userID" />
		<form:hidden path="sessionMapID" />

		<div id="content">
			<lams:errors/>

			<p>
				<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true"/>
			</p>

			<form:textarea cols="60" rows="8" path="entryText"
				cssClass="text-area form-control" />

			<div class="space-bottom-top align-right">
				<a href="#nogo" class="btn btn-primary voffset5 pull-right na" id="finishButton">
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
				</a>
			</div>
		</div>
	</form:form>

	<div id="footer">
	</div>
	<!--closes footer-->

	</lams:Page>
	
</body>
</lams:html>
