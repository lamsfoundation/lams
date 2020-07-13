<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="5"	scope="request" />
		
<lams:html>
	<lams:head>
		<lams:css />
		<link href="<lams:LAMSURL/>/tool/laasse10/includes/css/assessment.css" rel="stylesheet" type="text/css">
		<link href="<lams:LAMSURL/>/css/bootstrap-toggle.css" rel="stylesheet" type="text/css">
		<link href="<lams:LAMSURL/>css/qb-question.css" rel="stylesheet" type="text/css">

		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/common.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.tabcontroller.js"></script>
		<script type="text/javascript">
			const VALIDATION_ERROR_LABEL = "<fmt:message key='error.form.validation.error'/>";
			const VALIDATION_ERRORS_LABEL = "<fmt:message key='error.form.validation.errors'><fmt:param >{errors_counter}</fmt:param></fmt:message>";
		</script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/qb-question.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap-toggle.js"></script>
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
		    			}
		    		},
     			    submitHandler: function(form) {
		    			$("#description").val(CKEDITOR.instances.description.getData());
		    			$("#feedback").val(CKEDITOR.instances.feedback.getData());
		    			$("#feedbackOnCorrect").val(CKEDITOR.instances.feedbackOnCorrect.getData());
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
			});
  		</script>
</lams:head>
<body>
	<div class="panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.true.false.question" />
		</div>
			
		<div class="panel-body">
			<form:form action="/lams/qb/edit/saveOrUpdateQuestion.do" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm"
				method="post" autocomplete="off">
				<form:hidden path="authoringRestricted"/>
				<form:hidden path="sessionMapID" />
				<form:hidden path="uid" />
				<form:hidden path="questionId" />
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
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
					
					<div class="voffset10-bottom form-group">
						<label for="correctAnswer">
							<fmt:message key="label.authoring.true.false.correct.answer" />
						</label>
						
						<c:set var="TRUE_LABEL"><fmt:message key="label.authoring.true.false.true" /></c:set>
						<c:set var="FALSE_LABEL"><fmt:message key="label.authoring.true.false.false" /></c:set>
						<form:checkbox path="correctAnswer" id="correctAnswer" 
							data-toggle="toggle" data-on="${TRUE_LABEL}" data-off="${FALSE_LABEL}" data-size="mini"/>
					</div>
				</div>
				
				<div class="settings-tab">
					 <div class="error">
				    	<lams:Alert id="errorMessages" type="danger" close="false" >
							<span></span>
						</lams:Alert>	
				    </div>
					
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
						<lams:CKEditor id="feedback" value="${assessmentQuestionForm.feedback}" 
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
					
					<lams:OutcomeAuthor qbQuestionId="${assessmentQuestionForm.questionId}"  />
				</div>
			</form:form>
		</div>	
	</div>
	
	<%@ include file="addQuestionFooter.jsp"%>
</body>
</lams:html>
