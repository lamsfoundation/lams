<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="8"	scope="request" />

<lams:html>
<lams:head>
	<%@ include file="/common/authoringQuestionHeader.jsp"%>
	<link href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css" rel="stylesheet">
	<script src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
	<style>
	
	/* --- SWITCH --- */
	
.switch {
  position: relative;
  display: inline-block;
  width: 40px;
  height: 23px;
}

.switch input { 
  opacity: 0;
  width: 0;
  height: 0;
}

.switch-slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.switch-slider:before {
  position: absolute;
  content: "";
  height: 17px;
  width: 17px;
  left: 3px;
  bottom: 0.2em;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked ~ .switch-slider {
  background-color: #2196F3;
}

input:focus ~ .switch-slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked ~ .switch-slider:before {
  -webkit-transform: translateX(17px);
  -ms-transform: translateX(17px);
  transform: translateX(17px);
}

/* Rounded switch-sliders */
.switch-slider.round {
  border-radius: 23px;
}

.switch-slider.round:before {
  border-radius: 50%;
}
	
	</style>
	
    <script>
		$(document).ready(function(){
		    $("#assessmentQuestionForm").validate({
		    	ignore: 'hidden',
		    	rules: {
		    		title: "required",
		    		defaultGrade: {
		    		      required: true,
		    		      digits: true
		    		},
		    		hasOptionFilled: {
		    			required: function(element) {
				   			prepareOptionEditorsForAjaxSubmit();
		    	        	return $("textarea[name^=optionString]:filled").length < 1;
			    	    }			    		    
	    		    }
		    	},
		    	messages: {
		    		title: "<fmt:message key='label.authoring.choice.field.required'/>",
		    		defaultGrade: {
		    			required: "<fmt:message key='label.authoring.choice.field.required'/>",
		    			digits: "<fmt:message key='label.authoring.choice.enter.integer'/>"
		    		},
		    		hasOptionFilled: {
		    			required: "<fmt:message key='label.authoring.numerical.error.answer'/>"
		    		}
		    	},
		    	   invalidHandler: formInvalidHandler,
		    	debug: true,
		    	errorClass: "alert alert-danger",
     			submitHandler: function(form) {
     			   	prepareOptionEditorsForAjaxSubmit();
		    		$("#optionList").val($("#optionForm").serialize(true));
		    		$("#question").val(CKEDITOR.instances.question.getData());
		    		$("#generalFeedback").val(CKEDITOR.instances.generalFeedback.getData());
		    		$("#feedbackOnCorrect").val(CKEDITOR.instances.feedbackOnCorrect.getData());
		    		$("#feedbackOnPartiallyCorrect").val(CKEDITOR.instances.feedbackOnPartiallyCorrect.getData());
		    		$("#feedbackOnIncorrect").val(CKEDITOR.instances.feedbackOnIncorrect.getData());
		    			
		    	   	var options = { 
		    	   		target:  parent.jQuery('#questionListArea'), 
		    	   		success: afterRatingSubmit  // post-submit callback
		    	    }; 				
		    		    				
		    		$('#assessmentQuestionForm').ajaxSubmit(options);
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
			
			<form:form action="saveOrUpdateQuestion.do" method="post" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm">
				<c:set var="sessionMap" value="${sessionScope[assessmentQuestionForm.sessionMapID]}" />
				<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" />
				<form:hidden path="sessionMapID" />
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
				<input type="hidden" name="optionList" id="optionList" />
				<form:hidden path="sequenceId" />
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
					    <form:input path="title" id="title" cssClass="borderless-text-input" tabindex="1" maxlength="255" 
					    	placeholder="${TITLE_LABEL}"/>
					</div>
				
					<div class="form-group">
						<c:set var="QUESTION_DESCRIPTION_LABEL"><fmt:message key="label.enter.question.description"/></c:set>
						<lams:CKEditor id="question" value="${assessmentQuestionForm.question}" contentFolderID="${assessmentQuestionForm.contentFolderID}" 
							placeholder="${QUESTION_DESCRIPTION_LABEL}"	 />
					</div>
					
					<input type="text" name="hasOptionFilled" id="hasOptionFilled" class="fake-validation-input">
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
	
					<c:if test="${!isAuthoringRestricted}">
						<div class="form-group form-inline">
						    <label for="defaultGrade">
						    	<fmt:message key="label.authoring.basic.default.question.grade" />:
						    	<i class="fa fa-xs fa-asterisk text-danger pull-right" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
						    </label>
						    <form:input path="defaultGrade" cssClass="form-control short-input-text input-sm"/>
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
						<lams:CKEditor id="generalFeedback" value="${assessmentQuestionForm.generalFeedback}" 
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
	
	<footer class="footer fixed-bottom">
		<div class="panel-heading">
        	<div class="pull-right">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" />
				</a>
				<a href="#nogo" onclick="javascript:$('#assessmentQuestionForm').submit();" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="label.authoring.save.button" />
				</a>
			</div>	
      	</div>
    </footer>
</body>
</lams:html>
