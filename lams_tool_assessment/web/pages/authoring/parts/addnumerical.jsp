<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<link href="<html:rewrite page='/includes/css/addQuestion.css'/>" rel="stylesheet" type="text/css">
		
		<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
		<c:set var="questionType" value="4"	scope="request" />

		<script type="text/javascript">
			var questionType = ${questionType};
			var addOptionUrl = "<c:url value='/authoring/addOption.do'/>";
		   	var removeOptionUrl = "<c:url value='/authoring/removeOption.do'/>";
    	    var upOptionUrl = "<c:url value='/authoring/upOption.do'/>";
    	    var downOptionUrl = "<c:url value='/authoring/downOption.do'/>";
    	    
    		function addUnit(){
    			var url= "<c:url value='/authoring/newUnit.do'/>";
    			var unitList = $("#unitForm").serialize(true);
    			$("#unitArea").load(
    				url,
    				{
    					questionType: questionType,
    					unitList: unitList 
    				}
    			);
    		}
		</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/assessmentoption.js'/>"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.pack.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
  	    <script><!--
			$(document).ready(function(){
				var optionValidator = $("#optionForm").validate();				
				var unitValidator = $("#unitForm").validate();
				
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
		    		        	return $("input[name^=optionFloat][value!='0.0']").length < 1;
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
	    			    },
	    			    allAcceptedErrorsPositive: {
		    				required: function(element) {
		    					var count = 0;
		    					$("input[name^=optionAcceptedError]").each(function(){
			    					if (eval(this.value) < 0) {
				    					count++;
			    					}
		    					});
	    		        		return  count > 0;
			    		    }			    		    
	    			    },	    			    
		    			unitList: {
		    				required: function(element) {
	    		        		return ! $("#unitForm").valid();
		    		    	}
    			    	},
		    			optionList: {
		    				required: function(element) {
	    		        		return ! $("#optionForm").valid();
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
		    			},		
		    			allAcceptedErrorsPositive: {
		    				required: "<br><fmt:message key='error.form.validation.positive.accepted.errors'/>"
		    			}, 
		    			unitList: {
		    				required: ""
		    			},
		    			optionList: {
		    				required: ""
		    			}
		    		},
		    	    invalidHandler: function(form, validator) {
		    		      var errors = validator.numberOfInvalids();
		    		      if (errors) {
			    		      if (optionValidator.numberOfInvalids()) {
			    		    	  errors += optionValidator.numberOfInvalids() - 1;
			    		      }
			    		      if (unitValidator.numberOfInvalids()) {
			    		    	  errors += unitValidator.numberOfInvalids() - 1;
			    		      }
			    		      
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
		    			$("#unitList").val($("#unitForm").serialize(true));
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
				<input type="hidden" name="unitList" id="unitList" />
				<html:hidden property="questionIndex" />
				<html:hidden property="contentFolderID" styleId="contentFolderID"/>				
	
				<h2 class="no-space-left">
					<fmt:message key="label.authoring.numerical.question" />
				</h2>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.name" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="title"/>
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.text" />
				</div>
				<lams:CKEditor id="question" value="${formBean.question}"
					contentFolderID="${formBean.contentFolderID}">
				</lams:CKEditor>
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.default.question.grade" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="defaultGrade" styleClass="shortInputText"/>
					
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.penalty.factor" />
					<img title="Required field" alt="Required field" src="${ctxPath}/includes/images/req.gif" />
				</div>
				<html:text property="penaltyFactor" styleClass="shortInputText"/>
				
				<div class="field-name space-top" >
					<fmt:message key="label.authoring.answer.required" />
				</div>
				<html:checkbox property="answerRequired" styleClass="noBorder" />
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.general.feedback" />
				</div>
				<lams:CKEditor id="generalFeedback" value="${formBean.generalFeedback}"
					contentFolderID="${formBean.contentFolderID}">
				</lams:CKEditor>
				
				<br><br>
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.numerical.answers" />
				</div>
				<input type="hidden" name="hasOptionFilled" id="hasOptionFilled">
				<input type="hidden" name="hasOneHundredGrade" id="hasOneHundredGrade">
				<input type="hidden" name="allAcceptedErrorsPositive" id="allAcceptedErrorsPositive">
			</html:form>
			
			<!-- Options -->
			<form id="optionForm" name="optionForm">
				<%@ include file="optionlist.jsp"%>
				<a href="javascript:;" onclick="addOption();" class="button-add-item right-buttons" style="margin-right: 40px; margin-top:0px">
					<fmt:message key="label.authoring.numerical.add.answer" /> 
				</a>
			</form>
			
			<!-- Units -->
			<div class="field-name" style="margin-top: 50px;">
				<fmt:message key="label.authoring.numerical.units" />
			</div>			
			<form id="unitForm" name="unitForm">
				<%@ include file="unitlist.jsp"%>
				<a href="javascript:;" onclick="addUnit();" class="button-add-item right-buttons" style="margin-right: 40px; margin-top:0px">
					<fmt:message key="label.authoring.numerical.add.unit" /> 
				</a>
			</form>
			
			<br><br><br>
			<lams:ImgButtonWrapper>
				<a href="#" onclick="$('#assessmentQuestionForm').submit();" onmousedown="self.focus();" class="button-add-item">
					<fmt:message key="label.authoring.numerical.add.numerical" /> 
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
