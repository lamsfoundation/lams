<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<lams:css style="core" />
	<style type="text/css">
		a {
			margin: 10px 2px 0px 0px;
			float: right;
		}
		
		input {
			margin: 5px 0px 0px 10px;
		}
		
		body {
			width: 550px;
		}
				
		div.FCKdiv {
			margin-top: 10px;
		}
	</style>
	
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
  	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
  	<script type="text/javascript">
  		function createQuestion(){
  			prepareFormData();
  			$('#pedagogicalPlannerForm').ajaxSubmit({
  				url: "<c:url value='/pedagogicalPlanner.do?dispatch=createPedagogicalPlannerQuestion' />",
  				success: function(responseText){
  					$('#body').html(responseText.substring(responseText.search(/<body/i)));
  					fillForm();
  				}
  			});
  		}
  		
  		function prepareFormData(){
			//FCKeditor content is not submitted when sending by jQuery; we need to do this
			var questionIndex = 0;
			do{
				var question = document.getElementById("question["+questionIndex+"]");
				if (question!=null){
					var content = FCKeditorAPI.GetInstance("question["+questionIndex+"]").GetXHTML();
					question.value=content;
					questionIndex++;
				}
			} while (question!=null);
		}
  		

  		function fillForm(){
  			var candidateAnswersString = $("#candidateAnswersString").val();
  			if (candidateAnswersString!=null){
	  			var candidateAnswersArray = $("#candidateAnswersString").val().split("&");
	  			for (var paramIndex = 0;paramIndex<candidateAnswersArray.length;paramIndex++){
	  				var param = candidateAnswersArray[paramIndex];
	  				var paramArray = param.split("=");
	  				if (paramArray[0].indexOf("candidateAnswer")!=-1){
	  					var element = document.getElementById(paramArray[0]);
	  					if (element!=null){
	  						element.value=paramArray[1];
	  					}
	  				}
	  			}
  			}
		}
				
		$(document).ready(function(){
			fillForm();
		});
		
		
  	</script>
</lams:head>
<body id="body">
	<%@ include file="/common/messages.jsp"%>
	<h4 class="space-left"><fmt:message key="label.questions"/></h4>
	<html:form action="/pedagogicalPlanner.do?dispatch=saveOrUpdatePedagogicalPlannerForm" styleId="pedagogicalPlannerForm" method="post">
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		
		<html:hidden property="toolContentID" />
		<html:hidden property="valid" styleId="valid" />
		<html:hidden property="callID" styleId="callID" />
		<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
		<html:hidden property="candidateAnswersString" styleId="candidateAnswersString" />
		<input type="hidden" id="questionCount" value="${formBean.questionCount}" />
		
		<c:forEach var="questionIndex"  begin="1" end="${formBean.questionCount}" >
			<div class="FCKdiv">
				<lams:FCKEditor id="question[${questionIndex-1}]"
					value="${formBean.questionList[questionIndex-1]}"
					contentFolderID="${formBean.contentFolderID}"
	                toolbarSet="Custom-Pedplanner" height="150px"
	                width="545px" displayExpanded="false">
				</lams:FCKEditor>
			</div>
			<div class="space-left">
				<c:forEach var="candidateAnswerIndex" begin="1" end="${formBean.candidateAnswerCount[questionIndex-1]}" >
					${candidateAnswerIndex}.
					<input size="55" type="text" name="candidateAnswer${questionIndex}-${candidateAnswerIndex}" id="candidateAnswer${questionIndex}-${candidateAnswerIndex}" />
					<html:radio property="correct[${questionIndex-1}]" value="${candidateAnswerIndex}" />
					<br />
				</c:forEach>
				<input type="hidden" name="candidateAnswerCount${questionIndex}" value="${formBean.candidateAnswerCount[questionIndex-1]}" />
			</div>
		</c:forEach>
	</html:form>
	<a class="button" href="javascript:createQuestion();"><fmt:message key="label.addNewQuestion" /></a>
</body>
</lams:html>