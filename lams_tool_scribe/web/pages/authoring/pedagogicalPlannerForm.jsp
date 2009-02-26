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
		
		body{
			width: 760px;
		}
					
		div.FCKdiv {
			margin-top: 10px;
		}
	</style>
	
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
  	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
  	<script type="text/javascript">
  		function createHeading(){
  			prepareFormData();
  			$('#pedagogicalPlannerForm').ajaxSubmit({
  				url: "<c:url value='/pedagogicalPlanner.do?dispatch=createPedagogicalPlannerHeading' />",
  				success: function(responseText){
  					$('#body').html(responseText.substring(responseText.search(/<body/i)));
  				}
  			});
  		}
  		function prepareFormData(){
			//FCKeditor content is not submitted when sending by jQuery; we need to do this
			var headingIndex = 0;
			do{
				var heading = document.getElementById("heading["+headingIndex+"]");
				if (heading!=null){
					var content = FCKeditorAPI.GetInstance("heading["+headingIndex+"]").GetXHTML();
					heading.value=content;
					headingIndex++;
				}
			} while (heading!=null);
		}
  	</script>
</lams:head>
<body id="body">
	<html:form action="/pedagogicalPlanner.do?dispatch=saveOrUpdatePedagogicalPlannerForm" styleId="pedagogicalPlannerForm" method="post">
		<html:hidden property="toolContentID" />
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
						<lams:FCKEditor id="heading[${headingIndex-1}]"
							value="${formBean.headingList[headingIndex-1]}"
							contentFolderID="${formBean.contentFolderID}"
			                toolbarSet="Custom-Pedplanner" height="150px"
			                width="760px" displayExpanded="false">
						</lams:FCKEditor>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</html:form>
	<a class="button" href="javascript:createHeading();"><fmt:message key="label.authoring.basic.heading.add" /></a>
</body>
</lams:html>