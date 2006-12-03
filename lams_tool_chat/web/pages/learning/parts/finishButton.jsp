<%@ include file="/common/taglibs.jsp"%>

<html:form action="/learning" method="post">
	<html:hidden property="chatUserUID" value="${chatUserDTO.uid}" />
	<html:hidden property="dispatch" value="openNotebook" />
	
	<c:if
		test="${chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}">
		<div class="space-top">
			<h2>
				${chatDTO.reflectInstructions}
			</h2>
			
			<p>
				<c:choose>
					<c:when test="${not empty chatUserDTO.notebookEntry}">
						<lams:out value="${chatUserDTO.notebookEntry}" escapeXml="true" />
					</c:when>
					
					<c:otherwise>
						<em><fmt:message key="message.no.reflection.available" /></em>  
					</c:otherwise>
				</c:choose>		
			</p>
			
			<html:submit styleClass="button">
				<fmt:message key="button.edit" />
			</html:submit>
		</div>
	</c:if>
</html:form>

<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = "disabled";
		}
	}
</script>

<html:form action="/learning" method="post" onsubmit="disableFinishButton();">
	<html:hidden property="chatUserUID" value="${chatUserDTO.uid}" />
	<div class="space-bottom-top align-right">
		<c:choose>
			<c:when
				test="${!chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}">
				<html:hidden property="dispatch" value="openNotebook" />

				<html:submit styleClass="button">
					<fmt:message key="button.continue" />
				</html:submit>
			</c:when>
			<c:otherwise>
				<html:hidden property="dispatch" value="finishActivity" />
				<html:submit styleClass="button" styleId="finishButton">
					<fmt:message key="button.finish" />
				</html:submit>
			</c:otherwise>
		</c:choose>
	</div>
</html:form>
