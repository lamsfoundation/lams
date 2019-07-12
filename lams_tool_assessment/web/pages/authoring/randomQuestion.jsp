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
			Add random question
		</div>
			
		<div class="panel-body">
			
			<form:form action="/lams/qb/edit/saveOrUpdateQuestion.do" modelAttribute="assessmentQuestionForm" id="assessmentQuestionForm" 
				method="post" autocomplete="off">
				<form:hidden path="authoringRestricted"/>
				<form:hidden path="sessionMapID" />
				<form:hidden path="uid" />
				<form:hidden path="questionType" value="-1"/>
				
				<div class="question-tab">
					<lams:errors/>
					 <div class="error">
				    	<lams:Alert id="errorMessages" type="danger" close="false" >
							<span></span>
						</lams:Alert>	
				    </div>
					
					<!-- 
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
					 -->
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
					<fmt:message key="button.save" />
				</a>
			</div>
      	</div>
    </footer>
</body>
</lams:html>
