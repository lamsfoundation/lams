<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf8">
		<title><fmt:message key="title.export.loading" /></title>
		<!-- ********************  CSS ********************** -->
		<lams:css />
		<meta http-equiv="refresh" content="0;URL=<c:url value='/export.do?method=export&learningDesignID=${learningDesignID}'/>">
	</head>

	<BODY>
		<div align="center">
			<h1>
				<fmt:message key="msg.export.loading" />
			</h1>
			<img src="/images/loading.gif">
		</div>
	</BODY>
</HTML>
