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
	
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="${tool}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${tool}includes/javascript/learning.js"></script>
	</lams:head>
	<body class="stripes" onload="init()">
		<script type="text/javascript">
			var mode = "${mode}";
			
			function openPixlr(url)	{
				alert("<fmt:message key="message.learner.saveWhenFinished" />");
				
				url += "&target=" + escape("${returnURL}");
				url += "&image=" + escape("${currentImageURL}");
				openPopup(url, 768, 1023);
			}
		
			function refresh() {
				window.location.href = "<lams:WebAppURL/>learning.do?mode=${mode}&toolSessionID=${toolSessionID}&redoQuestion=true";
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
						<a class="btn btn-secondary" href="javascript:openPixlr('${pixlrURL}');">
							<fmt:message key="learner.edit.image" />
						</a>
					</c:when>
				</c:choose>
			</div>
			
			<c:if test="${mode == 'learner' || mode == 'author'}">
				<%--Reflection--------------------------------------------------%>
		
				<c:if test="${pixlrUserDTO.finishedActivity and pixlrDTO.reflectOnActivity and pixlrUserDTO.finishedReflection}">
					<form:form action="openNotebook.do" method="post" id="learningForm" modelAttribute="learningForm">
						<form:hidden path="mode" value="${mode}" />	
						<form:hidden path="toolSessionID" id="toolSessionID"/>
						
						<div class="panel panel-default mt-2">
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
											
								<button	type="button" class="btn btn-secondary float-start" >
									<fmt:message key="button.edit" />
								</button>
							</div>
					
						</div>
							
					</form:form>
				</c:if>
				
				<%--Finish buttons--------------------------------------------------%>
				
				<form:form action="learning/finishActivity.do" method="post" onsubmit="disableFinishButton();" id="learningForm" modelAttribute="learningForm">
					<form:hidden path="toolSessionID" id="toolSessionID"/>
					<form:hidden path="mode" value="${mode}" />	
					<div class="activity-bottom-buttons">
						
						<c:choose>
							<c:when test="${pixlrDTO.allowViewOthersImages}">
								<button class="btn btn-primary" onclick="javascript:document.getElementById('learningForm').action = '<lams:WebAppURL />learning/viewAllImages.do';">
									<fmt:message key="button.viewAll" />
								</button>
							</c:when>
							
							<c:otherwise>
								<c:choose>
									<c:when test="${!pixlrUserDTO.finishedActivity and pixlrDTO.reflectOnActivity}">
										<button class="btn btn-primary" onclick="javascript:document.getElementById('learningForm').action = '<lams:WebAppURL />learning/openNotebook.do';">
											<fmt:message key="button.continue" />
										</button>
									</c:when>
									
									<c:otherwise>!!
										<a href="#nogo" class="btn btn-primary" id="finishButton" onclick="submitForm('finished');">
											<span class="na">
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
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</div>
				</form:form>
			</c:if>
		
		</lams:Page>
		<div class="footer">
		</div>					
	</body>
</lams:html>
<%@ include file="/common/taglibs.jsp"%>


