<%@ include file="/common/taglibs.jsp"%>

<form action="openNotebook.do" method="post">
	<input type="hidden" name="chatUserUID" value="${chatUserDTO.uid}" />

	<c:if
		test="${chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}">
		<div class="space-top">
			<h3><fmt:message key="button.reflect" /> </h3>
			<p>
				<strong><lams:out value="${chatDTO.reflectInstructions}" escapeHtml="true"/></strong>
			</p>

			<p>
				<c:choose>
					<c:when test="${not empty chatUserDTO.notebookEntry}">
						<lams:out escapeHtml="true" value="${chatUserDTO.notebookEntry}" />
					</c:when>

					<c:otherwise>
						<em><fmt:message key="message.no.reflection.available" /> </em>
					</c:otherwise>
				</c:choose>
			</p>

			<input type="submit" class="button" value="<fmt:message key="button.edit" />">
		</div>
	</c:if>
</form>

<lams:JSImport src="learning/includes/javascript/gate-check.js" />
<script type="text/javascript">
	checkNextGateActivity('finishButton', '<c:out value="${param.toolSessionID}" />', '', function(){
		 submitForm('finishActivity');
	});
	
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
    function submitForm(metodName){
        var f = document.getElementById("learningForm");
        f.submit();
    }
</script>

<c:choose>
		<c:when test="${!chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}">
	
			<form:form action="openNotebook.do" method="post"
			onsubmit="disableFinishButton();"  modelAttribute="learningForm" id="learningForm">
			<form:hidden path="chatUserUID" value="${chatUserDTO.uid}" />
			<input type="submit" value="<fmt:message key="button.continue" />" class="btn btn-responsive btn-primary float-end mt-2"/>
			</form:form>
		
		</c:when>
		<c:otherwise>
		
			<form:form action="finishActivity.do" method="post"
			onsubmit="disableFinishButton();"  modelAttribute="learningForm" id="learningForm">
			<form:hidden path="chatUserUID" value="${chatUserDTO.uid}" />
			<a href="#nogo" type="button" class="btn btn-primary float-end mt-2 na btn-autoresize" id="finishButton">
						 <span class="nextActivity">
							 <c:choose>
							 	<c:when test="${isLastActivity}">
							 		 <fmt:message key="button.submit" />
							 	</c:when>
							 	<c:otherwise>
							 		 <fmt:message key="button.finish" />
							 	</c:otherwise>
							 </c:choose>
						 </span>
					</a>
			</form:form>
		</c:otherwise>
</c:choose>


