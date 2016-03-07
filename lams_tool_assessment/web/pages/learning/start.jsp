<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<html>
	<body class="stripes">
		<script type="text/javascript">
			document.location = "<c:url value="/learning/nextPage.do"/>?sessionMapID=${sessionMapID}&pageNumber=1";
		</script>
	<body class="stripes">
</html>
