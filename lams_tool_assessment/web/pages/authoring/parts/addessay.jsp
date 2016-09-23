<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<link href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" rel="stylesheet" type="text/css">
	<%@ include file="/common/header.jsp"%>
	<link href="<html:rewrite page='/includes/css/addQuestion.css'/>" rel="stylesheet" type="text/css">

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
    <script>
			$(document).ready(function(){
		    	$("#assessmentQuestionForm").validate({
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
		    	    invalidHandler: function(form, validator) {
		    		      var errors = validator.numberOfInvalids();
		    		      if (errors) {
		    		          var message = errors == 1
		    		          	  ? "<fmt:message key='error.form.validation.error'/>"
		    		          	  : "<fmt:message key='error.form.validation.errors'><fmt:param >" + errors + "</fmt:param></fmt:message>";
	    		          	  
		    		          $("div.error span").html(message);
		    		          $("div.error").show();
		    		      } else {
		    		          $("div.error").hide();
		    		      }
		    		},
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
			
    		// post-submit callback 
    		function afterRatingSubmit(responseText, statusText)  { 
    			self.parent.refreshThickbox();
    			self.parent.tb_remove();
    		}
  	</script>
		
</lams:head>
	
<body onload="parent.resizeIframe();">
	<div class="panel panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.basic.type.essay" />
		</div>
			
		<div class="panel-body">
			<%@ include file="/common/messages.jsp"%>
		    <div class="error">
		    	<lams:Alert id="errorMessages" type="danger" close="false" >
					<span></span>
				</lams:Alert>	
		    </div>
			
			<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="assessmentQuestionForm">
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<html:hidden property="sessionMapID" />
				<html:hidden property="questionType" value="6"/>
				<html:hidden property="questionIndex" />
				
				<div class="form-group">
				    <label for="title">
				    	<fmt:message key="label.authoring.basic.question.name"/>
				    	<i class="fa fa-xs fa-asterisk text-danger pull-right" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
				    </label>
				    <html:text property="title" styleId="title" styleClass="form-control" tabindex="1"/>
				</div>
			
				<div class="form-group">
					<label for="question">
						<fmt:message key="label.authoring.basic.question.text" />
					</label>
		            	
					<lams:CKEditor id="question" value="${formBean.question}" contentFolderID="${formBean.contentFolderID}" />
				</div>
				
				<div class="form-group">
				    <label for="defaultGrade">
				    	<fmt:message key="label.authoring.basic.default.question.grade" />
				    	<i class="fa fa-xs fa-asterisk text-danger pull-right" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
				    </label>
				    <html:text property="defaultGrade" styleClass="form-control short-input-text input-sm"/>
				</div>
				
				<div class="checkbox">
					<label for="answer-required">
						<html:checkbox property="answerRequired" styleId="answer-required"/>
						<fmt:message key="label.authoring.answer.required" />
					</label>
				</div>
				
				<div class="checkbox">
					<label for="allow-rich-editor">
						<html:checkbox property="allowRichEditor" styleId="allow-rich-editor"/>
						<fmt:message key="label.authoring.basic.allow.learners.rich.editor" />
					</label>
				</div>
				
				<div class="checkbox">
				    <label for="max-words-limit-checkbox">
						<input type="checkbox" id="max-words-limit-checkbox" name="noname"
								<c:if test="${formBean.maxWordsLimit != 0}">checked="checked"</c:if>/>
				    	<fmt:message key="label.maximum.number.words" />
				    </label>
				    <html:text property="maxWordsLimit" styleId="max-words-limit"/>
				</div>
				
				<div class="checkbox">
				    <label for="min-words-limit-checkbox">
						<input type="checkbox" id="min-words-limit-checkbox" name="noname"
								<c:if test="${formBean.minWordsLimit != 0}">checked="checked"</c:if>/>
				    	<fmt:message key="label.minimum.number.words" />
				    </label>
				    <html:text property="minWordsLimit" styleId="min-words-limit"/>
				</div>

				<div class="generalFeedback">
				  <a data-toggle="collapse" data-target="#general-feedback"><i class="fa fa-plus-square-o roffset5" aria-hidden="true"></i><fmt:message key="label.authoring.basic.general.feedback" /></a>
					<div id="general-feedback"  class="collapse form-group">
						<lams:CKEditor id="generalFeedback" value="${formBean.generalFeedback}" contentFolderID="${formBean.contentFolderID}" />
					</div>
				</div>
					
			</html:form>
			
			<div class="voffset10 pull-right">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" />
				</a>
				<a href="#nogo" onclick="javascript:$('#assessmentQuestionForm').submit();" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="label.authoring.essay.add.essay" />
				</a>
				
			</div>

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
	</div>	
</body>
</lams:html>
