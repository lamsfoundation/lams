<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	var mode = "${mode}";
	var forceResponse = "${notebookDTO.forceResponse}";

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

	function submitForm(methodName) {
		if (forceResponse =="true" && document.learningForm.focusedInput.value == "") {
			if (confirm("<fmt:message>message.learner.blank.alertforceResponse</fmt:message>")) {
				return true;
			} else {
				// otherwise, focus on the text area
				document.learningForm.entryText.focus();
				document.getElementById("finishButton").disabled = false;
				return false;
			}
		} 
		var f = document.getElementById('messageForm');
		f.submit();
	}
</script>

<html:form action="/learning" method="post" onsubmit="return validateForm();" styleId="messageForm">
	<html:hidden property="dispatch" value="finishActivity" />
	<html:hidden property="toolSessionID" />
	<html:hidden property="contentEditable" value="${contentEditable}" />
	<c:set var="lrnForm" value="<%=request
						.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />


	<lams:Page type="learner" title="${notebookDTO.title}">
		<div class="panel">
			<c:out value="${notebookDTO.instructions}" escapeXml="false" />
		</div>

		<!-- Notifications and warnings -->
		<c:if test="${not empty notebookDTO.submissionDeadline}">
			<lams:Alert id="submissionDeadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${notebookDTO.submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
		</c:if>

		<c:if test="${notebookDTO.lockOnFinish and mode == 'learner'}">
			<lams:Alert id="activityLocked" type="info" close="true">
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
		<!-- End Notifications and warnings -->

		<!-- Form -->
		<div class="form-group">
			<c:choose>
				<c:when test="${contentEditable}">
					<c:choose>
						<c:when test="${notebookDTO.allowRichEditor}">
							<lams:CKEditor id="entryText" value="${lrnForm.entryText}" contentFolderID="${learnerContentFolder}"
								toolbarSet="DefaultLearner">
							</lams:CKEditor>
						</c:when>

						<c:otherwise>
							<html:textarea rows="8" property="entryText" styleClass="form-control" styleId="focusedInput" />
						</c:otherwise>
					</c:choose>
				</c:when>

				<c:otherwise>
					<div class="sbox sbox-body voffset10 bg-warning">
						<lams:out value="${lrnForm.entryText}" />
					</div>
				</c:otherwise>
			</c:choose>

			<c:if test="${not empty teachersComment}">
				<div class="panel panel-default voffset5 roffset10">
					<div class="panel-heading">
						<fmt:message key="label.reply.comment" />
					</div>
					<div class="panel-body">
						<lams:out value="${teachersComment}" escapeHtml="true" />
					</div>
				</div>
			</c:if>

			<c:if test="${mode != 'teacher'}">
				<div class="right-buttons voffset5">
					<html:link href="#nogo" styleClass="btn btn-primary pull-right na" styleId="finishButton"
						onclick="submitForm('finish')">
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>
					</html:link>
				</div>
			</c:if>
		</div>
	</lams:Page>
</html:form>
<!-- end form -->





<script type="text/javascript">
	window.onload = function() {
		document.getElementById("focusedInput").focus();
	}
</script>