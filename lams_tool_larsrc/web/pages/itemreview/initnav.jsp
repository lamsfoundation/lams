<%--
To avoid use HttpSession (LDEV-199), try to 
use request.setAttribute() transfer value. But this page is embeded into a Frame html page, directly 
request can not block on frame page, so use this trick page redirect request, then server side could handle this 
request using this page as target 
--%>

<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%= session.getAttribute("instructionNavForm") %>" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	</head>
	<body bgcolor="#9DC5EC" border="1" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
		<c:redirect url="/nextInstruction.do">
			<c:param name="mode" value="${param.mode}"/>
			<c:param name="itemIndex" value="${param.itemIndex}"/>
			<c:param name="itemUid" value="${param.itemUid}"/>
			<c:param name="toolSessionID" value="${param.toolSessionID}"/>
		</c:redirect>
	</body>
</html>
