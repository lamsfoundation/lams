<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function saveAndFinish() {
		 serialiseMarkers();
		 document.learningForm.submit();
		 return false

	}
</script>


<c:if test="${gmapUserDTO.finishedActivity and gmapDTO.reflectOnActivity}">
<html:form action="/learning" method="post" styleId="reflectEditForm">
	<html:hidden property="dispatch" value="openNotebook" />
	<html:hidden property="mode" value="${mode}" />	
	<html:hidden property="toolSessionID" styleId="toolSessionID"/>
	<html:hidden property="markersXML" value="" />
	<div class="row no-gutter">
		<div class="col-xs-12">
			<div class="panel panel-default">
				<div class="panel-heading panel-title">
					<fmt:message key="heading.reflection" />
				</div>
				<div class="panel-body">
					<div class="reflectionInstructions">
						<lams:out value="${gmapDTO.reflectInstructions}" escapeHtml="true"/>
					</div>
					<div class="panel">
						<c:choose>
							<c:when test="${not empty gmapUserDTO.notebookEntry}">
								<lams:out escapeHtml="true" value="${gmapUserDTO.notebookEntry}" />
							</c:when>
							<c:otherwise>
								<em><fmt:message key="message.no.reflection.available" /> </em>
							</c:otherwise>
						</c:choose>
					</div>
					<html:submit styleClass="btn btn-default pull-left" onclick="javascript:return confirmLeavePage();">
						<fmt:message key="button.edit" />
					</html:submit>
				</div>
			</div>
		</div>
	</div>	
</html:form>
</c:if>

<html:form action="/learning" method="post" styleId="learningForm">
	<html:hidden property="dispatch" styleId = "dispatch" value="finishActivity" />
	<html:hidden property="toolSessionID" styleId="toolSessionID"/>
	<html:hidden property="markersXML" value="" styleId="markersXML" />
	<html:hidden property="mode" value="${mode}" />	
	<div class="space-bottom-top align-right">
		<c:choose>
			<c:when test="${!gmapUserDTO.finishedActivity and gmapDTO.reflectOnActivity}">
				<html:submit styleClass="btn btn-primary voffset10 pull-right" styleId="continueButton" onclick="javascript:document.getElementById('dispatch').value = 'openNotebook'; return serialiseMarkers();">
					<fmt:message key="button.continue" />
				</html:submit>
			</c:when>
			<c:otherwise>
				<html:hidden property="dispatch" value="finishActivity" />
				<html:link href="#nogo" styleClass="btn btn-primary voffset10 pull-right na" styleId="finishButton" onclick="javascript:saveAndFinish();">
					<c:choose>
	 					<c:when test="${activityPosition.last}">
	 						<fmt:message key="button.submit" />
	 					</c:when>
	 					<c:otherwise>
	 		 				<fmt:message key="button.finish" />
	 					</c:otherwise>
		 			</c:choose>
				</html:link>
			</c:otherwise>
		</c:choose>
	</div>
</html:form>
