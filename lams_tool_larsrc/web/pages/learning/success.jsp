<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
	</head>
	<body>
		<script type="text/javascript">
			parent.learningFrame.location = "<c:url value="/learning/start.do"/>?toolSessionID=${toolSessionID}";
		</script>
		<div style="align:center">
			<c:if test="${addType == 1}">
				Add URL success, <a href="<c:url value='/pages/learning/addurl.jsp'/>" target="newResourceFrame">click here to return</a>.
			</c:if>
			<c:if test="${addType == 2}">
				Add File success, <a href="<c:url value='/pages/learning/addfile.jsp'/>" target="newResourceFrame">click here to return</a>.
			</c:if>
		</div>
	</body>
</html>