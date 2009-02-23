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
  		function createTopic(){
  			prepareFormData();
  			$('#pedagogicalPlannerForm').ajaxSubmit({
  				url: "<c:url value='/authoring/createPedagogicalPlannerTopic.do' />",
  				success: function(responseText){
  					$('#body').html(responseText.substring(responseText.search(/<body/i)));
  				}
  			});
  		}
  		function prepareFormData(){
			//FCKeditor content is not submitted when sending by jQuery; we need to do this
			var topicIndex = 0;
			do{
				var topic = document.getElementById("topic["+topicIndex+"]");
				if (topic!=null){
					var content = FCKeditorAPI.GetInstance("topic["+topicIndex+"]").GetXHTML();
					topic.value=content;
					topicIndex++;
				}
			} while (topic!=null);
		}
  	</script>
</lams:head>
<body id="body">
	<%@ include file="/common/messages.jsp"%>
	<h4 class="space-left"><fmt:message key="lable.topic.title.subject"/></h4>
	<html:form action="/authoring/saveOrUpdatePedagogicalPlannerForm.do" styleId="pedagogicalPlannerForm" method="post">
		<html:hidden property="toolContentID" />
		<html:hidden property="valid" styleId="valid" />
		<html:hidden property="callID" styleId="callID" />
		<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		
		<c:forEach var="topicIndex"  begin="1" end="${formBean.topicCount}" >
			<div class="FCKdiv">
				<lams:FCKEditor id="topic[${topicIndex-1}]"
					value="${formBean.topicList[topicIndex-1]}"
					contentFolderID="${formBean.contentFolderID}"
	                toolbarSet="Custom-Pedplanner" height="150px"
	                width="760px" displayExpanded="false">
				</lams:FCKEditor>
			</div>
		</c:forEach>
	</html:form>
	<a class="button" href="javascript:createTopic();"><fmt:message key="label.authoring.create.new.topic" /></a>
</body>
</lams:html>