<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
<!--	
	function validateForm() {
	
		// Validates that there's input from the user. 
		
		// disables the Finish button to avoid double submittion 
		disableFinishButton();

 	<c:if test='${mode == "learner"}'>
		// if this is learner mode, then we add this validation see (LDEV-1319)
		
		if (document.learningForm.entryText.value == "") {
			
			// if the input is blank, then we further inquire to make sure it is correct
			if (confirm("\n<fmt:message>message.learner.blank.input</fmt:message>"))  {
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
		
	</c:if>
	
	}

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
-->
</script>

<div id="content">
	<h1>
		${notebookDTO.title}
	</h1>

	
	<html:form action="/learning" method="post" onsubmit="return validateForm();">
		<html:hidden property="dispatch" value="finishActivity" />
		<html:hidden property="toolSessionID" />

		<p>
			${notebookDTO.instructions}
		</p>

		<c:set var="lrnForm"
			value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

		<c:choose>
			<c:when test="${contentEditable}">
				<c:choose>
					<c:when test="${notebookDTO.allowRichEditor}">
						<lams:FCKEditor id="entryText" value="${lrnForm.entryText}"
							toolbarSet="Default-Learner">
						</lams:FCKEditor>
					</c:when>

					<c:otherwise>
						<html:textarea cols="60" rows="8" property="entryText"
							styleClass="text-area"></html:textarea>
					</c:otherwise>
				</c:choose>

				<div class="space-bottom-top align-right">
					<html:submit styleClass="button" styleId="finishButton">
						<fmt:message>button.finish</fmt:message>
					</html:submit>
				</div>

			</c:when>

			<c:otherwise>
					<lams:out value="${lrnForm.entryText}" />
				</c:otherwise>
		</c:choose>

	</html:form>
</div>
