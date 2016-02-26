<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<html>
	<body class="stripes">
		<script type="text/javascript">
			document.location.href = "<c:url value="/pages/learning/learning.jsp?sessionMapID=${sessionMapID}&mode=${mode}"/>"
		</script>
	</body>
</html>
