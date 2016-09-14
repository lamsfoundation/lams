<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>

		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>

		<script language="JavaScript" type="text/JavaScript">
			
			function submitMethod(actionMethod) {
				document.McAuthoringForm.dispatch.value=actionMethod;
				$("textarea[name^=ca]").each(function() {
					var name = $(this).attr("name");
					var value = CKEDITOR.instances[name].getData();
					$(this).val(value);
				});
				
				$.ajaxSetup({ cache: true });
	    		$('#newQuestionForm').ajaxSubmit({
	    			cache: true,
    	    		target:  $('#candidateArea')
    		    });
			}
			
			function addItem() {
				document.McAuthoringForm.dispatch.value="saveSingleQuestion"; 
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
				
			function submitModifyAuthoringCandidate(questionIndexValue, candidateIndexValue, actionMethod) {
				document.McAuthoringForm.candidateIndex.value=candidateIndexValue; 
				document.McAuthoringForm.questionIndex.value=questionIndexValue; 
				submitMethod(actionMethod);
			}

			function removeCandidate(questionIndexValue, candidateIndexValue) {
				if (validateMinumumCandidateCount()) {
					document.McAuthoringForm.candidateIndex.value=candidateIndexValue; 
					document.McAuthoringForm.questionIndex.value=questionIndexValue; 
					submitMethod("removeCandidate");
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
					if (radioCorrect[i] != null) {
						if ((typeof(radioCorrect[i]) != 'undefined') && (typeof(radioCorrect[i]) != null)) {
							if (radioCorrect[i].checked) {
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

	<html:form action="/authoring?validate=false" styleId="newQuestionForm" enctype="multipart/form-data" method="POST">

		<html:hidden property="dispatch" value="saveSingleQuestion" />
		<html:hidden property="currentField"/>
		<html:hidden property="toolContentID" />
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="httpSessionID" />
		<html:hidden property="contentFolderID" />
		<html:hidden property="editableQuestionIndex" />
		<html:hidden property="editQuestionBoxRequest" value="true" />
		<html:hidden property="totalMarks" />

		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

		<c:set var="title"><fmt:message key="label.edit.question"/></c:set>
		<lams:Page title="${title}" type="learner">

			<div class="form-group">
				<lams:CKEditor id="newQuestion"
					value="${mcGeneralAuthoringDTO.editableQuestionText}"
					contentFolderID="${mcGeneralAuthoringDTO.contentFolderID}">
				</lams:CKEditor>
			</div>

			<%@ include file="/authoring/candidateAnswersList.jsp"%>

			<div class="form-group">
				<html:button property="newCandidate" onclick="javascript:submitMethod('newCandidateBox');" styleClass="btn btn-default btn-sm">
					<fmt:message key="label.add.candidates" />
				</html:button>
			</div>
			
			<div id="questions-worth" class="form-group">
				<fmt:message key="label.questions.worth"/>&nbsp;
				<select name="mark" class="control-sm">
					<c:forEach var="markEntry"
						items="${mcGeneralAuthoringDTO.marksMap}">
						<c:set var="SELECTED_MARK" value="" />
						<c:if test="${markEntry.value == mcGeneralAuthoringDTO.markValue}">
							<c:set var="SELECTED_MARK" value="SELECTED" />
						</c:if>
		
						<option value="<c:out value="${markEntry.value}"/>"	${SELECTED_MARK}>
							<c:out value="${markEntry.value}" />
						</option>
						</c:forEach>
				</select>
				&nbsp;<fmt:message key="label.marks"></fmt:message>
			</div>

			<div class="pull-right">
				<a href="#" onclick="javascript:self.parent.tb_remove();" onmousedown="self.focus();" class="btn btn-default btn-sm roffset5"> 
					<fmt:message key="label.cancel" />
				</a>
				<a href="#" onclick="addItem();"
					onmousedown="self.focus();" class="btn btn-default btn-sm"> 
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.add.new.question" />
				</a>
			</div>

		</lams:Page>

	</html:form>
		
</body>
</lams:html>
