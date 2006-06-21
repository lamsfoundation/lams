<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<c:set var="lams"><lams:LAMSURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf8">
		<title><fmt:message key="title.export.loading" /></title>
		<!-- ********************  CSS ********************** -->
		<lams:css />
		
	</head>

	<BODY>
		<div align="center">
			<h1>
				<fmt:message key="msg.export.loading" />
			</h1>
			<img src="${lams}images/loading.gif">
		</div>
		<script>
			location.href="<c:url value='/authoring/exportToolContent.do?method=export&learningDesignID=${learningDesignID}'/>";
		</script>
	</BODY>
</HTML>
