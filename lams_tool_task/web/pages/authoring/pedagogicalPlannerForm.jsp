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
		
		input.item {
			margin: 5px 0px 5px 10px;
			float: none;
		}
	</style>
	
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
  	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
  	<script type="text/javascript">
  		function createItem(){
  			$('#pedagogicalPlannerForm').ajaxSubmit({
  				url: "<c:url value='/authoring/createPedagogicalPlannerItem.do' />",
  				success: function(responseText){
  					$('#body').html(responseText.substring(responseText.search(/<body/i)));
  				}
  			});
  		}
  	</script>
</lams:head>
<body id="body">
	<%@ include file="/common/messages.jsp"%>
	<h4 class="space-left"><fmt:message key="planner.item.title"/></h4>
	<html:form action="/authoring/saveOrUpdatePedagogicalPlannerForm.do" styleId="pedagogicalPlannerForm" method="post">
		<html:hidden property="toolContentID" />
		<html:hidden property="valid" styleId="valid" />
		<html:hidden property="callID" styleId="callID" />
		<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		
		<c:forEach var="itemIndex"  begin="1" end="${formBean.taskListItemCount}" >
			<html:text styleClass="item" size="85" property="taskListItem[${itemIndex-1}]"></html:text>
		</c:forEach>
	</html:form>
	<a class="button" href="javascript:createItem();"><fmt:message key="label.authoring.basic.add.task" /></a>
</body>
</lams:html>