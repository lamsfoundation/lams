<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp" %>

<html>
<body class="stripes">
	<form:form action="init.do?mode=${mode}" modelAttribute="authoringForm" method="post" id="startForm">
		<form:hidden path="commonCartridge.contentId"/>
		<form:hidden path="sessionMapID"/>
	</form:form>
	
	<script type="text/javascript">
	document.getElementById("startForm").submit();
	</script>
<body class="stripes">
</html>
