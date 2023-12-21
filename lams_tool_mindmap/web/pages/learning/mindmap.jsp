<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>

<lams:PageLearner title="${mindmapDTO.title}" toolSessionID="${learningForm.toolSessionID}" >
	<link rel="stylesheet" type="text/css" href="${lams}css/jquery.minicolors.css"></link>
	<link rel="stylesheet" type="text/css" href="${tool}includes/css/mapjs.css"></link>
	<link rel="stylesheet" type="text/css" href="${tool}includes/css/mindmap.css"></link>
	
	<lams:JSImport src="includes/javascript/common.js" />
	<script src="${lams}includes/javascript/jquery.minicolors.min.js"></script>
	<script src="${lams}includes/javascript/fullscreen.js"></script>
	<script src="${tool}includes/javascript/jquery.timer.js"></script>
	<script src="${tool}includes/javascript/mapjs/main.js"></script>
	<script src="${tool}includes/javascript/mapjs/underscore-min.js"></script>
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

		$(document).ready(function() {
			$('[data-bs-toggle="tooltip"]').each((i, el) => {
				new bootstrap.Tooltip($(el))
			});
		});
	</script>	
	<%@ include file="websocket.jsp"%>
		
	<div id="container-main">
		<form:form action="${reflectOnActivity ? 'reflect.do' : 'finishActivity.do'}" method="post" onsubmit="return false;" modelAttribute="learningForm" id="learningForm">
			<input type="hidden" name="userId" value="${userIdParam}" />
			<input type="hidden" name="userUid" value="${userUid}" />
			<input type="hidden" name="toolContentId" value="${toolContentID}" />
			<form:hidden path="toolSessionID" />
			<form:hidden path="mindmapContent" id="mindmapContent" />
			
			<%--Advanced settings and notices-----------------------------------%>
			
			<c:if test="${mindmapDTO.lockOnFinish and  mode != 'teacher' }">
				<lams:Alert5 type="danger" id="lock-on-finish" close="false">
					<c:choose>
						<c:when test="${finishedActivity}">
							<fmt:message key="message.activityLocked" />
						</c:when>
						<c:otherwise>
							<fmt:message key="message.warnLockOnFinish" />
						</c:otherwise>
					</c:choose>
				</lams:Alert5>
			</c:if>
				
			 <c:if test="${not empty submissionDeadline && (mode == 'author' || mode == 'learner')}">
				 <lams:Alert5 id="submission-deadline" close="true" type="info">
				  	<fmt:message key="authoring.info.teacher.set.restriction" >
				  		<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
				  	</fmt:message>
				 </lams:Alert5>
			 </c:if>
				
			<div id="instructions" class="instructions">
				<c:out value="${mindmapDTO.instructions}" escapeXml="false"/>
			</div>
				
			<%--MindMup -----------------------------------%>
			<%@ include file="/common/mapjs.jsp"%>
		 		
			<div class="activity-bottom-buttons">
					<c:choose>
						<c:when test="${isMonitor}">
							<button type="button" class="btn btn-primary" name="backButton" onclick="history.go(-1)">
								<fmt:message key="button.back"/>
							</button>
						</c:when>
					
						<c:otherwise>
							<c:choose>
								<c:when test="${mindmapDTO.galleryWalkEnabled}">
									<button type="button" data-bs-toggle="tooltip" class="btn btn-primary na ${mode == 'author' ? '' : 'disabled'}"
										<c:choose>
											<c:when test="${mode == 'author'}">
												title="<fmt:message key='label.gallery.walk.wait.start.preview' />"
												onClick="javascript:location.href = location.href + '&galleryWalk=forceStart'"
											</c:when>
											<c:otherwise>
												title="<fmt:message key='label.gallery.walk.wait.start' />"
											</c:otherwise>
										</c:choose>
									>
										<fmt:message key="button.continue" />
									</button>
								</c:when>
								
								<c:when test="${reflectOnActivity}">
									<button type="button" onclick="javascript:submitForm();" class="btn btn-primary na" id="continueButton">
										   <fmt:message key="button.continue"/>
									</button>
								</c:when>
				
								<c:otherwise>
									<button type="button" class="btn btn-primary na" id="finishButton">
										<c:choose>
							 				<c:when test="${isLastActivity}">
							 					<fmt:message key="button.submit" />
							 				</c:when>
							 				<c:otherwise>
							 					<fmt:message key="button.finish" />
							 				</c:otherwise>
							 			</c:choose>
									</button>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
			</div>
		</form:form>				
	</div>
</lams:PageLearner>