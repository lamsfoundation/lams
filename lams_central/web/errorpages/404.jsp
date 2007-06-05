<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">

<lams:html>
	<lams:head>
		<title><fmt:message key="404.title" /></title>
		<lams:css />
	</lams:head>

	<body class="stripes">

		<div id="content">

			<h1>
				<fmt:message key="404.title" />
			</h1>

			<p class="space-bottom">
				<fmt:message key="404.message" />
			</p>

		</div>
		<!--closes content-->

		<div id="footer"></div>
		<!--closes footer-->

	</body>
</lams:html>
