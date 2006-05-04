<%@ include file="/common/taglibs.jsp"%>
<html>
	<body>

		<c:choose>
			<c:when test="${runAuto}">
				<script type="text/javascript">
					document.location = "<c:url value="/reviewItem.do"/>?itemUid=${itemUid}";
				</script>
			</c:when>
			<c:otherwise>
				<script type="text/javascript">
					document.location = "<c:url value="/pages/learning/learning.jsp"/>"
				</script>
			</c:otherwise>
		</c:choose>
	<body>
</html>
