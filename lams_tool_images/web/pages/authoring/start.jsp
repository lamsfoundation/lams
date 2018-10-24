<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp" %>
<html>
<body class="stripes">
	<form:form action="init.do?mode=${mode}" method="post" modelAttribute="imageGalleryForm" id="startForm">
		<form:hidden path="imageGallery.contentId"/>
		<form:hidden path="sessionMapID"/>
	</form:form>
	
	<script type="text/javascript">
		document.getElementById("startForm").submit();
	</script>
</body>
</html>
