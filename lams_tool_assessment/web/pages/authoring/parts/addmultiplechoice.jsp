<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
		<c:set var="questionType" value="1"	scope="request" />
		
		<style type="text/css">
			label { width: 10em; float: left; }
			label.error { float: none; color: red; padding-left: .5em; vertical-align: top; font-weight: bold; font-style: italic;}
			em { font-weight: bold; padding-right: 1em; vertical-align: top; }
			input.error { border: 2px solid red;}
		</style>

		<script type="text/javascript">
			var questionType = ${questionType};
			var addOptionUrl = "<c:url value='/authoring/addOption.do'/>";
		   	var removeOptionUrl = "<c:url value='/authoring/removeOption.do'/>";
    	    var upOptionUrl = "<c:url value='/authoring/upOption.do'/>";
    	    var downOptionUrl = "<c:url value='/authoring/downOption.do'/>";
		</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/assessmentoption.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery-1.2.6.pack.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.validate.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.form.js'/>"></script>
  	    <script><!--
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
				    			$("[name^=optionString]").each(function() {
									this.value = FCKeditorAPI.GetInstance(this.name).GetXHTML();
				    			});		    				
		    		        	return $("input[name^=optionString]:filled").length < 1;
			    		    }			    		    
	    			    },
	    			    hasOneHundredGrade: {
		    				required: function(element) {
	    			    		return ($("select[name^='optionGrade'][value='1.0']").length < 1) && !eval($("#multipleAnswersAllowed").val());
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
		    				required: "<br><fmt:message key='error.form.validation.hundred.score'/>"
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
		    			$("[name^=optionString]").each(function() {
							this.value = FCKeditorAPI.GetInstance(this.name).GetXHTML();
		    			});
		    			$("#optionList").val($("#optionForm").serialize(true));
		    			$("#question").val(FCKeditorAPI.GetInstance("question").GetXHTML());
		    			$("#generalFeedback").val(FCKeditorAPI.GetInstance("generalFeedback").GetXHTML());
		    			$("#feedbackOnCorrect").val(FCKeditorAPI.GetInstance("feedbackOnCorrectOutsideForm").GetXHTML());
		    			$("#feedbackOnPartiallyCorrect").val(FCKeditorAPI.GetInstance("feedbackOnPartiallyCorrectOutsideForm").GetXHTML());
		    			$("#feedbackOnIncorrect").val(FCKeditorAPI.GetInstance("feedbackOnIncorrectOutsideForm").GetXHTML());
		    			
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
  		--></script>
		
		
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
				<html:text property="title" size="55" />
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.text" />
				</div>
				<lams:FCKEditor id="question" value="${formBean.question}"
					contentFolderID="${formBean.contentFolderID}" width="622px">
				</lams:FCKEditor>
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.default.question.grade" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="defaultGrade" size="3" />
					
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.penalty.factor" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="penaltyFactor" size="3" />
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.general.feedback" />
				</div>
				<lams:FCKEditor id="generalFeedback" value="${formBean.generalFeedback}"
					contentFolderID="${formBean.contentFolderID}" width="622px"> 
				</lams:FCKEditor>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.choice.one.multiple.answers" />
				</div>
				<html:select property="multipleAnswersAllowed" styleId="multipleAnswersAllowed">
					<html:option value="false"><fmt:message key="label.authoring.choice.one.answer" /></html:option>
					<html:option value="true"><fmt:message key="label.authoring.choice.multiple.answers" /></html:option>
				</html:select>
	
				<div class="field-name space-top" >
					<fmt:message key="label.authoring.basic.shuffle.the.choices" />
				</div>
				<html:checkbox property="shuffle" styleId="shuffleTheChoices" styleClass="noBorder" />
				
				<br><br>
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.answer.options" />
				</div>
				<input type="hidden" name="hasOptionFilled" id="hasOptionFilled">
				<input type="hidden" name="hasOneHundredGrade" id="hasOneHundredGrade">
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
				<fmt:message key="label.authoring.choice.overall.feedback" />
			</div>
			<div style="margin-left: 30px; margin-top: 10px;">
				<div class="field-name" >
					<fmt:message key="label.authoring.choice.feedback.on.correct" />
				</div>
				<lams:FCKEditor id="feedbackOnCorrectOutsideForm" value="${formBean.feedbackOnCorrect}"
					contentFolderID="${formBean.contentFolderID}" width="622px">
				</lams:FCKEditor>
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.choice.feedback.on.partially.correct" />
				</div>
				<lams:FCKEditor id="feedbackOnPartiallyCorrectOutsideForm" value="${formBean.feedbackOnPartiallyCorrect}"
					contentFolderID="${formBean.contentFolderID}" width="622px">
				</lams:FCKEditor>
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.choice.feedback.on.incorrect" />
				</div>
				<lams:FCKEditor id="feedbackOnIncorrectOutsideForm" value="${formBean.feedbackOnIncorrect}"
					contentFolderID="${formBean.contentFolderID}" width="622px">
				</lams:FCKEditor>
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
