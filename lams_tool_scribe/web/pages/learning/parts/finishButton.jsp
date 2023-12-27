<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
<lams:JSImport src="learning/includes/javascript/gate-check.js" />
<script type="text/javascript">
	checkNextGateActivity('finishButton', '${scribeSessionDTO.sessionID}', '', submitForm);

	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
	
    function submitForm(){
           var f = document.getElementById('learningForm');
           f.submit();
    }
</script>

<div class="activity-bottom-buttons">
	<form:form action="/lams/tool/lascrb11/learning/finishActivity.do" modelAttribute="learningForm" method="post" onsubmit="disableFinishButton()" id="learningForm">
		<form:hidden path="scribeUserUID" value="${scribeUserDTO.uid}" />
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
	</form:form>
</div>
