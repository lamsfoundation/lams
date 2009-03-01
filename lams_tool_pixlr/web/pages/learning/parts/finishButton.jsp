<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
         function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
        }
</script>

<c:if test="${pixlrUserDTO.finishedActivity and pixlrDTO.reflectOnActivity and pixlrUserDTO.finishedReflection}">
<html:form action="/learning" method="post" styleId="reflectEditForm" styleId="messageForm">
	<html:hidden property="dispatch" value="openNotebook" />
	<html:hidden property="mode" value="${mode}" />	
	<html:hidden property="toolSessionID" styleId="toolSessionID"/>
		<div class="space-top">
			<h2>
				${pixlrDTO.reflectInstructions}
			</h2>

			<p>
				<c:choose>
					<c:when test="${not empty pixlrUserDTO.notebookEntry}">
						<lams:out escapeHtml="true" value="${pixlrUserDTO.notebookEntry}" />
					</c:when>

					<c:otherwise>
						<em><fmt:message key="message.no.reflection.available" /> </em>
					</c:otherwise>
				</c:choose>
			</p>

			<html:submit styleClass="button" >
				<fmt:message key="button.edit" />
			</html:submit>
		</div>
</html:form>
</c:if>

<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="learningForm">
	<html:hidden property="dispatch" styleId = "dispatch" value="finishActivity" />
	<html:hidden property="toolSessionID" styleId="toolSessionID"/>
	<html:hidden property="mode" value="${mode}" />	
	<div class="space-bottom-top align-right">
		
		<c:choose>
			<c:when test="${pixlrDTO.allowViewOthersImages}">
				<html:submit styleClass="button" onclick="javascript:document.getElementById('dispatch').value = 'viewAllImages';">
					<fmt:message key="button.viewAll" />
				</html:submit>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${!pixlrUserDTO.finishedActivity and pixlrDTO.reflectOnActivity}">
						<html:submit styleClass="button" onclick="javascript:document.getElementById('dispatch').value = 'openNotebook';">
							<fmt:message key="button.continue" />
						</html:submit>
					</c:when>
					<c:otherwise>
						<html:hidden property="dispatch" value="finishActivity" />
						<html:link href="javascript:;" styleClass="button" styleId="finishButton" onclick="javascript:document.getElementById('dispatch').value = 'finishActivity'; ;">
							<span class="nextActivity"><fmt:message key="button.finish" /></span>
						</html:link>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</div>
</html:form>







