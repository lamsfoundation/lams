<!DOCTYPE html>
            
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
	<lams:head>  
		<title>
			<fmt:message>pageTitle.monitoring.notebook</fmt:message>
		</title>
		<lams:css/>
	</lams:head>

	<body class="stripes">
		<c:set var="title"><fmt:message>pageTitle.monitoring.notebook</fmt:message></c:set>
		<lams:Page type="learner" title="${title}">
			<p><c:out value="${gmapUserDTO.firstName} ${gmapUserDTO.lastName} (${gmapUserDTO.loginName})" escapeXml="true"/></p>
			<p><lams:out value="${gmapUserDTO.notebookEntry}" escapeHtml="true"/></p>
			<div class="footer"></div>
		</lams:Page>
	</body>
</lams:html>



