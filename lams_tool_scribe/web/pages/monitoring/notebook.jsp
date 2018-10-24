<!DOCTYPE html>
            

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
	</lams:head>
	<body class="stripes">
	<c:set var="title"><fmt:message key="pageTitle.monitoring.notebook" /></c:set>
		<lams:Page title="${title}" type="monitor">
			<h4>
				<c:out value="${scribeUserDTO.firstName} ${scribeUserDTO.lastName}" escapeXml="true"/>
			</h4>
			<p><lams:out value="${scribeUserDTO.notebookEntry}" escapeHtml="true"/></p>
			<div id="footer"></div>
		</lams:Page>
	</body>
</lams:html>



