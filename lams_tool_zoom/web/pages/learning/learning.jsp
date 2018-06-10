<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
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
				if (confirm("<fmt:message>message.learner.blank.input</fmt:message>")) {
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

</script>

<lams:Page type="learner" title="${contentDTO.title}">
	<div class="panel">
		<c:out value="${contentDTO.instructions}" escapeXml="false" />
	</div>

	<div class="buttons">
			<c:choose>
				<c:when test="${not empty meetingURL}">
					<iframe style="width: 100%; height: 500px; border: none;" src="${meetingURL}"></iframe>
				</c:when>
				<c:otherwise>
					<p>
						<fmt:message key="label.learning.conferenceNotAvailable" />
					</p>
					<html:link styleClass="btn btn-sm btn-default" href="#" onclick="window.location.reload()">
						<fmt:message key="label.refresh" />
					</html:link>
				</c:otherwise>
			</c:choose>

	</div>

	<hr class="msg-hr">

	<c:if test="${mode == 'learner' || mode == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>

</lams:Page>
