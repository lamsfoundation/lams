<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>

		<script type="text/javascript">
			var addOptionUrl = "<c:url value='/authoring/newOption.do'/>";
		   	var removeOptionUrl = "<c:url value='/authoring/removeOption.do'/>";
    	    var upOptionUrl = "<c:url value='/authoring/upOption.do'/>";
    	    var downOptionUrl = "<c:url value='/authoring/downOption.do'/>";

    		function submitAssessmentQuestion(){
    			$("#optionList").val($("#optionForm").serialize(true));
    			//$("#assessmentQuestionForm").submit();
    	    	var options = { 
    	    		target:  parent.jQuery('#questionListArea'), 
    		   		success: afterRatingSubmit  // post-submit callback
    		    }; 				
    		    				
    			$('#assessmentQuestionForm').ajaxSubmit(options);
    		}
    		
    		// post-submit callback 
    		function afterRatingSubmit(responseText, statusText)  { 
    			self.parent.refreshThickbox()
    			self.parent.tb_remove();
    		}    	    
		</script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/assessmentquestion.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery-1.2.6.pack.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.validate.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/jquery.form.js'/>"></script>
		<style type="text/css">
			label { width: 10em; float: left; }
			label.error { float: none; color: red; padding-left: .5em; vertical-align: top; }
			.submit { margin-left: 12em; }
			em { font-weight: bold; padding-right: 1em; vertical-align: top; }
		</style>
  	    <script>
			$(document).ready(function(){
		    	$("#assessmentQuestionForm").validate();
		  	});
  		</script>
		
		
	</lams:head>
	
	<body class="stripes">
		<div id="content" >	
			<%@ include file="/common/messages.jsp"%>
			
			<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="assessmentQuestionForm">
					<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<html:hidden property="sessionMapID" />
				<input type="hidden" name="optionList" id="optionList" />
				<input type="hidden" name="questionType" id="questionType" value="1" />
				<html:hidden property="questionIndex" />
	
				<h2 class="no-space-left">
					<fmt:message key="label.authoring.basic.type.choice" />
				</h2>
	
				<div class="field-name">
					<fmt:message key="label.authoring.basic.question.name" />
				</div>
				<em>*</em><input name="title" size="55" value="" type="text" class="{validate:{required:true, email:true, messages:{required:'Please enter your email address', email:'Please enter a valid email address'}}}">
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.question.text" />
				</div>
				<em>*</em><lams:STRUTS-textarea rows="5" cols="50" property="question" />					
	
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.default.question.grade" />
				</div>
				<input class="required" name="defaultGrade" size="3" type="text">
					
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.penalty.factor" />
				</div>
				<html:text property="penaltyFactor" size="3" />
				
				<div class="field-name space-top">
					<fmt:message key="label.authoring.basic.general.feedback" />
				</div>
				<lams:STRUTS-textarea rows="5" cols="50" property="generalFeedback"/>
	
				<div class="space-top">
					<html:checkbox property="shuffle"
						styleId="shuffleTheChoices" styleClass="noBorder">
					</html:checkbox>
					<label for="shuffleTheChoices">
						<fmt:message key="label.authoring.basic.shuffle.the.choices" />
					</label>
				</div>
			</html:form>
			
			
	
			<!-- Options -->
			<%@ include file="optionlist.jsp"%>
			
			<br /><br /><br />
			<lams:ImgButtonWrapper>
				<a href="#" onclick="submitAssessmentQuestion();" class="button-add-item">
					<fmt:message key="label.authoring.basic.add.url" /> 
				</a>
				<a href="#" onclick="self.parent.tb_remove();" class="button space-left">
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
