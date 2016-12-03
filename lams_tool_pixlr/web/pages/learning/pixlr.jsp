<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	var mode = "${mode}";
	
	function openPixlr(url)	{
		alert("<fmt:message key="message.learner.saveWhenFinished" />");
		
		url += "&target=" + escape("${returnURL}");
		url += "&image=" + escape("${currentImageURL}");
		openPopup(url, 648, 1152);
	}

	function refresh() {
		window.location.href = "<lams:WebAppURL/>/learning.do?mode=${mode}&toolSessionID=${toolSessionID}&redoQuestion=true";
	}

	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
	
	function submitForm(methodName){
		var f = document.getElementById('learningForm');
		f.submit();
	}
</script>

<lams:Page type="learner" title="${pixlrDTO.title}">

	<div class="panel">
		<c:out value="${pixlrDTO.instructions}" escapeXml="false"/>
	</div>

	<c:if test="${pixlrDTO.lockOnFinish and mode == 'learner'}">
		<lams:Alert type="danger" id="lock-on-finish" close="false">
			<c:choose>
				<c:when test="${finishedActivity}">
					<fmt:message key="message.activityLocked" />
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${pixlrDTO.allowViewOthersImages}">
							<fmt:message key="message.warnLockOnFinishViewAll" />
						</c:when>
						<c:otherwise>
							<fmt:message key="message.warnLockOnFinish" />
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</lams:Alert>
	</c:if>

	<div style="text-align:center;">
		<img id="image" border="1" title="<fmt:message key="tooltip.openfullsize" />" 
				onclick="openImage('${currentImageURL}')" src="${currentImageURL}" />
		
		<br />
		<br />
		<c:choose>
			<c:when test="${contentEditable}">
				<a class="btn btn-default" href="javascript:openPixlr('${pixlrURL}');">
					<fmt:message key="learner.edit.image" />
				</a>
			</c:when>
		</c:choose>
	</div>
	
	<c:if test="${mode == 'learner' || mode == 'author'}">
		<%--Reflection--------------------------------------------------%>

		<c:if test="${pixlrUserDTO.finishedActivity and pixlrDTO.reflectOnActivity and pixlrUserDTO.finishedReflection}">
			<html:form action="/learning" method="post" styleId="reflectEditForm">
				<html:hidden property="dispatch" value="openNotebook" />
				<html:hidden property="mode" value="${mode}" />	
				<html:hidden property="toolSessionID" styleId="toolSessionID"/>
				
				<div class="panel panel-default voffset10">
					<div class="panel-heading panel-title">
						${pixlrDTO.reflectInstructions}
					</div>
			
					<div class="panel-body">
						<c:choose>
							<c:when test="${not empty pixlrUserDTO.notebookEntry}">
								<lams:out escapeHtml="true" value="${pixlrUserDTO.notebookEntry}" />
							</c:when>
			
							<c:otherwise>
								<em><fmt:message key="message.no.reflection.available" /> </em>
							</c:otherwise>
						</c:choose>
									
						<html:submit styleClass="btn btn-default pull-left" >
							<fmt:message key="button.edit" />
						</html:submit>
					</div>
			
				</div>
					
			</html:form>
		</c:if>
		
		<%--Finish buttons--------------------------------------------------%>
		
		<html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="learningForm">
			<html:hidden property="dispatch" styleId = "dispatch" value="finishActivity" />
			<html:hidden property="toolSessionID" styleId="toolSessionID"/>
			<html:hidden property="mode" value="${mode}" />	
			<div class="voffset10 pull-right">
				
				<c:choose>
					<c:when test="${pixlrDTO.allowViewOthersImages}">
						<html:submit styleClass="btn btn-primary" onclick="javascript:document.getElementById('dispatch').value = 'viewAllImages';">
							<fmt:message key="button.viewAll" />
						</html:submit>
					</c:when>
					
					<c:otherwise>
						<c:choose>
							<c:when test="${!pixlrUserDTO.finishedActivity and pixlrDTO.reflectOnActivity}">
								<html:submit styleClass="btn btn-primary" onclick="javascript:document.getElementById('dispatch').value = 'openNotebook';">
									<fmt:message key="button.continue" />
								</html:submit>
							</c:when>
							
							<c:otherwise>
								<html:hidden property="dispatch" value="finishActivity" />
								<html:link href="#nogo" styleClass="btn btn-primary" styleId="finishButton" onclick="submitForm('finished');">
									<span class="na">
										<c:choose>
											<c:when test="${activityPosition.last}">
												<fmt:message key="button.submit" />
											</c:when>
											<c:otherwise>
												<fmt:message key="button.finish" />
											</c:otherwise>
										</c:choose>
									</span>
								</html:link>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
		</html:form>
	</c:if>

</lams:Page>
