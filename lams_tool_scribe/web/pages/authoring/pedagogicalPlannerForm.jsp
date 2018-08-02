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
		
		body{
			width: 760px;
		}
					
		div.FCKdiv {
			margin-top: 10px;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
  	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
  	<script type="text/javascript">
  		function createHeading(){
  			prepareFormData();
  			$('#pedagogicalPlannerForm').ajaxSubmit({
  				url: "<c:url value='/pedagogicalPlanner/createPedagogicalPlannerHeading.do?' />",
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
			var headingIndex = 0;
			do{
				var heading = document.getElementById("heading["+headingIndex+"]");
				if (heading!=null){
					var content = CKEDITOR.instances["heading["+headingIndex+"]"].getData();
					heading.value=content;
					headingIndex++;
				}
			} while (heading!=null);
		}
  	</script>
</lams:head>
<body id="body">
	<form:form action="/pedagogicalPlanner/saveOrUpdatePedagogicalPlannerForm.do?" modelAttribute="pedagogicalPlannerForm" id="pedagogicalPlannerForm" method="post">
		<form:hidden path="toolContentID" id="toolContentID" />
		<form:hidden path="valid" id="valid" />
		<form:hidden path="callID" id="callID" />
		<html:hidden path="activityOrderNumber" id="activityOrderNumber" />
		
		<c:choose>
			<c:when test="${pedagogicalPlannerForm.headingCount eq 0}">
				<h4 class="space-left"><fmt:message key="message.noHeadings" /></h4>
			</c:when>
			<c:otherwise>
				<h4 class="space-left"><fmt:message key="label.authoring.basic.heading"/></h4>
				<c:forEach var="headingIndex"  begin="1" end="${pedagogicalPlannerForm.headingCount}" >
					<div class="FCKdiv">
						<lams:CKEditor id="heading[${headingIndex-1}]"
							value="${pedagogicalPlannerForm.headingList[headingIndex-1]}"
							contentFolderID="${pedagogicalPlannerForm.contentFolderID}"
			                toolbarSet="CustomPedplanner" height="150px"
			                width="${param.plannerCKEditorLongWidth}" displayExpanded="false">
						</lams:CKEditor>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</form:form>
	<a class="button" href="javascript:createHeading();"><fmt:message key="label.authoring.basic.heading.add" /></a>
</body>
</lams:html>