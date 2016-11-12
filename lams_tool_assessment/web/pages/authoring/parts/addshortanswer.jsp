<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="questionType" value="3"	scope="request" />

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
		    		        	return $("input[name^=optionString]:filled").length < 1;
			    		    }			    		    
	    			    },
	    			    hasOneHundredGrade: {
		    				required: function(element) {
		    					var hasOneHundredGrade = false;
		    					$("select[name^='optionGrade']").each(function() {
		    						hasOneHundredGrade = hasOneHundredGrade || (this.value == '1.0');
		    					});
	    			    		return !hasOneHundredGrade;
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
		    			hasOptionFilled: "<fmt:message key='label.authoring.numerical.error.answer'/>",
		    			hasOneHundredGrade: "<fmt:message key='error.form.validation.hundred.score'/>"
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
     			    	prepareOptionEditorsForAjaxSubmit();     			    
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
			<fmt:message key="label.authoring.basic.type.short.answer" />
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
				
				<div class="form-group">
				    <label for="title">
				    	<fmt:message key="label.authoring.basic.question.name"/>
				    	<i class="fa fa-xs fa-asterisk text-danger pull-right" title="* <fmt:message key="label.required.field"/>" alt="<fmt:message key="label.required.field"/>"></i>
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
																	
				<div class="form-inline form-group">
					<label for="caseSensitive">
						<fmt:message key="label.authoring.short.answer.case.sensitivity" />:
					</label>
					<html:select property="caseSensitive" styleClass="form-control input-sm">
						<html:option value="false"><fmt:message key="label.authoring.short.answer.no.case.unimportant" /></html:option>
						<html:option value="true"><fmt:message key="label.authoring.short.answer.yes.case.must.match" /></html:option>
					</html:select>
				</div>

				<div class="generalFeedback">
				  <a data-toggle="collapse" data-target="#general-feedback"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i><fmt:message key="label.authoring.basic.general.feedback" /></a>
					<div id="general-feedback"  class="voffset5 collapse <c:if test="${not empty formBean.generalFeedback}">in</c:if> form-group">
						<lams:CKEditor id="generalFeedback" value="${formBean.generalFeedback}" contentFolderID="${formBean.contentFolderID}" />
					</div>
				</div>
				
				<h5 class="voffset20">
					<fmt:message key="label.authoring.short.answer.answers" />
				</h5>
				<input type="text" name="hasOptionFilled" id="hasOptionFilled" class="fake-validation-input">
				<input type="text" name="hasOneHundredGrade" id="hasOneHundredGrade" class="fake-validation-input">
			</html:form>
			
			<!-- Options -->
			<form id="optionForm" name="optionForm">
				<%@ include file="optionlist.jsp"%>
				
				<a href="#nogo" onclick="javascript:addOption();" class="btn btn-xs btn-default button-add-item pull-right">
					<fmt:message key="label.authoring.short.answer.add.answer" />  
				</a>
			</form>

			<div class="voffset10 pull-right clear-both">
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
