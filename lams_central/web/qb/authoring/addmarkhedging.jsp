<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="8"	scope="request" />

<lams:html>
<lams:head>
	<link href="<lams:LAMSURL/>/css/bootstrap-toggle.css" rel="stylesheet" type="text/css">
	<%@ include file="addQuestionHeader.jsp"%>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap-toggle.js"></script>
	
    <script>
		$(document).ready(function(){
		    $("#assessmentQuestionForm").validate({
		    	ignore: 'hidden',
		    	rules: {
		    		title: "required",
		    		maxMark: {
		    		      required: true,
		    		      digits: true
		    		},
		    		hasOptionFilled: {
		    			required: function(element) {
				   			prepareOptionEditorsForAjaxSubmit();
		    	        	return $("textarea[name^=optionName]:filled").length < 1;
			    	    }			    		    
	    		    }
		    	},
		    	messages: {
		    		title: "<fmt:message key='label.authoring.choice.field.required'/>",
		    		maxMark: {
		    			required: "<fmt:message key='label.authoring.choice.field.required'/>",
		    			digits: "<fmt:message key='label.authoring.choice.enter.integer'/>"
		    		},
		    		hasOptionFilled: {
		    			required: "<fmt:message key='label.authoring.numerical.error.answer'/>"
		    		}
		    	},
     			submitHandler: function(form) {
     			   	prepareOptionEditorsForAjaxSubmit();
		    		$("#optionList").val($("#optionForm").serialize(true));
		    		$("#question").val(CKEDITOR.instances.question.getData());
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

		    //enforce only one correct option
		    $(document).on("change", "input[type='checkbox'][name='optionCorrect']", function() {
		        if(this.checked) {
		        	$("input[type='checkbox'][name='optionCorrect']").not(this).bootstrapToggle('off');
		        }
		    });	
		});   
 	</script>
</lams:head>
<body>
	<div class="panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.basic.type.mark.hedging" />
		</div>
			
		<div class="panel-body">
			
			<form:form action="/lams/qb/edit/saveOrUpdateQuestion.do" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm"
				method="post" autocomplete="off">
				<form:checkbox path="authoringRestricted"/>
				<form:hidden path="sessionMapID" />
				<form:hidden path="uid" />
				<form:hidden path="questionId" />
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
				<input type="hidden" name="optionList" id="optionList" />
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
						<lams:CKEditor id="question" value="${assessmentQuestionForm.question}" contentFolderID="${assessmentQuestionForm.contentFolderID}" 
							placeholder="${QUESTION_DESCRIPTION_LABEL}"	 />
					</div>
					
					<div>
						<input type="text" name="hasOptionFilled" id="hasOptionFilled" class="fake-validation-input">
					</div>
				</div>
				
				<div class="settings-tab">
					 <div class="error">
				    	<lams:Alert id="errorMessages" type="danger" close="false" >
							<span></span>
						</lams:Alert>	
				    </div>

					<label class="switch">
						<form:checkbox path="answerRequired" id="answer-required"/>
						<span class="switch-slider round"></span>
					</label>
					<label for="answer-required">
						<fmt:message key="label.authoring.answer.required" />
					</label>
	
					<c:if test="${!assessmentQuestionForm.authoringRestricted}">
						<div class="form-group row form-inline">
						    <label for="maxMark" class="col-sm-3">
						    	<fmt:message key="label.authoring.basic.default.question.grade" />
						    	<i class="fa fa-xs fa-asterisk text-danger" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
						    </label>
						    
						    <div class="col-sm-9">
						    	<form:input path="maxMark" cssClass="form-control short-input-text input-sm"/>
						    </div>
						</div>
					</c:if>
					
					<div>
						<label class="switch">
							<form:checkbox path="shuffle" id="shuffle"/>
							<span class="switch-slider round"></span>
						</label>
						<label for="shuffle">		
							<fmt:message key="label.authoring.basic.shuffle.the.choices" />
						</label>
					</div>
					
					<div>
						<label class="switch">
							<form:checkbox path="hedgingJustificationEnabled" id="hedging-justification"/>
							<span class="switch-slider round"></span>
						</label>
						<label for="hedging-justification">
							<fmt:message key="label.ask.for.hedging.justification" />
						</label>
					</div>
	
					<div class="voffset5 form-group">
						<c:set var="GENERAL_FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.general.feedback"/></c:set>
						<lams:CKEditor id="feedback" value="${assessmentQuestionForm.feedback}" 
							placeholder="${GENERAL_FEEDBACK_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}" />
					</div>
			
					<!-- Overall feedback -->
			
					<div class="form-group">
						<c:set var="FEEDBACK_ON_CORRECT_LABEL"><fmt:message key="label.authoring.choice.feedback.on.correct"/></c:set>
						<lams:CKEditor id="feedbackOnCorrect" value="${assessmentQuestionForm.feedbackOnCorrect}" 
							placeholder="${FEEDBACK_ON_CORRECT_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}"/>
					</div>
					
					<div class="form-group">
						<c:set var="FEEDBACK_ON_PARTIALLY_CORRECT_LABEL"><fmt:message key="label.authoring.choice.feedback.on.partially.correct"/></c:set>
						<lams:CKEditor id="feedbackOnPartiallyCorrect" value="${assessmentQuestionForm.feedbackOnPartiallyCorrect}" 
							placeholder="${FEEDBACK_ON_PARTIALLY_CORRECT_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}"/>
					</div>
					
					<div class="form-group">
						<c:set var="FEEDBACK_ON_INCORRECT_LABEL"><fmt:message key="label.authoring.choice.feedback.on.incorrect"/></c:set>
						<lams:CKEditor id="feedbackOnIncorrect" value="${assessmentQuestionForm.feedbackOnIncorrect}" 
							placeholder="${FEEDBACK_ON_INCORRECT_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}"/>
					</div>
					
					<c:if test="${not empty assessmentQuestionForm.questionId}">
						<lams:OutcomeAuthor qbQuestionId="${assessmentQuestionForm.questionId}"  />
					</c:if>
				</div>
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
