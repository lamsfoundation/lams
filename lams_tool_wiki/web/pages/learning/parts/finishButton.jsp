<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
	
	function continueOrFinish(dispatch) {
		document.getElementById("dispatch2").value = dispatch;
		document.getElementById("learningButtonForm").submit();
	}
</script>

Reflect: ${wikiDTO.reflectOnActivity} <br />
User:${userDTO.finishedActivity} <br />	
<c:if test="${userDTO.finishedActivity and wikiDTO.reflectOnActivity}">
<html:form action="/learning" method="get" styleId="reflectEditForm">
	<html:hidden property="dispatch" value="openNotebook" />
	<html:hidden property="mode" value="${mode}" />	
	<html:hidden property="toolSessionID" styleId="toolSessionID"/>
		<div class="space-top">
			<h2>
				${wikiDTO.reflectInstructions}
			</h2>

			<p>
				<c:choose>
					<c:when test="${not empty userDTO.notebookEntry}">
						<lams:out escapeHtml="true" value="${userDTO.notebookEntry}" />
					</c:when>

					<c:otherwise>
						<em><fmt:message key="message.no.reflection.available" /> </em>
					</c:otherwise>
				</c:choose>
			</p>

			<html:submit styleClass="button">
				<fmt:message key="button.edit" />
			</html:submit>			
		</div>
	
</html:form>
</c:if>

${!userDTO.finishedActivity and wikiDTO.reflectOnActivity}

<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="learningButtonForm">
	<html:hidden property="dispatch" styleId = "dispatch2" value="finishActivity" />
	<html:hidden property="toolSessionID" styleId="toolSessionID"/>
	<html:hidden property="mode" value="${mode}" />	
	<div class="space-bottom-top align-right" id="finishButtonDiv">
		<c:choose>
			<c:when test="${!userDTO.finishedActivity and wikiDTO.reflectOnActivity}">
				
				<a href="javascript:continueOrFinish('openNotebook');" class="button"><fmt:message key="button.continue" /></a>
				<!--  
				<html:submit styleClass="button" onclick="javascript:document.getElementById('dispatch').value = 'openNotebook';">
					<fmt:message key="button.continue" />
				</html:submit>
				-->
			</c:when>
			<c:otherwise>
				<a href="javascript:continueOrFinish('finishActivity');" class="button"><fmt:message key="button.finish" /></a>
				
				<!-- 
				<html:submit styleClass="button" styleId="finishButton" onclick="javascript:document.getElementById('dispatch').value = 'finishActivity';">
					<fmt:message key="button.finish" />
				</html:submit>
				-->
			</c:otherwise>
		</c:choose>
	</div>
</html:form>







