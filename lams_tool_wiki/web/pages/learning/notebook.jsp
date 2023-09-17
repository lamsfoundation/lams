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
		<lams:JSImport src="includes/javascript/wikiCommon.js" relative="true" />
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<lams:JSImport src="learning/includes/javascript/gate-check.js" />
		
		<script type="text/javascript">
			checkNextGateActivity('finishButton', '${learningForm.toolSessionID}', '', submitForm);
			
			function disableFinishButton() {
				document.getElementById("finishButton").disabled = true;
			}
			function submitForm() {
				var f = document.getElementById('learningForm');
				f.submit();
			}
		</script>
	</lams:head>

	<body class="stripes">
		
		<lams:Page type="learner" title="${wikiDTO.title}">
			<form:form action="submitReflection.do" method="post" onsubmit="disableFinishButton();" id="learningForm" modelAttribute="learningForm">
				<form:hidden path="toolSessionID" id="toolSessionID" />
				<form:hidden path="mode" value="${mode}" />
		
				<div class="panel">
					<lams:out value="${wikiDTO.reflectInstructions}" escapeHtml="true" />
				</div>
				<div class="mb-3">
					<textarea id="focused" rows="4" name="entryText" class="form-control">${learningForm.entryText}</textarea>
		
					<div class="activity-bottom-buttons">
						<a href="#nogo" class="btn btn-primary na" id="finishButton">
							<c:choose>
								<c:when test="${isLastActivity}">
									<fmt:message key="button.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.finish" />
								</c:otherwise>
							</c:choose>
						</a>
					</div>
				</div>
			</form:form>
		</lams:Page>
		
		<script type="text/javascript">
			window.onload = function() {
				document.getElementById("focused").focus();
			}
		</script>

		<div class="footer">
		</div>					
	</body>
</lams:html>