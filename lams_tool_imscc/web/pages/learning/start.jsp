<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<html>
	<body class="stripes">

		<script type="text/javascript">
			<c:choose>
				<c:when test="${sessionMap.runAuto}">
					document.location = "<c:url value="/reviewItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemUid=${itemUid}";
				</c:when>
				<c:otherwise>
					document.location = "<c:url value="/pages/learning/learning.jsp?sessionMapID=${sessionMapID}&mode=${mode}"/>"
				</c:otherwise>
			</c:choose>
		</script>
		
	</body>
</html>
