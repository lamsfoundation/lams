<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<html>
	<body class="stripes">

		<html:form action="/learning/init.do?sessionMapID=${sessionMapID}&mode=${mode}" method="post" styleId="startForm">
			<html:hidden property="sessionMapID"/>
			<html:hidden property="spreadsheet.code"/>	
		</html:form>
		
		<script type="text/javascript">
			document.getElementById("startForm").submit();
		</script>
		
	<body class="stripes">
</html>




