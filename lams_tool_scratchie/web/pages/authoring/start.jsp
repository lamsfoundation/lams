<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp" %>
<html>
	<body class="stripes">
	
		<form:form action="/lams/tool/lascrt11/authoring/init.do?mode=${mode}&notifyCloseURL=${notifyCloseURL}" method="post" modelAttribute="authoringForm" id="startForm">
			<form:hidden path="scratchie.contentId"/>
			<form:hidden path="sessionMapID"/>
		</form:form>
		
		<script type="text/javascript">
			document.getElementById("startForm").submit();
		</script>
		
	</body>
</html>
