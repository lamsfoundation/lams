<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
		
		<c:set var="lams"> <lams:LAMSURL /> </c:set>
		<c:set var="tool"> <lams:WebAppURL /> </c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
		<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
		<link href="${lams}css/defaultHTML_learner.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<lams:JSImport src="learning/includes/javascript/gate-check.js" />
		
	</lams:head>
	<body class="stripes">
			
			<script type="text/javascript">
				checkNextGateActivity('finishButton', '${toolSessionID}', '', function(){
					 submitForm('finishActivity');
				});
			
				function disableFinishButton() {
					document.getElementById("finishButton").disabled = true;
				}
				function submitForm(methodName) {
					var f = document.getElementById('learningForm');
					f.submit();
				}
			</script>
			
			<lams:Page type="learner" title="${chatDTO.title}" formID="learningForm">
			
				<form:form action="submitReflection.do" method="post" onsubmit="disableFinishButton();" modelAttribute="learningForm" id="learningForm">
			
					<div class="panel">
						<lams:out value="${chatDTO.reflectInstructions}" escapeHtml="true" />
					</div>
			
					<form:textarea rows="5" path="entryText" cssClass="form-control" id="focused"></form:textarea>
			
					<form:hidden path="chatUserUID" />
					<a href="#nogo" class="btn btn-primary float-end mt-2 na" id="finishButton">
						<c:choose>
							<c:when test="${isLastActivity}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>
					</a>
				</form:form>
			</lams:Page>
			
			
			<script type="text/javascript">
				window.onload = function() {
					document.getElementById("focused").focus();
				}
			</script>



			
	</body>
</lams:html>
