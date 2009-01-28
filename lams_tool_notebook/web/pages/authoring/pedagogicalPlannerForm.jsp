<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.dimensions.pack.js"></script>
	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.cluetip.js"></script>
	<script language="JavaScript" type="text/javascript">
		$(document).ready(function() {
			$('#editingAdvice').cluetip({
				height: 130,
				cluetipClass: 'jtip',
				arrows: true,
				dropShadow: false,
	 			sticky: true,
				waitImage: true,
				activation: 'click',
				closeText: 'X',
				topOffset: 0
			});
		});
	</script>
	
	<lams:css style="core" />
	<link href="<lams:LAMSURL />css/jquery.cluetip.css" rel="stylesheet" type="text/css" />
</lams:head>
<body>
	<%@ include file="/common/messages.jsp"%>
	<h4 class="space-left" style="float:left"><fmt:message key="label.authoring.basic.instructions" /></h4>
	<html:form action="/pedagogicalPlanner.do?dispatch=saveOrUpdatePedagogicalPlannerForm" styleId="pedagogicalPlannerForm" method="post">
		<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		
		<c:if test="${formBean.editingAdviceAvailable}">
			<c:url value="/pedagogicalPlanner.do" var="tipUrl">
				<c:param name="dispatch" value="getEditingAdvice" />
				<c:param name="toolContentID" value="${formBean.toolContentID}" />
			</c:url>
			<a href="#" title="" rel="${tipUrl}" id="editingAdvice"><fmt:message key="label.planner.editing.advice" /></a>
		</c:if>
		
		<html:hidden property="toolContentID" />
		<html:hidden property="valid" styleId="valid" />
		<html:hidden property="callID" styleId="callID" />
		<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
		<html:textarea property="instructions" style="margin: 2px 0px 0px 10px;" cols="65" rows="6" />
	</html:form>
</body>
</lams:html>