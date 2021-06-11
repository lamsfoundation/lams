<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	
	<lams:css style="core" />
	<style type="text/css">
		a {
			margin: 10px 2px 0px 0px;
			float: right;
		}
		
		body {
			width: ${param.plannerCKEditorLongWidth};
		}
		
		input.item {
			margin: 5px 0px 5px 10px;
		}
		
		table#taskTable td {
			margin: 0px;
			padding: 0px;
		}
		
		img.clearEntry {
			maring: 0px;
			padding: 0px 10px 0px 0px;
			cursor: pointer;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
  	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
  	<script type="text/javascript">
  		function createItem(){
  			$('#pedagogicalPlannerForm').ajaxSubmit({
  				url: "<c:url value='/authoring/createPedagogicalPlannerItem.do' />",
  				success: function(responseText){
 	  				var bodyTag = '<body';
 	  				var selectedBody = responseText.substring(responseText.search(bodyTag) + 1);
 	  				selectedBody = selectedBody.substring(selectedBody.search(bodyTag));
  					$('#body').html(selectedBody);
  				}
  			});
  		}
  		
  		function clearEntry(itemIndex){
			document.getElementById("item"+itemIndex).value="";
		}
  	</script>
</lams:head>
<body id="body">
	<lams:errors/>
	<h4 class="space-left"><fmt:message key="planner.item.title"/></h4>
	<form:form  action="saveOrUpdatePedagogicalPlannerForm.do"  modelAttribute="plannerForm" id="pedagogicalPlannerForm" method="post">
		<form:hidden path="toolContentID" id="toolContentID" />
		<form:hidden path="valid" id="valid" />
		<form:hidden path="callID" idd="callID" />
		<form:hidden path="activityOrderNumber" id="activityOrderNumber" />
		
		<table id="taskTable" cellpadding="0" cellspacing="0">
			<c:forEach var="itemIndex"  begin="1" end="${plannerForm.taskListItemCount}" >
				<tr>
					<td>
						<form:input id="item${itemIndex-1}" cssClass="item" size="100" path="taskListItem[${itemIndex-1}]"/>
					</td>
					<td>
						<img class="clearEntry" src="<lams:LAMSURL/>images/cross.png"
							title="<fmt:message key="msg.planner.clear.entry" />"
							onclick="javascript:clearEntry(${itemIndex-1})" />
						</td>
				</tr>
			</c:forEach>
		</table>
	</form:form>
	<a class="button" href="javascript:createItem();"><fmt:message key="label.authoring.basic.add.task" /></a>
</body>
</lams:html>