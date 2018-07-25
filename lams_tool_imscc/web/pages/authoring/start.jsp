<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp" %>

<html>
<body class="stripes">
	<html:form action="/authoring/init.do?mode=${mode}" method="post" styleId="startForm">
		<html:hidden property="commonCartridge.contentId"/>
		<html:hidden property="sessionMapID"/>
	</html:form>
	
	<script type="text/javascript">
	document.getElementById("startForm").submit();
	</script>
<body class="stripes">
</html>
