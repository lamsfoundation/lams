<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<lams:css />
	<link href="<lams:LAMSURL/>tool/laasse10/includes/css/assessment.css" rel="stylesheet" type="text/css">
	<link href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css">
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
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
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
		    		maxWordsLimit: {
		    		    digits: true
		    		},
		    		minWordsLimit: {
		    		    digits: true
		    		}
		    	},
	    		messages: {
	    			title: "<fmt:message key='label.authoring.choice.field.required'/>",
	    			maxMark: {
	    				required: "<fmt:message key='label.authoring.choice.field.required'/>",
	    				digits: "<fmt:message key='label.authoring.choice.enter.integer'/>"
	    			}
	    		},
   			    submitHandler: function(form) {
	    			$("#question").val(CKEDITOR.instances.question.getData());
	    			$("#feedback").val(CKEDITOR.instances.feedback.getData());
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
		    	
		   	//spinner
	    	var validateMinMax = function() {
	    		// min can't be more than max
	    		var min = $( "#min-words-limit" ),
	    			max = $( "#max-words-limit" ),
	    			minVal = +min.val(),
	    			maxVal = +max.val();
	    		if (minVal > maxVal){
	    			max.val(minVal);
	    		}
	    	},
		    	maximumWordsSpinner = $( "#max-words-limit" ).spinner({ 
			   		min: 0,
			   		disabled: ($( "#max-words-limit" ).val() == 0),
			   		change: validateMinMax,
			   		stop: validateMinMax
		    	}),
		    	minimumWordsSpinner = $( "#min-words-limit" ).spinner({ 
			   		min: 0,
			   		disabled: ($( "#min-words-limit" ).val() == 0),
			   		change: validateMinMax,
			   		stop: validateMinMax
		    	});
	    	$("#max-words-limit-checkbox").click(function() {
	    		if ( maximumWordsSpinner.spinner( "option", "disabled" ) ) {
	    			 maximumWordsSpinner.spinner( "enable" );
	    		} else {
	    			maximumWordsSpinner.spinner( "disable" );
	    		}
	        });
		    	
		    //spinner
	    	$("#min-words-limit-checkbox").click(function() {
	    		if ( minimumWordsSpinner.spinner( "option", "disabled" ) ) {
	    			minimumWordsSpinner.spinner( "enable" );
	    		} else {
	    			minimumWordsSpinner.spinner( "disable" );
	    		}
	        });
		});
  	</script>
</lams:head>
	
<body>
	<div class="panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.basic.type.essay" />
		</div>
			
		<div class="panel-body">
			
			<form:form action="/lams/qb/edit/saveOrUpdateQuestion.do" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm" 
				method="post" autocomplete="off">
				<form:hidden path="authoringRestricted"/>
				<form:hidden path="sessionMapID" />
				<form:hidden path="uid" />
				<form:hidden path="questionType" value="6"/>
				<form:hidden path="oldCollectionUid" id="old-collection-uid"/>
				<form:hidden path="newCollectionUid" id="new-collection-uid"/>

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
				
					<div class="form-group form-group-cke">
						<c:set var="QUESTION_DESCRIPTION_LABEL"><fmt:message key="label.enter.question.description"/></c:set>
						<lams:CKEditor id="question" value="${assessmentQuestionForm.question}" contentFolderID="${assessmentQuestionForm.contentFolderID}" 
							placeholder="${QUESTION_DESCRIPTION_LABEL}"	 />
					</div>
				</div>
				
				<div class="settings-tab">
					<div class="error">
				    	<lams:Alert id="errorMessages" type="danger" close="false" >
							<span></span>
						</lams:Alert>	
				    </div>
				    
					<div>
						<label class="switch">
							<form:checkbox path="answerRequired" id="answer-required"/>
							<span class="switch-slider round"></span>
						</label>
						<label for="answer-required">
							<fmt:message key="label.authoring.answer.required" />
						</label>
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

					<div>
						<label class="switch">
							<form:checkbox path="allowRichEditor" id="allow-rich-editor"/>
							<span class="switch-slider round"></span>
						</label>
						<label for="allow-rich-editor">		
							<fmt:message key="label.authoring.basic.allow.learners.rich.editor" />
						</label>
					</div>
					
					<div class="form-group row form-inline" style="display: flex; align-items: center;">
					    <label for="max-words-limit-checkbox" class="col-sm-3">
							<input type="checkbox" id="max-words-limit-checkbox" name="noname"
								<c:if test="${assessmentQuestionForm.maxWordsLimit != 0}">checked="checked"</c:if>/>
					    	<fmt:message key="label.maximum.number.words" />
					    </label>
					    
					    <div class="col-sm-9">
					   		<form:input path="maxWordsLimit" id="max-words-limit"/>
					    	<label id="max-words-limit-error" class="alert alert-danger" for="max-words-limit" style="display: none;"></label>
					    </div>
					</div>
					
					<div class="form-group row form-inline" style="display: flex; align-items: center;">
					    <label for="min-words-limit-checkbox" class="col-sm-3">
							<input type="checkbox" id="min-words-limit-checkbox" name="noname"
								<c:if test="${assessmentQuestionForm.minWordsLimit != 0}">checked="checked"</c:if>/>
					    	<fmt:message key="label.minimum.number.words" />
					    </label>
					    
					    <div class="col-sm-9">
					    	<form:input path="minWordsLimit" id="min-words-limit"/>
					    	<label id="min-words-limit-error" class="alert alert-danger" for="min-words-limit" style="display: none;"></label>
					    </div>
					</div>
	
					<div class="voffset5 form-group">
						<c:set var="GENERAL_FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.general.feedback"/></c:set>
						<lams:CKEditor id="feedback" value="${assessmentQuestionForm.feedback}" 
							placeholder="${GENERAL_FEEDBACK_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}" />
					</div>
					
					<c:if test="${not empty assessmentQuestionForm.questionId}">
						<lams:OutcomeAuthor qbQuestionId="${assessmentQuestionForm.questionId}"  />
					</c:if>
				</div>
					
			</form:form>

		</div>		
	</div>		
	
	<%@ include file="addQuestionFooter.jsp"%>
</body>
</lams:html>
