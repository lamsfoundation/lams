<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<html>
	<body class="stripes">
		<c:set var="sessionMapID" value="${sessionMapID}" />
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

		<script type="text/javascript">
			window.parent.location.href = "<c:url value="/monitoring/summary.do"/>?toolContentID=${sessionMap.toolContentID}&contentFolderID=${sessionMap.contentFolderID}"
		</script>
	<body class="stripes">
</html>

