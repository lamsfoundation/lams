<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<link href="<html:rewrite page='/includes/css/addQuestion.css'/>" rel="stylesheet" type="text/css">
		
		<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
		<c:set var="questionType" value="1"	scope="request" />

		<script type="text/javascript">
			var questionType = ${questionType};
			var addOptionUrl = "<c:url value='/authoring/addOption.do'/>";
		   	var removeOptionUrl = "<c:url value='/authoring/removeOption.do'/>";
    	    var upOptionUrl = "<c:url value='/authoring/upOption.do'/>";
    	    var downOptionUrl = "<c:url value='/authoring/downOption.do'/>";
		</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/assessmentoption.js'/>"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
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
		    		        	return $("textarea[name^=optionString]:filled").length < 1;
			    		    }			    		    
	    			    },
	    			    hasOneHundredGrade: {
		    				required: function(element) {
		    					var hasOneHundredGrade = false;
		    					$("select[name^='optionGrade']").each(function() {
		    						hasOneHundredGrade = hasOneHundredGrade || (this.value == '1.0');
		    					});
	    			    		return !hasOneHundredGrade && !eval($("#multipleAnswersAllowed").val());
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
		    			hasOptionFilled: {
		    				required: "<fmt:message key='label.authoring.numerical.error.answer'/>"
		    			},
		    			hasOneHundredGrade: {
		    				required: "<fmt:message key='error.form.validation.hundred.score'/>"
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
     			    submitHandler: function(form) {
     			    	prepareOptionEditorsForAjaxSubmit();
		    			$("#optionList").val($("#optionForm").serialize(true));
		    			$("#question").val(CKEDITOR.instances.question.getData());
		    			$("#generalFeedback").val(CKEDITOR.instances.generalFeedback.getData());
		    			$("#feedbackOnCorrect").val(CKEDITOR.instances.feedbackOnCorrectOutsideForm.getData());
		    			$("#feedbackOnPartiallyCorrect").val(CKEDITOR.instances.feedbackOnPartiallyCorrectOutsideForm.getData());
		    			$("#feedbackOnIncorrect").val(CKEDITOR.instances.feedbackOnIncorrectOutsideForm.getData());
		    			
		    	    	var options = { 
		    	    		target:  parent.jQuery('#questionListArea'), 
		    		   		success: afterRatingSubmit  // post-submit callback
		    		    }; 				
		    		    				
		    			$('#assessmentQuestionForm').ajaxSubmit(options);
		    		}
		  		});
		    	
				$("#multipleAnswersAllowed").on('change', function() {
					$("#incorrect-answer-nullifies-mark-area").toggle(eval($(this).val()));
				}).trigger("change");

			});
    		// post-submit callback 
    		function afterRatingSubmit(responseText, statusText)  { 
    			self.parent.refreshThickbox()
    			self.parent.tb_remove();
    		}    
  		</script>
		
	</lams:head>
	
	<body class="stripes" onload="parent.resizeIframe();">
		<div id="content" >	
			<%@ include file="/common/messages.jsp"%>
		    <div class="error" style="display:none;">
		      	<img src="${ctxPath}/includes/images/warning.gif" alt="Warning!" width="24" height="24" style="float:left; margin: -5px 10px 0px 0px; " />
		      	<span></span>.<br clear="all"/>
		    </div>			
			
			<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="assessmentQuestionForm">
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<html:hidden property="sessionMapID" />
				<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
				<input type="hidden" name="optionList" id="optionList" />
				<html:hidden property="questionIndex" />
				<html:hidden property="contentFolderID" styleId="contentFolderID"/>
				<html:hidden property="feedbackOnCorrect" styleId="feedbackOnCorrect"/>
				<html:hidden property="feedbackOnPartiallyCorrect" styleId="feedbackOnPartiallyCorrect"/>
				<html:hidden property="feedbackOnIncorrect" styleId="feedbackOnIncorrect"/>
	
				<h2 class="no-space-left">
					<fmt:message key="label.authoring.basic.type.multiple.choice" />
				</h2>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.name" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="title" />
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.text" />
				</div>
				<lams:CKEditor id="question" value="${formBean.question}"
					contentFolderID="${formBean.contentFolderID}" >
				</lams:CKEditor>
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.default.question.grade" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="defaultGrade" styleClass="shortInputText" />
					
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.penalty.factor" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="penaltyFactor" styleClass="shortInputText" />
				
				<div class="field-name space-top" >
					<html:checkbox property="answerRequired" styleId="answer-required"/>
					<label for="answer-required">
						<fmt:message key="label.authoring.answer.required" />
					</label>
				</div>
				
				<div class="field-name space-top">
					<img src="<lams:LAMSURL/>/images/tree_closed.gif" onclick="javascript:toggleVisibility('general-feedback');" />

					<a href="javascript:toggleVisibility('general-feedback');" >
						<fmt:message key="label.authoring.basic.general.feedback" />
					</a>
				</div>
				<div id="general-feedback" class="hidden">
					<lams:CKEditor id="generalFeedback" value="${formBean.generalFeedback}"
						contentFolderID="${formBean.contentFolderID}"> 
					</lams:CKEditor>
				</div>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.choice.one.multiple.answers" />
				</div>
				<html:select property="multipleAnswersAllowed" styleId="multipleAnswersAllowed">
					<html:option value="false"><fmt:message key="label.authoring.choice.one.answer" /></html:option>
					<html:option value="true"><fmt:message key="label.authoring.choice.multiple.answers" /></html:option>
				</html:select>
				
				<div class="field-name space-top" id="incorrect-answer-nullifies-mark-area">
					<html:checkbox property="incorrectAnswerNullifiesMark" styleId="incorrect-answer-nullifies-mark"/>
					<label for="incorrect-answer-nullifies-mark">
						<fmt:message key="label.incorrect.answer.nullifies.mark" />
					</label>
				</div>				
	
				<div class="field-name space-top" >
					<html:checkbox property="shuffle" styleId="shuffle"/>
					<label for="shuffle">
						<fmt:message key="label.authoring.basic.shuffle.the.choices" />
					</label>
				</div>
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.answer.options" />
				</div>
				<input type="text" name="hasOptionFilled" id="hasOptionFilled" class="fake-validation-input">
				<input type="text" name="hasOneHundredGrade" id="hasOneHundredGrade" class="fake-validation-input">
			</html:form>
			
			<!-- Options -->
			<form id="optionForm" name="optionForm">
				<%@ include file="optionlist.jsp"%>
				<a href="javascript:;" onclick="addOption();" class="button-add-item right-buttons" style="margin-right: 40px; margin-top:0px">
					<fmt:message key="label.authoring.choice.add.option" /> 
				</a>
			</form>
			
			<!-- Overall feedback -->
			<div class="field-name " style="margin-top: 60px;">
				<img src="<lams:LAMSURL/>/images/tree_closed.gif" onclick="javascript:toggleVisibility('overall-feedback');" />

				<a href="javascript:toggleVisibility('overall-feedback');" >
					<fmt:message key="label.authoring.choice.overall.feedback" />
				</a>
			</div>
			<div id="overall-feedback" class="hidden">
				<div class="field-name" >
					<fmt:message key="label.authoring.choice.feedback.on.correct" />
				</div>
				<lams:CKEditor id="feedbackOnCorrectOutsideForm" value="${formBean.feedbackOnCorrect}"
					contentFolderID="${formBean.contentFolderID}">
				</lams:CKEditor>
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.choice.feedback.on.partially.correct" />
				</div>
				<lams:CKEditor id="feedbackOnPartiallyCorrectOutsideForm" value="${formBean.feedbackOnPartiallyCorrect}"
					contentFolderID="${formBean.contentFolderID}">
				</lams:CKEditor>
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.choice.feedback.on.incorrect" />
				</div>
				<lams:CKEditor id="feedbackOnIncorrectOutsideForm" value="${formBean.feedbackOnIncorrect}"
					contentFolderID="${formBean.contentFolderID}">
				</lams:CKEditor>
			</div>
			<br><br>
			
			<lams:ImgButtonWrapper>
				<a href="#" onclick="$('#assessmentQuestionForm').submit();" onmousedown="self.focus();" class="button-add-item">
					<fmt:message key="label.authoring.choice.add.multiple.choice" /> 
				</a>
				<a href="#" onclick="self.parent.tb_remove();" onmousedown="self.focus();" class="button space-left">
					<fmt:message key="label.cancel" /> 
				</a>
			</lams:ImgButtonWrapper>

		</div>
		<!--closes content-->
	
		<div id="footer">
		</div>
		<!--closes footer-->		
		
	</body>
</lams:html>
