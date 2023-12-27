<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:PageLearner title="${contentDTO.title}" toolSessionID="${learningForm.toolSessionID}">
	<lams:JSImport src="includes/javascript/common.js" />
	<lams:JSImport src="includes/javascript/getSysInfo.js" />
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${learningForm.toolSessionID}', '', function(){
			$('#finishForm').submit();
		});
			
		function disableFinishButton() {
			var finishButton = document.getElementById("finishButton");
			if (finishButton != null) {
				finishButton.disabled = true;
			}
		}
	</script>

	<div id="container-main">
		<lams:errors5/>
		
		<div id="instructions" class="instructions">
			<c:out value="${contentDTO.instructions}" escapeXml="false" />
		</div>
		
		<c:if test="${not skipContent}">
			<c:choose>
				<c:when test="${empty meetingURL}">
					<script type="text/javascript">
						// refresh in 20 seconds
						window.setTimeout(function() {
							window.location.reload();
						}, 20*1000);
					</script>
					
					<lams:Alert5 type="warning">
						<fmt:message key="label.learning.conferenceNotAvailable" />
					</lams:Alert5>
				</c:when>
				
				<c:otherwise>
					<div class="card lcard">
						<div class="card-body">
							<c:if test="${not empty meetingPassword}">
								<div class="fs-4">
									<fmt:message key="label.meeting.password" />&nbsp;<code>${meetingPassword}</code>
								</div>
							</c:if>
							
							<iframe id="zoomJoinFrame" style="width: 100%; height: 680px; border: none; display: none" src="${meetingURL}"></iframe>
							
							<div class="clearfix">
								<a id="zoomJoinButton" href="${meetingURL}" target="_blank" style="display: none" class="btn btn-secondary float-end">
									<i class="fa-solid fa-video me-1"></i>
									<fmt:message key="button.enter" />
								</a>
							</div>
							
							<script type="text/javascript">
								if (isMac) {
									$('#zoomJoinButton').show();
								} else {
									$('#zoomJoinFrame').show();
								}
							</script>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</c:if>
	
		<c:if test="${mode == 'learner' || mode == 'author'}">
			<form:form action="finishActivity.do" method="post" onsubmit="disableFinishButton();" modelAttribute="learningForm" id="finishForm">
				<form:hidden path="toolSessionID" />
				<div class="activity-bottom-buttons">
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

					<c:if test="${empty meetingURL}">
						<button type="button" onclick="window.location.reload()" class="btn btn-secondary btn-icon-refresh me-2">
							<fmt:message key="label.refresh" />
						</button>
					</c:if>
				</div>
			</form:form>
		</c:if>
	</div>
</lams:PageLearner>
