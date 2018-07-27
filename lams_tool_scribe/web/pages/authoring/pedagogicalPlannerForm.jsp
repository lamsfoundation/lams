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
  				url: "<c:url value='/pedagogicalPlanner.do?dispatch=createPedagogicalPlannerHeading' />",
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
	<html:form action="/pedagogicalPlanner.do?dispatch=saveOrUpdatePedagogicalPlannerForm" styleId="pedagogicalPlannerForm" method="post">
		<html:hidden property="toolContentID" styleId="toolContentID" />
		<html:hidden property="valid" styleId="valid" />
		<html:hidden property="callID" styleId="callID" />
		<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		
		<c:choose>
			<c:when test="${formBean.headingCount eq 0}">
				<h4 class="space-left"><fmt:message key="message.noHeadings" /></h4>
			</c:when>
			<c:otherwise>
				<h4 class="space-left"><fmt:message key="label.authoring.basic.heading"/></h4>
				<c:forEach var="headingIndex"  begin="1" end="${formBean.headingCount}" >
					<div class="FCKdiv">
						<lams:CKEditor id="heading[${headingIndex-1}]"
							value="${formBean.headingList[headingIndex-1]}"
							contentFolderID="${formBean.contentFolderID}"
			                toolbarSet="CustomPedplanner" height="150px"
			                width="${param.plannerCKEditorLongWidth}" displayExpanded="false">
						</lams:CKEditor>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</html:form>
	<a class="button" href="javascript:createHeading();"><fmt:message key="label.authoring.basic.heading.add" /></a>
</body>
</lams:html>