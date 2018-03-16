<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="2"	scope="request" />

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<link href="<html:rewrite page='/includes/css/addQuestion.css'/>" rel="stylesheet" type="text/css">

	<script type="text/javascript">
		var questionType = ${questionType};
		var addOptionUrl = "<c:url value='/authoring/addOption.do'/>";
	   	var removeOptionUrl = "<c:url value='/authoring/removeOption.do'/>";
   	    var upOptionUrl = "<c:url value='/authoring/upOption.do'/>";
   	    var downOptionUrl = "<c:url value='/authoring/downOption.do'/>";
	</script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/assessmentoption.js'/>"></script>
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
		    			},
		    			hasOptionFilled: {
		    				required: function(element) {
		    					prepareOptionEditorsForAjaxSubmit();	    				
		    		        	return $("textarea[name^=optionQuestion]:filled").length < 1;
			    		    }
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
		    			},
		    			hasOptionFilled: "<fmt:message key='label.authoring.matching.pairs.error.one.matching.pair'/>"
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
		    			$("#optionList").val($("#optionForm").serialize(true));
		    			$("#question").val(CKEDITOR.instances.question.getData());
		    			$("#generalFeedback").val(CKEDITOR.instances.generalFeedback.getData());
		    			
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
			<fmt:message key="label.authoring.basic.type.matching.pairs" />
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
				<input type="hidden" name="optionList" id="optionList" />
				<html:hidden property="questionIndex" />
				<html:hidden property="contentFolderID" styleId="contentFolderID"/>				
				<html:hidden property="feedbackOnCorrect" />
				<html:hidden property="feedbackOnPartiallyCorrect" />
				<html:hidden property="feedbackOnIncorrect" />
				<html:hidden property="questionIndex" />

				<div class="form-group">
				    <label for="title">
				    	<fmt:message key="label.authoring.basic.question.name"/>
				    	<i class="fa fa-xs fa-asterisk text-danger pull-right" title="<fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
				    </label>
				    <html:text property="title" styleId="title" maxlength="255" styleClass="form-control" tabindex="1"/>
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
				
										
				<div class="checkbox">
					<label for="shuffle">
						<html:checkbox property="shuffle" styleId="shuffle"/>
						<fmt:message key="label.authoring.basic.shuffle.the.choices" />
					</label>
				</div>

				<div class="generalFeedback">
				  <a data-toggle="collapse" data-target="#general-feedback" href="#general-feedback"><i class="fa fa-plus-square-o roffset5" aria-hidden="true"></i><fmt:message key="label.authoring.basic.general.feedback" /></a>
					<div id="general-feedback"  class="collapse form-group">
						<lams:CKEditor id="generalFeedback" value="${formBean.generalFeedback}" contentFolderID="${formBean.contentFolderID}" />
					</div>
				</div>
				
				<h5 class="voffset20">
					<fmt:message key="label.authoring.matching.pairs.matching.pairs" />
				</h5>
				<input type="text" name="hasOptionFilled" id="hasOptionFilled" class="fake-validation-input">
				<label for="hasOptionFilled" class="error" style="display: none;"></label>
			</html:form>
			
			<!-- Options -->
			<form id="optionForm" name="optionForm">
				<%@ include file="optionlist.jsp"%>
				
				<a href="#nogo" onclick="javascript:addOption();" class="btn btn-xs btn-default button-add-item pull-right">
					<fmt:message key="label.authoring.matching.pairs.add.matching.pair" /> 
				</a>
			</form>

			<div class="voffset10 pull-right clear-both">
			<a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" />
				</a>
				<a href="#nogo" onclick="javascript:$('#assessmentQuestionForm').submit();" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="label.authoring.matching.pairs.add.matching.pairs" />
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
