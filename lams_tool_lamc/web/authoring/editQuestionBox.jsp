<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapId]}" />
<c:set var="questionDtos" value="${sessionMap.questionDtos}" />
<c:set var="questionDto" value="${sessionMap.questionDto}" />

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>

		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>

		<script type="text/JavaScript">
			
			function submitMethod(actionMethod) {
				$("textarea[name^=ca],textarea[name=feedback]").each(function() {
					var name = $(this).attr("name");
					var value = CKEDITOR.instances[name].getData();
					$(this).val(value);
				});
				
				$.ajaxSetup({ cache: true });
	    		$('#newQuestionForm').ajaxSubmit({
	    			data: { 
						sessionMapId: '${sessionMapId}',
						dispatch: actionMethod
					},
					cache: true,
    	    		target:  $('#candidateArea')
    		    });
			}
			
			function saveQuestion() {
				$("#newQuestion").val(CKEDITOR.instances.newQuestion.getData());
				$("textarea[name^=ca],textarea[name=feedback]").each(function() {
					var name = $(this).attr("name");
					var value = CKEDITOR.instances[name].getData();
					$(this).val(value);
				});
				
				if (validateSingleCorrectAnswer()) { 
					$('#newQuestionForm').ajaxSubmit({
						data: { 
							sessionMapId: '${sessionMapId}',
							dispatch: 'saveQuestion'
						},
	    	    		target:  parent.jQuery('#resourceListArea'), 
	    	    		success: function() { 
	    	    			self.parent.refreshThickbox();
	    	    			self.parent.tb_remove();
	    	    	    }
	    		    });
				}
			}
				
			function submitModifyCandidate(candidateIndexValue, actionMethod) {
				document.McAuthoringForm.candidateIndex.value=candidateIndexValue;
				submitMethod(actionMethod);
			}

			function removeCandidate(candidateIndexValue) {
				if (validateMinumumCandidateCount()) {
					document.McAuthoringForm.candidateIndex.value=candidateIndexValue;
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
					
					if ((radioCorrect[0] != 'undefined') && (radioCorrect[0] != null)) {
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
				if ((radioCorrect == 'undefined') || (radioCorrect == null)) {
					var msg = "<fmt:message key="candidates.unremovable.groupSize"/>";
					alert(msg);
					return false;				
				}
				
				var radioGroupSize=radioCorrect.length;
				if ((radioGroupSize == 'undefined') || (radioGroupSize <= 2)) {
					var msg = "<fmt:message key="candidates.unremovable.groupSize"/>";
					alert(msg);
					return false;				
				}
				
				return true;
			}

		</script>
	</lams:head>

<body class="stripes">

	<html:form action="/authoring?validate=false" styleId="newQuestionForm" enctype="multipart/form-data" method="POST">
		<html:hidden property="questionIndex" />

		<c:set var="title"><fmt:message key="label.edit.question"/></c:set>
		<lams:Page title="${title}" type="learner">

			<div class="form-group">
				<lams:CKEditor id="newQuestion"
					value="${questionDto.question}"
					contentFolderID="${sessionMap.contentFolderID}">
				</lams:CKEditor>
			</div>

			<div id="candidateArea">
				<%@ include file="/authoring/candidateAnswersList.jsp"%>
			</div>
			
			<div class="form-group">
				<html:button property="newCandidate" onclick="javascript:submitMethod('newCandidateBox');" styleClass="btn btn-default btn-sm">
					<fmt:message key="label.add.candidates" />
				</html:button>
			</div>
			
			<div id="questions-worth" class="form-group">
				<fmt:message key="label.questions.worth"/>&nbsp;
				<select name="mark" class="control-sm">
					<c:forEach var="i" begin="1" end="10">
						<option value="${i}"
								<c:if test="${i == questionDto.mark}">SELECTED</c:if> >
							${i}
						</option>
					</c:forEach>
				</select>
				&nbsp;<fmt:message key="label.marks"></fmt:message>
			</div>

			<div class="form-group">
				<a data-toggle="collapse" data-target="#feedbackDiv"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i><fmt:message key="label.feedback" /></a>
				<div id="feedbackDiv" class="panel-body collapse <c:if test="${not empty questionDto.feedback}">in</c:if>">
					<lams:CKEditor id="feedback"
						value="${questionDto.feedback}"
						contentFolderID="${sessionMap.contentFolderID}">
					</lams:CKEditor>
				</div>
			</div>

			<div class="pull-right">
				<a href="#" onclick="javascript:self.parent.tb_remove();" onmousedown="self.focus();" class="btn btn-default btn-sm roffset5"> 
					<fmt:message key="label.cancel" />
				</a>
				<a href="#" onclick="saveQuestion();"
					onmousedown="self.focus();" class="btn btn-default btn-sm"> 
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.save" />
				</a>
			</div>

		</lams:Page>
	</html:form>
</body>
</lams:html>
