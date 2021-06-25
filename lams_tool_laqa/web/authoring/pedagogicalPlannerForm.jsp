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
		
		body {
			 width: ${param.plannerCKEditorLongWidth};
		}
			
		table#questionTable td {
			margin: 0px;
			padding: 0px;
		}
		
		table#questionTable td.FCKcell {
			padding: 10px 0px 0px 5px;
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
  				url: "<c:url value='createPedagogicalPlannerQuestion.do' />",
  				success: function(responseText){
 	  				var bodyTag = '<body';
 	  				var selectedBody = responseText.substring(responseText.search(bodyTag) + 1);
 	  				selectedBody = selectedBody.substring(selectedBody.search(bodyTag));
  					$('#body').html(selectedBody);
  				}
  			});
  		}
  		
  		function prepareFormData(){
			//CKeditor content is not submitted when sending by jQuery; we need to do this
			var questionIndex = 0;
			do{
				var question = document.getElementById("question["+questionIndex+"]");
				if (question!=null){
					var content = CKEDITOR.instances["question["+questionIndex+"]"].getData();
					question.value=content;
					questionIndex++;
				}
			} while (question!=null);
		}
		
		function clearEntry(questionIndex){
			CKEDITOR.instances["question["+questionIndex+"]"].setData("");
		}
  	</script>
</lams:head>
<body id="body">
	<h4 class="space-left"><fmt:message key="label.questions"/></h4>
	<form:form action="lams/tool/laqa11/pedagogicalPlanner/saveOrUpdatePedagogicalPlannerForm.do" modelAttribute="pedagogicalPlannerForm" id="pedagogicalPlannerForm" method="post">
		<lams:errors/>
		<form:hidden path="toolContentID" id="toolContentID" />
		<form:hidden path="valid" id="valid" />
		<form:hidden path="callID" id="callID" />
		<form:hidden path="activityOrderNumber" id="activityOrderNumber" />
		
		<table id="questionTable" cellpadding="0" cellspacing="0">
			<c:forEach var="questionIndex"  begin="1" end="${pedagogicalPlannerForm.questionCount}">
				<tr>
					<td class="FCKcell">
						<lams:CKEditor id="question[${questionIndex-1}]"
							value="${pedagogicalPlannerForm.questionList[questionIndex-1]}"
							contentFolderID="${pedagogicalPlannerForm.contentFolderID}"
		 	   	            toolbarSet="CustomPedplanner" height="150px"
		  	              width="${param.plannerCKEditorShortWidth}" displayExpanded="false">
						</lams:CKEditor>
						</td>
					<td>
						<img class="clearEntry" src="<lams:LAMSURL/>images/cross.png"
							title="<fmt:message key="msg.planner.clear.entry" />"
							onclick="javascript:clearEntry(${questionIndex-1})" />
					</td>
				</tr>
			</c:forEach>
		</table>
	</form:form>
	<a class="button" href="javascript:createQuestion();"><fmt:message key="label.add.new.question" /></a>
</body>
</lams:html>