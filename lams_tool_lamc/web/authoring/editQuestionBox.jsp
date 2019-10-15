<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapId]}" />
<c:set var="questionDtos" value="${sessionMap.questionDtos}" />
<c:set var="questionDto" value="${sessionMap.questionDto}" />
<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" />

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
	    		$('#mcAuthoringForm').ajaxSubmit({
		    		url: "<lams:WebAppURL />authoring/"+actionMethod+".do",
	    			data: { 
						sessionMapId: '${sessionMapId}',
					},
					cache: true,
    	    		target:  $('#candidateArea')
    		    });
			}
				
			function saveQuestion() {
				$("#description").val(CKEDITOR.instances.description.getData());
				$("textarea[name^=ca],textarea[name=feedback]").each(function() {
					var name = $(this).attr("name");
					var value = CKEDITOR.instances[name].getData();
					$(this).val(value);
				});
				
				if (validateSingleCorrectAnswer()) { 
					$('#mcAuthoringForm').ajaxSubmit({
						url: "<c:url value='/authoring/saveQuestion.do' />",
						data: { 
							sessionMapId: '${sessionMapId}',
						},
	    	    		target:  parent.jQuery('#itemArea'), 
	    	    		success: function() { 
	    	    			self.parent.refreshThickbox();
	    	    			self.parent.tb_remove();
	    	    	    }
	    		    });
				}
			}
				
			function submitModifyCandidate(candidateIndexValue, actionMethod) {
				document.forms.mcAuthoringForm.candidateIndex.value=candidateIndexValue;
				submitMethod(actionMethod);
			}

			function removeCandidate(candidateIndexValue) {
				if (validateMinumumCandidateCount()) {
					document.forms.mcAuthoringForm.candidateIndex.value=candidateIndexValue;
					submitMethod("removeCandidate");
				}
			}
	
			function validateSingleCorrectAnswer() {
				
				//question.blank
				if (!$("#name").val()) {
					var msg = "<fmt:message key="question.blank"/>";
					alert(msg);
					return false;
				}
				
				var singleCorrectEntry = 0;
				var radioCorrect=document.forms.mcAuthoringForm.correct;
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
				var radioCorrect=document.forms.mcAuthoringForm.correct;
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

	<form:form action="saveQuestion.do" modelAttribute="mcAuthoringForm" id="mcAuthoringForm" enctype="multipart/form-data" method="POST">
		<form:hidden path="questionIndex" />
		<input type="hidden" name="qbQuestionUid" value="${questionDto.qbQuestionUid}" />
		<input type="hidden" name="contentFolderId" value="${questionDto.contentFolderId}" />
		
		<c:set var="title"><fmt:message key="label.edit.question"/></c:set>
		<lams:Page title="${title}" type="learner" formID="mcAuthoringForm">
		
			<div class="form-group">
				<label for="name">
					<fmt:message key="label.authoring.title" />
				</label>
				<input type="text" id="name" name="name" value="${questionDto.name}" class="form-control"/>
			</div>

			<div class="form-group">
				<label for="description">
					<fmt:message key="label.question" />
				</label>
			
				<lams:CKEditor id="description"
					value="${questionDto.description}"
					contentFolderID="${questionDto.contentFolderId}">
				</lams:CKEditor>
			</div>

			<div id="candidateArea">
				<%@ include file="/authoring/candidateAnswersList.jsp"%>
			</div>
			
			<div class="form-group">
				<button name="newCandidate" onclick="javascript:submitMethod('newCandidateBox');" type="button" class="btn btn-default btn-sm">
					<fmt:message key="label.add.candidates" />
				</button>
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
				<a data-toggle="collapse" data-target="#feedbackDiv" href="#fdbackDiv"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i><fmt:message key="label.feedback" /></a>
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
				<a href="#" onclick="javascript:saveQuestion();"
					onmousedown="self.focus();" class="btn btn-default btn-sm"> 
					<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.save" />
				</a>
			</div>

		</lams:Page>
	</form:form>
</body>
</lams:html>
