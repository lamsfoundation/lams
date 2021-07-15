<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript" src="${lams}learning/includes/javascript/gate-check.js"></script>
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

<div class="voffset10">
	<form:form action="${(scribeUserDTO.finishedActivity == 'false' and scribeDTO.reflectOnActivity == 'true') ? '/lams/tool/lascrb11/learning/openNotebook.do' : '/lams/tool/lascrb11/learning/finishActivity.do'}" modelAttribute="learningForm" method="post" onsubmit="disableFinishButton()" id="learningForm">
		<form:hidden path="scribeUserUID" value="${scribeUserDTO.uid}" />
		<c:choose>
			<c:when
				test="${!scribeUserDTO.finishedActivity and scribeDTO.reflectOnActivity}">

				<button class="btn btn-primary pull-right">
					<fmt:message key="button.continue" />
				</button>

			</c:when>
			<c:otherwise>
				<a href="#nogo" class="btn btn-primary pull-right na" id="finishButton">
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
			</c:otherwise>
		</c:choose>
	</form:form>
</div>
