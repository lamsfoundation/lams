<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="1"	scope="request" />

<lams:html>
<lams:head>
	<%@ include file="addQuestionHeader.jsp"%>
    <script>
		$(document).ready(function(){
		    $("#assessmentQuestionForm").validate({
		    	ignore: 'hidden, div.cke_editable',
	    		rules: {
	    			title: "required",
	    			maxMark: {
	    			    required: true,
	    			    digits: true
	    			},
	    			penaltyFactor: {
	    			    required: true,
	    			    number: true
	    			},
	    			hasOptionFilled: {
	    				required: function(element) {
			    			prepareOptionEditorsForAjaxSubmit();
	    		        	return $("textarea[name^=optionName]:filled").length < 1;
		    		    }			    		    
    			    },
    			    hasOneHundredMaxMark: {
	    				required: function(element) {
	    					var hasOneHundredMaxMark = false;
	    					$("input[name^='optionMaxMark']").each(function() {
	    						hasOneHundredMaxMark = hasOneHundredMaxMark || (eval(this.value) == 1);
	    					});
    			    		return !hasOneHundredMaxMark && !eval($("#multipleAnswersAllowed").val());
		    		    }		    		    
    			    }
	    		},
	    		messages: {
	    			title: "<fmt:message key='label.authoring.choice.field.required'/>",
	    			maxMark: {
	    				required: "<fmt:message key='label.authoring.choice.field.required'/>",
	    				digits: "<fmt:message key='label.authoring.choice.enter.integer'/>"
	    			},
	    			penaltyFactor: {
	    				required: "<fmt:message key='label.authoring.choice.field.required'/>",
	    				number: "<fmt:message key='label.authoring.choice.enter.float'/>"
	    			},
	    			hasOptionFilled: {
	    				required: "<fmt:message key='label.authoring.numerical.error.answer'/>"
	    			},
	    			hasOneHundredMaxMark: {
	    				required: "<fmt:message key='error.form.validation.hundred.score'/>"
	    			}
	    		},
   			    submitHandler: function(form) {
   			    	prepareOptionEditorsForAjaxSubmit();
	    			$("#optionList").val($("#optionForm").serialize(true));
	    			$("#description").val(CKEDITOR.instances.description.getData());
	    			$("#feedback").val(CKEDITOR.instances.feedback.getData());
	    			$("#feedbackOnCorrect").val(CKEDITOR.instances.feedbackOnCorrect.getData());
	    			$("#feedbackOnPartiallyCorrect").val(CKEDITOR.instances.feedbackOnPartiallyCorrect.getData());
	    			$("#feedbackOnIncorrect").val(CKEDITOR.instances.feedbackOnIncorrect.getData());
	    			$("#new-collection-uid").val($("#collection-uid-select option:selected").val());
		    			
	    	    	var options = { 
	    	    		target:  parent.jQuery('#itemArea'), 
	    		   		success: afterRatingSubmit  // post-submit callback
	    		    }; 				
		    		    				
	    			$('#assessmentQuestionForm').ajaxSubmit(options);
	    		},
	    	    invalidHandler: formValidationInvalidHandler,
				errorElement: "em",
				errorPlacement: formValidationErrorPlacement,
				success: formValidationSuccess,
				highlight: formValidationHighlight,
				unhighlight: formValidationUnhighlight
		  	});
		    	
			$("#multipleAnswersAllowed").on('change', function() {
				$("#incorrect-answer-nullifies-mark-area").toggle(eval($(this).val()));
			}).trigger("change");

			// Only one of prefixAnswersWithLetters or shuffle at a time
 			$("#prefixAnswersWithLetters").on('change', function() {
				if ( this.checked ) {
					if ($("#shuffle").prop('checked')) {
						$("#shuffle").prop('checked', false);
					}
					$("#shuffle").prop('disabled', true);
					$("#shuffleText").addClass('text-muted');
				} else {
					$("#shuffle").prop('disabled', false);
					$("#shuffleText").removeClass('text-muted');
				}
			}).trigger("change");

			$("#shuffle").on('change', function() {
				if ( this.checked ) {
					if ($("#prefixAnswersWithLetters").prop('checked')) {
						$("#prefixAnswersWithLetters").prop('checked', false);
					}
					$("#prefixAnswersWithLetters").prop('disabled', true);
					$("#prefixAnswersWithLettersText").addClass('text-muted');
				} else {
					$("#prefixAnswersWithLetters").prop('disabled', false);
					$("#prefixAnswersWithLettersText").removeClass('text-muted');
				}
			}).trigger("change");
		});
	</script>
