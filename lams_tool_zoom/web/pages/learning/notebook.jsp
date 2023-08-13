<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:head>  
	<title>
		<fmt:message key="activity.title" />
	</title>
	
	<lams:css/>
	
	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<lams:JSImport src="learning/includes/javascript/gate-check.js" />
		
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${learningForm.toolSessionID}', '', function(){
			$('#learningForm').submit();
		});
		
		function disableFinishButton() {
			document.getElementById("finishButton").disabled = true;
		}
	</script>	
</lams:head>
<body class="stripes">
	<lams:Page type="learner" title="${contentDTO.title}">

		<form:form action="submitReflection.do" method="post" onsubmit="disableFinishButton();" modelAttribute="learningForm">
	
			<div class="panel">
				<lams:out value="${contentDTO.reflectInstructions}" escapeHtml="true" />
			</div>
	
			<form:textarea id="focused" rows="5" path="entryText" cssClass="form-control" />
	
			<form:hidden path="toolSessionID" />
			
			<div class="activity-bottom-buttons">
				<button type="button" class="btn btn-primary na" id="finishButton">
					<c:choose>
						<c:when test="${isLastActivity}">
							<fmt:message key="button.submit" />
						</c:when>
						<c:otherwise>
							<fmt:message key="button.finish" />
						</c:otherwise>
					</c:choose>
				</button>
			</div>
		</form:form>
		
		<script type="text/javascript">
			window.onload = function() {
				document.getElementById("focused").focus();
			}
		</script>
	
	</lams:Page>
</body>
</lams:html>