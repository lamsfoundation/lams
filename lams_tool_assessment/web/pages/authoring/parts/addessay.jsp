<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<link href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css">
	<%@ include file="/common/header.jsp"%>
	<link href="<lams:WebAppURL/>includes/css/addQuestion.css" rel="stylesheet" type="text/css">

	<script type="text/javascript">
		const VALIDATION_ERROR_LABEL = "<fmt:message key='error.form.validation.error'/>";
		const VALIDATION_ERRORS_LABEL = "<fmt:message key='error.form.validation.errors'><fmt:param >{errors_counter}</fmt:param></fmt:message>";
	</script>
	<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/authoring-question.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
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
		    		}
		    	},
	    		messages: {
	    			title: "<fmt:message key='label.authoring.choice.field.required'/>",
	    			defaultGrade: {
	    				required: "<fmt:message key='label.authoring.choice.field.required'/>",
	    				digits: "<fmt:message key='label.authoring.choice.enter.integer'/>"
	    			}
	    		},
	    	    invalidHandler: formInvalidHandler,
	    		debug: true,
	    		errorClass: "alert alert-danger",
   			    submitHandler: function(form) {
	    			$("#question").val(CKEDITOR.instances.question.getData());
	    			$("#generalFeedback").val(CKEDITOR.instances.generalFeedback.getData());
	     			    
	    	    	var options = { 
	    	    		target:  parent.jQuery('#questionListArea'), 
	    		   		success: afterRatingSubmit  // post-submit callback
	    		    }; 				
		    		    				
	    			$('#assessmentQuestionForm').ajaxSubmit(options);
	    		}
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
			
			<form:form action="saveOrUpdateQuestion.do" method="post" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm">
				<c:set var="sessionMap" value="${sessionScope[assessmentQuestionForm.sessionMapID]}" />
				<c:set var="isAuthoringRestricted" value="${sessionMap.isAuthoringRestricted}" />
				<form:hidden path="sessionMapID" />
				<form:hidden path="questionType" value="6"/>
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
						<div class="form-group form-inline">
						    <label for="defaultGrade">
						    	<fmt:message key="label.authoring.basic.default.question.grade" />:
						    	<i class="fa fa-xs fa-asterisk text-danger pull-right" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
						    </label>
						    <form:input path="defaultGrade" cssClass="form-control short-input-text input-sm"/>
						</div>
					</c:if>
					
					<div class="checkbox">
						<label for="allow-rich-editor">
							<form:checkbox path="allowRichEditor" id="allow-rich-editor"/>
							<fmt:message key="label.authoring.basic.allow.learners.rich.editor" />
						</label>
					</div>
					
					<div class="checkbox">
					    <label for="max-words-limit-checkbox">
							<input type="checkbox" id="max-words-limit-checkbox" name="noname"
									<c:if test="${assessmentQuestionForm.maxWordsLimit != 0}">checked="checked"</c:if>/>
					    	<fmt:message key="label.maximum.number.words" />
					    </label>
					    <form:input path="maxWordsLimit" id="max-words-limit"/>
					</div>
					
					<div class="checkbox">
					    <label for="min-words-limit-checkbox">
							<input type="checkbox" id="min-words-limit-checkbox" name="noname"
									<c:if test="${assessmentQuestionForm.minWordsLimit != 0}">checked="checked"</c:if>/>
					    	<fmt:message key="label.minimum.number.words" />
					    </label>
					    <form:input path="minWordsLimit" id="min-words-limit"/>
					</div>
	
					<div class="voffset5 form-group">
						<c:set var="GENERAL_FEEDBACK_LABEL"><fmt:message key="label.authoring.basic.general.feedback"/></c:set>
						<lams:CKEditor id="generalFeedback" value="${assessmentQuestionForm.generalFeedback}" 
							placeholder="${GENERAL_FEEDBACK_LABEL}" contentFolderID="${assessmentQuestionForm.contentFolderID}" />
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
