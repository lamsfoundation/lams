<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<!DOCTYPE html>

<lams:html>
<lams:head>

	<c:set var="title">
		<fmt:message key="heading.general.error" />
	</c:set>

	<title>${title}</title>
	<lams:css />
</lams:head>

<body class="stripes">


	<lams:Page type="admin" title="${title}">

	<lams:Alert id="403" type="danger" close="false">
				<fmt:message key="403.title" />
	</lams:Alert>
	
	<div id="footer"></div>
	<!--closes footer-->

	</lams:Page>
	
</body>
</lams:html>
