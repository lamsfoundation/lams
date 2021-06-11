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
		
		table#nominationTable td {
			maring: 0px;
			padding: 0px;
		}
		
		table#nominationTable td.CKcell {
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
  				url: "<c:url value='/pedagogicalPlanner/createPedagogicalPlannerQuestion.do' />",
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
			var content = CKEDITOR.instances.instructions.getData();
			document.getElementById("instructions").value=content;
			
			var nominationIndex = 0;
			do{
				var nomination = document.getElementById("nomination["+nominationIndex+"]");
				if (nomination!=null){
					var content = CKEDITOR.instances["nomination["+nominationIndex+"]"].getData();
					nomination.value=content;
					nominationIndex++;
				}
			} while (nomination!=null);
		}
		
		function clearEntry(nominationIndex){
			CKEDITOR.instances["nomination["+nominationIndex+"]"].setData("");
		}
  	</script>
</lams:head>
<body id="body">
	<lams:errors/>
	<form:form action="<lams:WebAppURL />pedagogicalPlanner/saveOrUpdatePedagogicalPlannerForm.do" modelAttribute="pedagogicalPlannerForm" method="post">
		<form:hidden path="toolContentID" />
		<form:hidden path="valid" />
		<form:hidden path="callID" />
		<form:hidden path="activityOrderNumber" />
		
		<h4 class="space-left"><fmt:message key="label.authoring.instructions"/></h4>
		<lams:CKEditor id="instructions" value="${pedagogicalPlannerForm.instructions}"
			contentFolderID="${pedagogicalPlannerForm.contentFolderID}"
			toolbarSet="CustomPedplanner" height="150px"
			width="${param.plannerCKEditorLongWidth}" displayExpanded="false">
		</lams:CKEditor>

		<c:if test="${pedagogicalPlannerForm.nominationCount ne 0}">
			<h4 class="space-left space-top"><fmt:message key="label.vote.nominations"/></h4>
			<table id="nominationTable" cellpadding="0" cellspacing="0">
				<c:forEach var="nominationIndex"  begin="1" end="${pedagogicalPlannerForm.nominationCount}" >
					<tr>
						<td class="CKcell">
							<lams:CKEditor id="nomination[${nominationIndex-1}]"
								value="${pedagogicalPlannerForm.nominationList[nominationIndex-1]}"
								contentFolderID="${pedagogicalPlannerForm.contentFolderID}"
				                toolbarSet="CustomPedplanner" height="150px"
				                width="${param.plannerCKEditorShortWidth}" displayExpanded="false">
							</lams:CKEditor>
						</td>
						<td>
							<img class="clearEntry" src="<lams:LAMSURL/>images/cross.png"
								title="<fmt:message key="msg.planner.clear.entry" />"
								onclick="javascript:clearEntry(${nominationIndex-1})" />
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	</form:form>
	<a class="button" href="javascript:createQuestion();"><fmt:message key="label.add.new.nomination" /></a>
</body>
</lams:html>