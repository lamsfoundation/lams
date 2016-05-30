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
	<title><fmt:message key="403.title" /></title>
	<lams:css />
</lams:head>

<body class="stripes">

	<c:set var="title">
		<fmt:message key="403.title" />
	</c:set>

	<lams:Page type="admin" title="${title}"></lams:Page>


	<lams:Alert id="403" class="type" close="false"></
				<fmt:message key="403.message" />
	</lams:Alert>
	
	<div id="footer"></div>
	<!--closes footer-->

</body>
</lams:html>
