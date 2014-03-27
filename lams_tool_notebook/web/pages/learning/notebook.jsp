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
         function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
        }
-->
</script>

<div id="content">
	<h1>
		<c:out value="${notebookDTO.title}" escapeXml="true"/>
	</h1>

	<p>
		<c:out value="${notebookDTO.instructions}" escapeXml="false"/>
	</p>

	<c:if test="${not empty notebookDTO.submissionDeadline}">
		 <div class="info">
		 	<fmt:message key="authoring.info.teacher.set.restriction" >
		 		<fmt:param><lams:Date value="${notebookDTO.submissionDeadline}" /></fmt:param>
		 	</fmt:message>
		 </div>
	</c:if>

	<c:if test="${notebookDTO.lockOnFinish and mode == 'learner'}">
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

	&nbsp;

	<html:form action="/learning" method="post"
		onsubmit="return validateForm();" styleId="messageForm">
		<html:hidden property="dispatch" value="finishActivity" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="contentEditable" value="${contentEditable}" />
		
		<c:set var="lrnForm"
			value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

		<c:choose>
			<c:when test="${contentEditable}">
				<c:choose>
					<c:when test="${notebookDTO.allowRichEditor}">
						<lams:CKEditor id="entryText" value="${lrnForm.entryText}" contentFolderID="${learnerContentFolder}"
							toolbarSet="DefaultLearner">
						</lams:CKEditor>
					</c:when>

					<c:otherwise>
						<html:textarea cols="60" rows="8" property="entryText"
							styleClass="text-area"></html:textarea>
					</c:otherwise>
				</c:choose>
			</c:when>

			<c:otherwise>
				<lams:out value="${lrnForm.entryText}" />
			</c:otherwise>
		</c:choose>
		
		<div class="space-bottom-top align-right">
			<html:link href="#nogo" styleClass="button" styleId="finishButton" onclick="submitForm('finish')">
				<span class="nextActivity">
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
		</div>
	</html:form>
</div>
