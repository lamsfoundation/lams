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

<div data-role="header" data-theme="b" data-nobackbtn="true">
	<h1>
		${notebookDTO.title}
	</h1>
</div>

<html:form action="/learning" method="post" onsubmit="return validateForm();" styleId="messageForm">
	<div data-role="content">
		<p>
			${notebookDTO.instructions}
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
	
		<html:hidden property="dispatch" value="finishActivity" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="contentEditable" value="${contentEditable}" />

		<c:set var="lrnForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

		<c:choose>
			<c:when test="${contentEditable}">
				<html:textarea cols="60" rows="8" property="entryText" styleClass="text-area"></html:textarea>
			</c:when>

			<c:otherwise>
				<lams:out value="${lrnForm.entryText}" />
			</c:otherwise>
		</c:choose>

	
	</div>

	<div data-role="footer" data-theme="b" class="ui-bar">
		<c:choose>
			<c:when test="${contentEditable}">
				<span class="ui-finishbtn-right">
					<a href="#nogo" id="finishButton" onclick="submitForm('finish')" data-role="button" data-icon="arrow-r" data-theme="b">
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
					</a>
				</span>
			</c:when>
	
			<c:otherwise>
				<h2>&nbsp;</h2>
			</c:otherwise>
		</c:choose>
	</div>
</html:form>
