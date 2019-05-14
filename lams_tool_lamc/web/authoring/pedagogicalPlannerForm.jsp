<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<lams:css/>
	<style type="text/css">
		a {
			margin: 10px 2px 0px 0px;
			float: right;
		}
		
		input {
			margin: 5px 0px 0px 10px;
		}
		
		body {
			width: ${param.plannerCKEditorLongWidth};
		}
				
		table#questionTable td {
			margin: 0px;
			padding: 0px;
		}
		
		table#questionTable td.FCKcell {
			padding: 10px 0px 0px 0px;
		}
		
		img.clearEntry {
			maring: 0px;
			padding: 0px;
			cursor: pointer;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
  	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
  	<script type="text/javascript">
  		function createQuestion(){
  			prepareFormData();
  			$('#pedagogicalPlannerForm').ajaxSubmit({
  				url: "<c:url value='/pedagogicalPlanner/createPedagogicalPlannerQuestion.do' />",
  				data: { 
					sessionMapId: '${sessionMapId}'
				},
  				success: function(responseText){
 	  				var bodyTag = '<body';
 	  				var selectedBody = responseText.substring(responseText.search(bodyTag) + 1);
 	  				selectedBody = selectedBody.substring(selectedBody.search(bodyTag));
  					$('#body').html(selectedBody);
  					fillForm();
  				}
  			});
  		}
  		
  		function prepareFormData(){
			//CKeditor content is not submitted when sending by jQuery; we need to do this
			var questionIndex = 0;
			do{
				var description = document.getElementById("description["+questionIndex+"]");
				if (description!=null){
					var content = CKEDITOR.instances["description["+questionIndex+"]"].getData();
					description.value=content;
					questionIndex++;
				}
			} while (description!=null);
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
		
		function clearEntry(questionIndex){
			CKEDITOR.instances["description["+questionIndex+"]"].setData("");
		}
				
		$(document).ready(function(){
			fillForm();
		});
  	</script>
</lams:head>
<body id="body">
	<lams:errors/>
	<h4 class="space-left"><fmt:message key="label.questions"/></h4>
	<form:form action="saveOrUpdatePedagogicalPlannerForm.do" id="plannerForm" modelAttribute="plannerForm" method="post">
		
		<form:hidden path="toolContentID" id="toolContentID" />
		<form:hidden path="valid" id="valid" />
		<form:hidden path="callID" id="callID" />
		<form:hidden path="activityOrderNumber" id="activityOrderNumber" />
		<form:hidden path="candidateAnswersString" id="candidateAnswersString" />
		<input type="hidden" id="questionCount" value="${plannerForm.questionCount}" />
		
		<table id="questionTable" cellpadding="0" cellspacing="0">
			<c:forEach var="questionIndex"  begin="1" end="${plannerForm.questionCount}">
			
				<tr>
					<td class="FCKcell">
						<lams:CKEditor id="description[${questionIndex-1}]"
							value="${plannerForm.descriptionList[questionIndex-1]}"
							contentFolderID="${sessionMap.contentFolderID}"
			                toolbarSet="CustomPedplanner" height="150px"
			                width="${param.plannerCKEditorShortWidth}" displayExpanded="false">
						</lams:CKEditor>
					</td>
					<td>
						<img class="clearEntry" src="<lams:LAMSURL/>images/icons/cross.png"
							title="<fmt:message key="msg.planner.clear.entry" />"
							onclick="javascript:clearEntry(${questionIndex-1})" />
					</td>
				</tr>
				
				<tr>
					<td class="space-left" colspan="2">
						<c:forEach var="candidateAnswerIndex" begin="1" end="${plannerForm.candidateAnswerCount[questionIndex-1]}" >
							${candidateAnswerIndex}.
							<input size=90" type="text" name="candidateAnswer${questionIndex}-${candidateAnswerIndex}" id="candidateAnswer${questionIndex}-${candidateAnswerIndex}" />
							<form:radiobutton path="correct[${questionIndex-1}]" value="${candidateAnswerIndex}" />
							<br />
						</c:forEach>
						<input type="hidden" name="candidateAnswerCount${questionIndex}" value="${plannerForm.candidateAnswerCount[questionIndex-1]}" />
					</td>
				</tr>
				
			</c:forEach>
		</table>
	</form:form>
	
	<a class="button" href="javascript:createQuestion();">
		<fmt:message key="label.addNewQuestion" />
	</a>
	
</body>
</lams:html>
