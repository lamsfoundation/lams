<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<%-- Same layout as error.jsp but assumes the error details have been supplied in the request by an Action (e.g. LoginAs) and not from a stacktrace --%>

<lams:html>
<lams:head>
	<title><fmt:message key="heading.general.error" /></title>
	<lams:css />
</lams:head>

<body class="stripes">

	<lams:Page type="admin" title="${errorName}">
		<%-- Error Messages --%>

		<div class="voffset10">
		<lams:Alert id="errors" type="danger" close="false">
			
				<c:out value="${errorMessage}" escapeXml="true" />

		</lams:Alert>
		</div>
		

		<div id="footer"></div>
		<!--closes footer-->
	</lams:Page>
</body>
</lams:html>
