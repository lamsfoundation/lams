<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript">
		window.parent['${param.function}']('${param.action}');
	</script>
</head>
</html>