</lams:head>
<body>
	<div class="panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.basic.type.multiple.choice" />
		</div>
			
		<div class="panel-body">
			
			<form:form action="/lams/qb/edit/saveOrUpdateQuestion.do" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm"
				method="post" autocomplete="off">
				<form:hidden path="authoringRestricted"/>
				<form:hidden path="sessionMapID" />
				<form:hidden path="uid" />
				<form:hidden path="questionId" />
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
				<input type="hidden" name="optionList" id="optionList" />
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<form:hidden path="oldCollectionUid" id="old-collection-uid"/>
				<form:hidden path="newCollectionUid" id="new-collection-uid"/>
				<form:hidden path="contentFolderID" id="contentFolderID"/>
				
				<button type="button" id="question-settings-link" class="btn btn-default btn-sm">
					<fmt:message key="label.settings" />
				</button>

				<div class="question-tab">
					<lams:errors/>
					 <div class="error">
				    	<lams:Alert id="errorMessages" type="danger" close="false" >
							<span></span>
						</lams:Alert>	
				    </div>
				
					<div id="title-container" class="form-group">
						<c:set var="TITLE_LABEL"><fmt:message key="label.enter.question.title"/> </c:set>
					    <form:input path="title" id="title" cssClass="form-control borderless-text-input" tabindex="1" maxlength="255"  
					    	placeholder="${TITLE_LABEL}"/>
					</div>
				
					<div class="form-group">
						<c:set var="QUESTION_DESCRIPTION_LABEL"><fmt:message key="label.enter.question.description"/></c:set>
						<lams:CKEditor id="description" value="${assessmentQuestionForm.description}" contentFolderID="${assessmentQuestionForm.contentFolderID}" 
							placeholder="${QUESTION_DESCRIPTION_LABEL}"	 />
					</div>
				
					<div>
						<input type="text" name="hasOptionFilled" id="hasOptionFilled" class="fake-validation-input">
					</div>
					<div>
						<input type="text" name="hasOneHundredMaxMark" id="hasOneHundredMaxMark" class="fake-validation-input">
					</div>
				</div>

				<div class="settings-tab">
					 <div class="error">
				    	<lams:Alert id="errorMessages" type="danger" close="false" >
							<span></span>
						</lams:Alert>	
				    </div>
					
					<c:if test="${!assessmentQuestionForm.authoringRestricted}">
						<div class="form-group row">
						    <label for="maxMark" class="col-sm-3">
						    	<fmt:message key="label.authoring.basic.default.question.grade" />
						    	<i class="fa fa-xs fa-asterisk text-danger" style="vertical-align: super;" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
						    </label>
						    
						    <div class="col-sm-9">
						    	<form:input path="maxMark" cssClass="form-control short-input-text input-sm"/>
						    </div>
						</div>
					</c:if>
					    					
					<div class="form-group row">
					    <label for="penaltyFactor" class="col-sm-3"> 
					    	<fmt:message key="label.authoring.basic.penalty.factor" />
							<i class="fa fa-xs fa-asterisk text-danger" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
					    </label>
					    
					    <div class="col-sm-9">
					    	<form:input path="penaltyFactor" cssClass="form-control short-input-text input-sm"/>
					    </div>
					</div>
					
					<div class="form-group row form-inline">
						<label for="multipleAnswersAllowed" class="col-sm-3">
							<fmt:message key="label.authoring.choice.one.multiple.answers" />
						</label>
						
						<div class="col-sm-9">
							<form:select path="multipleAnswersAllowed" id="multipleAnswersAllowed" cssClass="form-control input-sm">
								<form:option value="false"><fmt:message key="label.authoring.choice.one.answer" /></form:option>
								<form:option value="true"><fmt:message key="label.authoring.choice.multiple.answers" /></form:option>
							</form:select>
						</div>
					</div>

					<div id="incorrect-answer-nullifies-mark-area">
						<label class="switch">
							<form:checkbox path="incorrectAnswerNullifiesMark" id="incorrectAnswerNullifiesMark"/>
							<span class="switch-slider round"></span>
						</label>
						<label for="incorrectAnswerNullifiesMark">		
							<fmt:message key="label.incorrect.answer.nullifies.mark" />
						</label>
					</div>
		
					<div>
						<label class="switch">
							<form:checkbox path="shuffle" id="shuffle"/>
							<span class="switch-slider round"></span>
						</label>
						<label for="shuffle">		
							<fmt:message key="label.authoring.basic.shuffle.the.choices" />
						</label>
					</div>

					<div id="prefixAnswersWithLettersDiv">
						<label class="switch">
							<form:checkbox path="prefixAnswersWithLetters" id="prefixAnswersWithLetters"/>
							<span class="switch-slider round"></span>
						</label>
						<label for="prefixAnswersWithLetters">		
							<span id="prefixAnswersWithLettersText"><fmt:message key="label.prefix.sequential.letters.for.each.answer" /></span>
						</label>
					</div>
				
					<!-- Overall feedback -->
					<!-- <h5 style="margin-top: 20px;">Feedbacks</h5> -->
	
					<div class="form-group">
						<c:set var="GENERAL_FEEDBACK_LABEL">General Feedback</c:set>
						<lams:CKEditor id="feedback" value="${assessmentQuestionForm.feedback}" 
							placeholder="${GENERAL_FEEDBACK_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}" />
					</div>
					
					<div class="form-group">
						<c:set var="FEEDBACK_ON_CORRECT_LABEL">Feedback on any correct response</c:set>
						<lams:CKEditor id="feedbackOnCorrect" value="${assessmentQuestionForm.feedbackOnCorrect}" 
							placeholder="${FEEDBACK_ON_CORRECT_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}"/>
					</div>
					
					<div class="form-group">
						<c:set var="FEEDBACK_ON_PARTICALLY_CORRECT_LABEL">Feedback on any partially correct response</c:set>
						<lams:CKEditor id="feedbackOnPartiallyCorrect" value="${assessmentQuestionForm.feedbackOnPartiallyCorrect}" 
							placeholder="${FEEDBACK_ON_PARTICALLY_CORRECT_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}"/>
					</div>
							
					<div class="form-group">
						<c:set var="FEEDBACK_ON_INCORRECT_LABEL">Feedback on any incorrect response</c:set>
						<lams:CKEditor id="feedbackOnIncorrect" value="${assessmentQuestionForm.feedbackOnIncorrect}" 
							placeholder="${FEEDBACK_ON_INCORRECT_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}"/>
					</div>
					
					<lams:OutcomeAuthor qbQuestionId="${assessmentQuestionForm.questionId}" />
				</div><!-- settings tab ends -->
			</form:form>
			
			<!-- Options -->
			<div class="question-tab">	
				<form id="optionForm" name="optionForm" class="form-group">
					<%@ include file="optionlist.jsp"%>
					
					<a href="#nogo" onclick="javascript:addOption();" class="btn btn-xs btn-default button-add-item pull-right">
						<fmt:message key="label.authoring.choice.add.option" /> 
					</a>
				</form>
			</div>		
		</div>	
	</div>	
	
	<%@ include file="addQuestionFooter.jsp"%>	
</body>
</lams:html>
