<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp" %>

<html>
<body class="stripes">
	<form:form action="init.do?mode=${mode}" modelAttribute="authoringForm" method="post" id="startForm">
		<form:hidden path="whiteboard.contentId"/>
		<form:hidden path="sessionMapID"/>
	</form:form>
	
	<script type="text/javascript">
		document.getElementById("startForm").submit();
	</script>
</body>
</html>
