<%@ include file="/common/taglibs.jsp" %>
<html>
<body>
<form action="<c:url value='/authoring/init.do'/>" method="post" id="startForm">
	<input type="hidden" name="toolContentID" value="${toolContentID}"/>
</form>

<script type="text/javascript">
document.getElementById("startForm").submit();
</script>
<body>
</html>