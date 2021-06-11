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
		
		table#topicTable td {
			margin: 0px;
			padding: 0px;
		}
		
		table#topicTable td.FCKcell {
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
  		function createTopic(){
  			prepareFormData();
  			$('#plannerForm').ajaxSubmit({
  				url: "<c:url value='/authoring/createPedagogicalPlannerTopic.do' />",
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
			
			var instructions = CKEDITOR.instances.instructions.getData();
			document.getElementById("instructions").value=instructions;
			
			var topicIndex = 0;
			do{
				var topic = document.getElementById("topic["+topicIndex+"]");
				if (topic!=null){
					var content = CKEDITOR.instances["topic["+topicIndex+"]"].getData();
					topic.value=content;
					topicIndex++;
				}
			} while (topic!=null);
		}
		
		function clearEntry(topicIndex){
			CKEDITOR.instances["topic["+topicIndex+"]"].setData("");
		}
  	</script>
</lams:head>
<body id="body">
	<lams:errors/>
	
	<form:form action="saveOrUpdatePedagogicalPlannerForm.do" id="plannerForm" modelAttribute="plannerForm" method="post">
	
		<form:hidden path="toolContentID" id="toolContentID" />
		<form:hidden path="valid" id="valid" />
		<form:hidden path="callID" id="callID" />
		<form:hidden path="activityOrderNumber" id="activityOrderNumber" />
		
		<h4 class="space-left"><fmt:message key="label.instructions"/></h4>
		<lams:CKEditor id="instructions"
			value="${plannerForm.instructions}"
			contentFolderID="${plannerForm.contentFolderID}"
               toolbarSet="CustomPedplanner" height="150px"
               width="${param.plannerCKEditorLongWidth}" displayExpanded="false">
		</lams:CKEditor>
		
		<h4 class="space-left small-space-top"><fmt:message key="lable.topic.title.subject"/></h4>
		<table id="topicTable" cellpadding="0" cellspacing="0">
			<c:forEach var="topicIndex" begin="1" end="${plannerForm.topicCount}">
				<tr>
					<td class="FCKcell">
						<lams:CKEditor id="topic[${topicIndex-1}]"
							value="${plannerForm.topicList[topicIndex-1]}"
							contentFolderID="${plannerForm.contentFolderID}"
			                toolbarSet="CustomPedplanner" height="150px"
			                width="${param.plannerCKEditorShortWidth}" displayExpanded="false">
						</lams:CKEditor>
					</td>
					<td>
						<img class="clearEntry" src="<lams:LAMSURL/>images/cross.png"
							title="<fmt:message key="msg.planner.clear.entry" />"
							onclick="javascript:clearEntry(${topicIndex-1})" />
					</td>
				</tr>
			</c:forEach>
		</table>
	</form:form>
	<a class="button" href="javascript:createTopic();"><fmt:message key="label.authoring.create.new.topic" /></a>
</body>
</lams:html>