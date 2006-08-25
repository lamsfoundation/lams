<%@ include file="/common/taglibs.jsp" %>
<html>
<body>
<html:form action="/authoring/init.do?mode=${mode}" method="post" styleId="startForm">
	<html:hidden property="resource.contentId"/>
	<html:hidden property="sessionMapID"/>
</html:form>

<script type="text/javascript">
document.getElementById("startForm").submit();
</script>
<body>
</html>