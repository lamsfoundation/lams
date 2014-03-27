<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
<!--
	var mode = "${mode}";

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}

	function validateForm() {
	
		// Validates that there's input from the user. 
		
		// disables the Finish button to avoid double submittion 
		disableFinishButton();

 		if (mode == "learner") {
			// if this is learner mode, then we add this validation see (LDEV-1319)
		
			if (document.learningForm.entryText.value == "") {
				
				// if the input is blank, then we further inquire to make sure it is correct
				if (confirm("<fmt:message>message.learner.blank.input</fmt:message>"))  {
					// if correct, submit form
					return true;
				} else {
					// otherwise, focus on the text area
					document.learningForm.entryText.focus();
					document.getElementById("finishButton").disabled = false;
					return false;      
				}
			} else {
				// there was something on the form, so submit the form
				return true;
			}
		}
	}

-->
</script>

<div id="content">
	<h1>
		<c:out value="${contentDTO.title}" escapeXml="true"/>
	</h1>

	<p>
		<c:out value="${contentDTO.instructions}" escapeXml="false"/>
	</p>

	<c:if test="${contentDTO.lockOnFinish and mode == 'learner'}">
		<div class="info">
			<c:choose>
				<c:when test="${finishedActivity}">
					<fmt:message key="message.activityLocked" />
				</c:when>
				<c:otherwise>
					<fmt:message key="message.warnLockOnFinish" />
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>

	<p>
		<c:choose>
			<c:when test="${meetingOpen}">
				<html:link href="${meetingURL}" target="bbbLearner${toolSessionID}" onclick="window.open('${meetingURL}', 'bbbLearner${toolSessionID}', 'resizable=yes,scrollbars=yes')">
					<fmt:message key="label.learning.joinConference" />
				</html:link>
			</c:when>
			<c:otherwise>
				<p>
					<fmt:message key="label.learning.conferenceNotAvailable" />
				</p>
				<html:link href="#" onclick="window.location.reload()">
					<fmt:message key="label.refresh" />
				</html:link>
			</c:otherwise>
		</c:choose>
	</p>

	<c:if test="${mode == 'learner' || mode == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>

</div>
