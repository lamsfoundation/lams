<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<link href="<html:rewrite page='/includes/css/addItem.css'/>" rel="stylesheet" type="text/css">
		
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>

		<script language="JavaScript" type="text/JavaScript">
		
			function submitMethod(actionMethod) {
				document.McAuthoringForm.dispatch.value=actionMethod;
				$("textarea[name^=ca]").each(function() {
					var name = $(this).attr("name");
					var value = CKEDITOR.instances[name].getData();
					$(this).val(value);
				});
				
	    		$('#newQuestionForm').ajaxSubmit({
    	    		target:  $('#candidateArea')
    		    });
			}
			
			function addItem() {
				document.McAuthoringForm.dispatch.value="addSingleQuestion";
				$("#newQuestion").val(CKEDITOR.instances.newQuestion.getData());
				$("textarea[name^=ca]").each(function() {
					var name = $(this).attr("name");
					var value = CKEDITOR.instances[name].getData();
					$(this).val(value);
				});
				
				if (validateSingleCorrectAnswer()) { 
					$('#newQuestionForm').ajaxSubmit({ 
	    	    		target:  parent.jQuery('#resourceListArea'), 
	    	    		success: function() { 
	    	    			self.parent.refreshThickbox();
	    	    			self.parent.tb_remove();
	    	    	    }
	    		    });
				}
			}
			
			function submitModifyAddedCandidate(candidateIndexValue, actionMethod) {
				document.McAuthoringForm.candidateIndex.value=candidateIndexValue; 
				submitMethod(actionMethod);
			}

			function removeAddedCandidate(candidateIndexValue) {
				if (validateMinumumCandidateCount()) { 
					document.McAuthoringForm.candidateIndex.value=candidateIndexValue; 
					submitMethod("removeAddedCandidate");
				}
			}
			
			function validateSingleCorrectAnswer() {
				
				//question.blank
				if (!$("#newQuestion").val()) {
					var msg = "<fmt:message key="question.blank"/>";
					alert(msg);
					return false;
				}
				
				var singleCorrectEntry = 0;
				var radioCorrect=document.McAuthoringForm.correct;

				if ((radioCorrect == 'null') || (radioCorrect == 'undefined')) {
					var msg = "<fmt:message key="candidates.groupSize.warning"/>";
					alert(msg);
					return false;				
				}

				var radioGroupSize=radioCorrect.length;
				if ((radioGroupSize == 'undefined') || (radioGroupSize < 2)) {
					var msg = "<fmt:message key="candidates.groupSize.warning"/>";
					alert(msg);
					return false;
				}
				
				for(i = 0; i < 51; i++) {
					if (radioCorrect[i] != null)
					{
						if ((typeof(radioCorrect[i]) != 'undefined') && (typeof(radioCorrect[i]) != null))
						{
							if (radioCorrect[i].checked) 
							{
								singleCorrectEntry =1;
							}
						}
					}
				}
				
				if (singleCorrectEntry == 0) {
					var msg = "<fmt:message key="candidates.none.correct"/>";
					var msgSetFirst = "<fmt:message key="candidates.setFirst"/>";
					alert(msg);

					if ((radioCorrect[0] != 'undefined') && (radioCorrect[0] != null))
					{
						radioCorrect[0].checked=true;					
						alert(msgSetFirst);
					}

					return false;				
				}
				
				//verify selected radiobutton should not be blank
				var selectedAnswerIndex = $('input[name=correct]:checked').val();
				if (!$("textarea[name=ca" + selectedAnswerIndex + "]").val()) {
					var msg = "<fmt:message key="error.correct.answer.blank"/>";
					alert(msg);
					return false;
				}
				
				return true;
			}

			function validateMinumumCandidateCount() {
				var radioCorrect=document.McAuthoringForm.correct;

				if ((radioCorrect == 'undefined') || (radioCorrect == null))
				{
					var msg = "<fmt:message key="candidates.unremovable.groupSize"/>";
					alert(msg);
					return false;				
				}

				var radioGroupSize=radioCorrect.length;
				
				if ((radioGroupSize == 'undefined') || (radioGroupSize <= 2))				
				{
					var msg = "<fmt:message key="candidates.unremovable.groupSize"/>";
					alert(msg);
					return false;				
				}
				
				return true;
			}
			
		</script>
	</lams:head>

	<body class="stripes" onload="parent.resizeIframe();">
		<div id="content" >
		<html:form action="/authoring?validate=false" styleId="newQuestionForm" enctype="multipart/form-data" method="POST">
			<html:hidden property="dispatch" value="addSingleQuestion" />
			<html:hidden property="currentField" />			
			<html:hidden property="toolContentID" />
			<html:hidden property="currentTab" styleId="currentTab" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="contentFolderID" />
			<html:hidden property="editQuestionBoxRequest" value="false" />
			<html:hidden property="totalMarks" />

			<div class="field-name space-top">
				<fmt:message key="label.add.new.question"></fmt:message>
			</div>
			<lams:CKEditor id="newQuestion"
				value="${mcGeneralAuthoringDTO.editableQuestionText}"
				contentFolderID="${mcGeneralAuthoringDTO.contentFolderID}"
				width="100%">
			</lams:CKEditor>

			<%@ include file="/authoring/candidateAnswersAddList.jsp"%>

			<div class="space-bottom small-space-top">
				<div class="right-buttons">
					<html:button property="newAddedCandidate"
						onclick="javascript:submitMethod('newAddedCandidateBox');"
						styleClass="button">
						<fmt:message key="label.add.candidates" />
					</html:button>
				</div>
				<div id="questions-worth">
					<fmt:message key="label.questions.worth"></fmt:message>
					<select name="mark">
						<c:forEach var="markEntry" items="${mcGeneralAuthoringDTO.marksMap}">
							<c:set var="SELECTED_MARK" scope="request" value="" />
							<c:if test="${markEntry.value == mcGeneralAuthoringDTO.markValue}">
								<c:set var="SELECTED_MARK" scope="request" value="SELECTED" />
							</c:if>
	
							<option value="<c:out value="${markEntry.value}"/>" <c:out value="${SELECTED_MARK}"/>>
								<c:out value="${markEntry.value}" />
							</option>
						</c:forEach>
					</select>
					<fmt:message key="label.marks"></fmt:message>
				</div>
			</div>

			<div class="field-name">
				<fmt:message key="label.feedback"></fmt:message>
			</div>
			<html:textarea property="feedback" rows="3" cols="70"></html:textarea>
			
			<lams:ImgButtonWrapper>
					<a href="#" onclick="addItem();" onmousedown="self.focus();" class="button-add-item"> 
						<fmt:message key="label.add.new.question" />
					</a>
	
					<a href="#" onclick="javascript:self.parent.tb_remove();" onmousedown="self.focus();" class="button space-left"> 
						<fmt:message key="label.cancel" />
					</a>
			</lams:ImgButtonWrapper>

		</html:form>
		</div>
	</body>
</lams:html>
