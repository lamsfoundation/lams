<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<html>
	<body class="stripes">
		<script type="text/javascript">
			window.parent.location.href = "<c:url value="/learning/start.do?toolSessionID=${toolSessionID}&mode=${mode}"/>"
		</script>
	</body>
</html>

