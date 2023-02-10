<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
	<script type="text/javascript">
		window.parent['<c:out value="${param.function}" />']('<c:out value="${param.action}" />');
	</script>
</head>
</html>