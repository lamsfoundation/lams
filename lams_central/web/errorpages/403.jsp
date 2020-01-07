<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

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
