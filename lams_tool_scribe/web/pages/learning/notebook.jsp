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
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<lams:JSImport src="learning/includes/javascript/gate-check.js" />
		
		<script type="text/javascript">
			checkNextGateActivity('finishButton', '${toolSessionID}', '', submitForm);
			
			function disableFinishButton() {
				document.getElementById("finishButton").disabled = true;
			}
			
	         function submitForm(){
	                var f = document.getElementById('learningForm');
	                f.submit();
	        }
	</script>
	</lams:head>
	<body class="stripes">
	
		
		<lams:Page type="learner" title="${scribeDTO.title}">
		
			<form:form action="submitReflection.do"  modelAttribute="learningForm" method="post" onsubmit="disableFinishButton();" id="learningForm">
				<form:hidden path="scribeUserUID" />
		
				<div class="panel">
					<lams:out value="${scribeDTO.reflectInstructions}" escapeHtml="true"/>
				</div>
		
				<form:textarea rows="5" id="focused" path="entryText"
					cssClass="form-control"></form:textarea>
		
				<div class="activity-bottom-buttons">
					<a href="#nogo" class="btn btn-primary na" id="finishButton">
						<span class="nextActivity">
							<c:choose>
			 					<c:when test="${isLastActivity}">
			 						<fmt:message key="button.submitActivity" />
			 					</c:when>
			 					<c:otherwise>
			 		 				<fmt:message key="button.finish" />
			 					</c:otherwise>
			 				</c:choose>
			 			</span>
					</a>
				</div>
		
			</form:form>
		</lams:Page>
		
		
		<script type="text/javascript">
			window.onload = function() {
				document.getElementById("focused").focus();
			}
		</script>
	</body>
</lams:html>