<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<html>
	<body>
		<c:choose>
			<c:when test="${sessionMap.runAuto}">
				<script type="text/javascript">
					document.location = "<c:url value="/reviewItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemUid=${itemUid}";
				</script>
			</c:when>
			<c:otherwise>
				<script type="text/javascript">
					document.location = "<c:url value="/pages/learning/learning.jsp?sessionMapID=${sessionMapID}&mode=${mode}"/>"
				</script>
			</c:otherwise>
		</c:choose>
	</body>
</html>
