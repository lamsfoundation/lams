<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
            
<lams:html>
	<lams:head>
	</lams:head>
	
	<body class="stripes" bgcolor="#9DC5EC" border="1" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
		<c:redirect url="/nextInstruction.do">
			<c:param name="mode" value="${param.mode}"/>
			<c:param name="itemIndex" value="${param.itemIndex}"/>
			<c:param name="itemUid" value="${param.itemUid}"/>
			<c:param name="toolSessionID" value="${param.toolSessionID}"/>
			<c:param name="sessionMapID" value="${param.sessionMapID}"/>
		</c:redirect>
	</body>
</lams:html>
