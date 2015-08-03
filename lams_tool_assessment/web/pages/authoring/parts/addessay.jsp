<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<link href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" rel="stylesheet" type="text/css" >
		<%@ include file="/common/header.jsp"%>
		<link href="<html:rewrite page='/includes/css/addQuestion.css'/>" rel="stylesheet" type="text/css">
		
		<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
		
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
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
				<html:hidden property="questionType" value="6"/>
				<html:hidden property="questionIndex" />
	
				<h2 class="no-space-left">
					<fmt:message key="label.authoring.basic.type.essay" />
				</h2>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.name" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="title" />
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.text" />
				</div>
				<lams:CKEditor id="question" value="${formBean.question}" contentFolderID="${formBean.contentFolderID}">
				</lams:CKEditor>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.default.question.grade" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="defaultGrade" styleClass="shortInputText"/>
				
				<div class="field-name space-top" >
					<html:checkbox property="answerRequired" styleId="answer-required"/>
					<label for="answer-required">
						<fmt:message key="label.authoring.answer.required" />
					</label>
				</div>
				
				<div class="field-name space-top" >
					<html:checkbox property="allowRichEditor" styleId="allow-rich-editor"/>
					<label for="allow-rich-editor">
						<fmt:message key="label.authoring.basic.allow.learners.rich.editor" />
					</label>
				</div>
				
				<div class="field-name space-top" >
					<input type="checkbox" id="max-words-limit-checkbox" name="noname"
						<c:if test="${formBean.maxWordsLimit != 0}">checked="checked"</c:if>
					/>
					<label for="max-words-limit-checkbox" class="word-limit-label">
						<fmt:message key="label.maximum.number.words" />
					</label>
					<html:text property="maxWordsLimit" styleId="max-words-limit"/>
				</div>
				
				<div class="field-name space-top" >
					<input type="checkbox" id="min-words-limit-checkbox" name="noname"
						<c:if test="${formBean.minWordsLimit != 0}">checked="checked"</c:if>
					/>
					<label for="min-words-limit-checkbox" class="word-limit-label">
						<fmt:message key="label.minimum.number.words" />
					</label>
					<html:text property="minWordsLimit" styleId="min-words-limit"/>
				</div>
					
				<div class="field-name space-top">
					<img src="<lams:LAMSURL/>/images/tree_closed.gif" onclick="javascript:toggleVisibility('general-feedback');" />

					<a href="javascript:toggleVisibility('general-feedback');" >
						<fmt:message key="label.authoring.basic.general.feedback" />
					</a>
				</div>
				
				<div id="general-feedback" class="hidden">
					<lams:CKEditor id="generalFeedback" value="${formBean.generalFeedback}" contentFolderID="${formBean.contentFolderID}">
					</lams:CKEditor>
				</div>

			</html:form>

			<br><br>
			<lams:ImgButtonWrapper>
				<a href="#" onclick="$('#assessmentQuestionForm').submit();" onmousedown="self.focus();" class="button-add-item">
					<fmt:message key="label.authoring.essay.add.essay" /> 
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
