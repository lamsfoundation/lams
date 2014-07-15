<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<html>
	<body class="stripes">
		<script type="text/javascript">
			document.location.href = "<c:url value="/pages/learning/learning.jsp?sessionMapID=${sessionMapID}&mode=${mode}"/>"
		</script>
	</body>
</html>
