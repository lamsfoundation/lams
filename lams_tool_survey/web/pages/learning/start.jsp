<!DOCTYPE html>
		

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
