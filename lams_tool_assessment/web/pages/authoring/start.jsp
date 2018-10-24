<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp" %>

<html>
	<body class="stripes">
		<c:url value="init.do" var="actionURL">
			<c:param name="mode" value="${mode}" />
			<c:param name="notifyCloseURL" value="${param.notifyCloseURL}" />
		</c:url>
		<form:form action="${actionURL}" method="post" modelAttribute="assessmentForm" id="startForm">
			<form:hidden path="assessment.contentId"/>
			<form:hidden path="sessionMapID"/>
		</form:form>
		
		<script type="text/javascript">
			document.getElementById("startForm").submit();
		</script>
	</body>
</html>


