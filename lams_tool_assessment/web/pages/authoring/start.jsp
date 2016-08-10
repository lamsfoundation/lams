<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp" %>

<html>
	<body class="stripes">
		<c:url value="authoring/init.do" var="actionURL">
			<c:param name="mode" value="${mode}" />
			<c:param name="notifyCloseURL" value="${param.notifyCloseURL}" />
		</c:url>
		<html:form action="${actionURL}" method="post" styleId="startForm">
			<html:hidden property="assessment.contentId"/>
			<html:hidden property="sessionMapID"/>
		</html:form>
		
		<script type="text/javascript">
			document.getElementById("startForm").submit();
		</script>
	</body>
</html>


