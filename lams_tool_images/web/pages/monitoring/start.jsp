<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<body class="stripes">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

		<script type="text/javascript">
			window.parent.location.href = "<c:url value="/monitoring/summary.do?toolSessionID=${sessionMap.toolSessionID}"/>"
		</script>
	<body class="stripes">
</html>

