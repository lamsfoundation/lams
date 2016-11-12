<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="5"	scope="request" />
		
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<link href="<html:rewrite page='/includes/css/addQuestion.css'/>" rel="stylesheet" type="text/css">

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
    		// post-submit callback 
    		function afterRatingSubmit(responseText, statusText)  { 
    			self.parent.refreshThickbox()
    			self.parent.tb_remove();
    		}    
  		</script>
		
</lams:head>
<body>
	<div class="panel panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.true.false.question" />
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
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
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
				
				<div class="checkbox">
					<label for="answer-required">
						<html:checkbox property="answerRequired" styleId="answer-required"/>
						<fmt:message key="label.authoring.answer.required" />
					</label>
				</div>				
				
				<div class="form-group form-inline">
				    <label for="defaultGrade">
				    	<fmt:message key="label.authoring.basic.default.question.grade" />:
				    	<i class="fa fa-xs fa-asterisk text-danger pull-right" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
				    </label>
				    <html:text property="defaultGrade" styleClass="form-control short-input-text input-sm"/>
				    
				    <label class="loffset10" for="penaltyFactor">
				    	<fmt:message key="label.authoring.basic.penalty.factor" />:
						<i class="fa fa-xs fa-asterisk text-danger pull-right" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
				    </label>
				    <html:text property="penaltyFactor" styleClass="form-control short-input-text input-sm"/>
				</div>
								
				<div class="generalFeedback">
				  <a data-toggle="collapse" data-target="#general-feedback"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i><fmt:message key="label.authoring.basic.general.feedback" /></a>
					<div id="general-feedback"  class="collapse <c:if test="${not empty formBean.generalFeedback}">in</c:if> form-group">
						<lams:CKEditor id="generalFeedback" value="${formBean.generalFeedback}" contentFolderID="${formBean.contentFolderID}" />
					</div>
				</div>
				
				<hr>
				
				<div class="voffset10 form-inline form-group">
					<label for="correctAnswer">
						<fmt:message key="label.authoring.true.false.correct.answer" />
					</label>
					<html:select property="correctAnswer" styleClass="form-control input-sm">
						<html:option value="false"><fmt:message key="label.authoring.true.false.false" /></html:option>
						<html:option value="true"><fmt:message key="label.authoring.true.false.true" /></html:option>
					</html:select>
				</div>
				
				<a data-toggle="collapse" data-target="#feedback"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i><fmt:message key="label.authoring.basic.option.feedback"></fmt:message></a>
				<div id="feedback" class="voffset5 collapse  <c:if test="${(not empty formBean.feedbackOnCorrect) || (not empty formBean.feedbackOnIncorrect)}">in</c:if>">
					<div class="form-group voffset10">
						<label for="feedbackOnCorrect">
							<fmt:message key="label.authoring.true.false.feedback.on.true" />
						</label>
			            	
						<lams:CKEditor id="feedbackOnCorrect" value="${formBean.feedbackOnCorrect}" contentFolderID="${formBean.contentFolderID}"/>
					</div>
					
					<div class="form-group">
						<label for="feedbackOnIncorrect">
							<fmt:message key="label.authoring.true.false.feedback.on.false" />
						</label>
			            	
						<lams:CKEditor id="feedbackOnIncorrect" value="${formBean.feedbackOnIncorrect}" contentFolderID="${formBean.contentFolderID}"/>
					</div>
				</div>
			</html:form>

			<div class="voffset10 pull-right">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" />
				</a>
				<a href="#nogo" onclick="javascript:$('#assessmentQuestionForm').submit();" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="label.authoring.save.button" />
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
