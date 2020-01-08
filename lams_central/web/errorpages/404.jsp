<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<!-- CUSTOMERRORPAGE404: authoring templates string -->
 <lams:html>
	<lams:head>
		<title><fmt:message key="404.title" /></title>
		<lams:css />
	</lams:head>

	<body class="stripes">

	<c:set var="title">
		<fmt:message key="404.title" />
	</c:set>
	
		<lams:Page type="admin" title="${title}">

			<lams:Alert id="404" type="danger" close="false">
				<fmt:message key="404.message" />
			</lams:Alert>

		</div>
		<!--closes content-->

		<div id="footer"></div>
		<!--closes footer-->
</lams:Page>
	</body>
</lams:html>
