<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

	<lams:html>
		<c:set var="lams"> <lams:LAMSURL /> </c:set>
		<c:set var="tool"> 	<lams:WebAppURL />	</c:set>
	
	<lams:head>  
		<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:css/>
	
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		
		<link rel="stylesheet" type="text/css" href="${lams}css/jquery.minicolors.css"></link>
		<link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link>
		<link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link>
		
		<script src="${lams}includes/javascript/jquery.minicolors.min.js"></script>
		<script src="${lams}includes/javascript/fullscreen.js"></script>
		<script src="${tool}includes/javascript/jquery.timer.js"></script>
		<script src="${tool}includes/javascript/mapjs/main.js"></script>
		<script src="${tool}includes/javascript/mapjs/underscore-min.js"></script>
		
		<lams:JSImport src="learning/includes/javascript/gate-check.js" />
		<script type="text/javascript">
			checkNextGateActivity('finishButton', '${learningForm.toolSessionID}', '', submitForm);
		
			var mode = "${mode}";		// learner, teacher, ...
			
			function disableButtons() {
				$("#finishButton, #continueButton").attr("disabled", true);
				// show the waiting area during the upload
				$("#spinnerArea_Busy").show();
			}
		
			function enableButtons() {
				$("#spinnerArea_Busy").hide();
				$("#finishButton, #continueButton").removeAttr("disabled");
			}
		
			function submitForm() {
				var mindmapContent = document.getElementById("mindmapContent");
				mindmapContent.value = JSON.stringify(contentAggregate);
		 	 	var f = document.getElementById('learningForm');
		 		f.submit();
			}
			
			</script>
	</lams:head>

	<body class="stripes">
		
		<form:form action="${reflectOnActivity ? 'reflect.do' : 'finishActivity.do'}" method="post" onsubmit="return false;" modelAttribute="learningForm" id="learningForm">
			<input type="hidden" name="userId" value="${userIdParam}" />
			<input type="hidden" name="toolContentId" value="${toolContentIdParam}" />
			<form:hidden path="toolSessionID" />
			<form:hidden path="mindmapContent" id="mindmapContent" />
		
			<lams:Page type="learner" title="${mindmapDTO.title}" hideProgressBar="${isMonitor}" formID="learningForm">
			
				<%--Advanced settings and notices-----------------------------------%>
				
				<div class="panel">
					<c:out value="${mindmapDTO.instructions}" escapeXml="false"/>
				</div>
			
				<c:if test="${mindmapDTO.lockOnFinish and  mode != 'teacher' }">
					<lams:Alert type="danger" id="lock-on-finish" close="false">
						<c:choose>
							<c:when test="${finishedActivity}">
								<fmt:message key="message.activityLocked" />
							</c:when>
							<c:otherwise>
								<fmt:message key="message.warnLockOnFinish" />
							</c:otherwise>
						</c:choose>
					</lams:Alert>
				</c:if>
				
				 <c:if test="${not empty submissionDeadline && (mode == 'author' || mode == 'learner')}">
					 <lams:Alert id="submission-deadline" close="true" type="info">
					  	<fmt:message key="authoring.info.teacher.set.restriction" >
					  		<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
					  	</fmt:message>
					 </lams:Alert>
				 </c:if>
			
				&nbsp;
				
				<%--MindMup -----------------------------------%>
				<%@ include file="/common/mapjs.jsp"%>
		 		<%-- End MindMup -----------------------------------%>
		 		
				<div class="voffset10 pull-right">
					<c:choose>
						<c:when test="${isMonitor}">
							<button class="btn btn-primary" name="backButton" onclick="history.go(-1)">
								<fmt:message key="button.back"/>
							</button>
						</c:when>
					
						<c:otherwise>
							<c:choose>
								<c:when test="${reflectOnActivity}">
									<a href="javascript:submitForm();" class="btn btn-primary" id="continueButton">
										   <span class="nextActivity"><fmt:message key="button.continue"/></span>
									</a>
								</c:when>
				
								<c:otherwise>
									<button type="button" class="btn btn-primary" id="finishButton">
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
									</button>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</div>
		
			</lams:Page>
		</form:form>
		<div class="footer"></div>					
	</body>
</lams:html>