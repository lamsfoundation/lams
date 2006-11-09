<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<html>
	<body class="stripes">

		<c:choose>
			<c:when test="${sessionMap.showOnOnePage}">
				<script type="text/javascript">
					document.location = "<c:url value="/learning/allonpage.do"/>?sessionMapID=${sessionMapID}";
				</script>
			</c:when>
			<c:otherwise>
				<script type="text/javascript">
					document.location = "<c:url value="/learning/oneonpage.do"/>?sessionMapID=${sessionMapID}";
				</script>
			</c:otherwise>
		</c:choose>
	<body class="stripes">
</html>
