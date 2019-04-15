<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="5"	scope="request" />
		
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<link href="<lams:WebAppURL/>includes/css/addQuestion.css" rel="stylesheet" type="text/css">

		<script type="text/javascript">
			const VALIDATION_ERROR_LABEL = "<fmt:message key='error.form.validation.error'/>";
			const VALIDATION_ERRORS_LABEL = "<fmt:message key='error.form.validation.errors'><fmt:param >{errors_counter}</fmt:param></fmt:message>";
		</script>
		<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/authoring-question.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
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
		    			penaltyFactor: {
		    			      required: true,
		    			      number: true
		    			}
		    		},
		    		messages: {
		    			title: "<fmt:message key='label.authoring.choice.field.required'/>",
		    			defaultGrade: {
		    				required: "<fmt:message key='label.authoring.choice.field.required'/>",
		    				digits: "<fmt:message key='label.authoring.choice.enter.integer'/>"
		    			},
		    			penaltyFactor: {
		    				required: "<fmt:message key='label.authoring.choice.field.required'/>",
		    				number: "<fmt:message key='label.authoring.choice.enter.float'/>"
		    			}
		    		},
		    	    invalidHandler: formInvalidHandler,
		    		debug: true,
		    		errorClass: "alert alert-danger",
     			    submitHandler: function(form) {
		    			$("#question").val(CKEDITOR.instances.question.getData());
		    			$("#generalFeedback").val(CKEDITOR.instances.generalFeedback.getData());
		    			$("#feedbackOnCorrect").val(CKEDITOR.instances.feedbackOnCorrect.getData());
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
			<fmt:message key="label.authoring.true.false.question" />
		</div>
			
		<div class="panel-body">
			<form:form action="saveOrUpdateQuestion.do" method="post" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm">
				<c:set var="sessionMap" value="${sessionScope[assessmentQuestionForm.sessionMapID]}" />
				<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" />
				<form:hidden path="sessionMapID" />
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
				<form:hidden path="sequenceId" />

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
				
					<div class="voffset10 form-inline form-group">
						<label for="correctAnswer">
							<fmt:message key="label.authoring.true.false.correct.answer" />
						</label>
						<form:select path="correctAnswer" cssClass="form-control input-sm">
							<form:option value="false"><fmt:message key="label.authoring.true.false.false" /></form:option>
							<form:option value="true"><fmt:message key="label.authoring.true.false.true" /></form:option>
						</form:select>
					</div>
				</div>
				
				<div class="settings-tab">
					 <div class="error">
				    	<lams:Alert id="errorMessages" type="danger" close="false" >
							<span></span>
						</lams:Alert>	
				    </div>
				
					<div class="checkbox">
						<label for="answer-required">
							<form:checkbox path="answerRequired" id="answer-required"/>
							<fmt:message key="label.authoring.answer.required" />
						</label>
					</div>				
					
					<c:if test="${!isAuthoringRestricted}">
						<div class="form-group row form-inline">
						    <label for="defaultGrade" class="col-sm-3">
						    	<fmt:message key="label.authoring.basic.default.question.grade" />
						    	<i class="fa fa-xs fa-asterisk text-danger" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
						    </label>
						    
						    <div class="col-sm-9">
						    	<form:input path="defaultGrade" cssClass="form-control short-input-text input-sm"/>
						    </div>
						</div>
					</c:if>
					
					<div class="form-group row form-inline">
					    <label for="penaltyFactor" class="col-sm-3"> 
					    	<fmt:message key="label.authoring.basic.penalty.factor" />
							  <i class="fa fa-xs fa-asterisk text-danger" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
					    </label>
					    
					    <div class="col-sm-9">
					    	<form:input path="penaltyFactor" cssClass="form-control short-input-text input-sm"/>
					    </div>
					</div>
									
					<div class="voffset5 form-group">
						<c:set var="GENERAL_FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.general.feedback"/></c:set>
						<lams:CKEditor id="generalFeedback" value="${assessmentQuestionForm.generalFeedback}" 
							placeholder="${GENERAL_FEEDBACK_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}" />
					</div>
					
					<div class="form-group">
				    	<c:set var="FEEDBACK_ON_TRUE_LABEL"><fmt:message key="label.authoring.true.false.feedback.on.true"/></c:set>
						<lams:CKEditor id="feedbackOnCorrect" value="${assessmentQuestionForm.feedbackOnCorrect}" 
							placeholder="${FEEDBACK_ON_TRUE_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}"/>
					</div>
						
					<div class="form-group">
				        <c:set var="FEEDBACK_ON_FALSE_LABEL"><fmt:message key="label.authoring.true.false.feedback.on.false"/></c:set>
						<lams:CKEditor id="feedbackOnIncorrect" value="${assessmentQuestionForm.feedbackOnIncorrect}" 
							placeholder="${FEEDBACK_ON_FALSE_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}"/>
					</div>
				</div>
			</form:form>
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
