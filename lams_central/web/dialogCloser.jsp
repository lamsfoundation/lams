<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
	<script type="text/javascript">
		window.parent['${param.function}']('${param.action}');
	</script>
</head>
</html>