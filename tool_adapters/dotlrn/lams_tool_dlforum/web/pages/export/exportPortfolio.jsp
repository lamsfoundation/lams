<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><fmt:message key="titleHeading.exportPortfolio" /></title>
		<lams:css localLinkPath="../" />
	</lams:head>

	<body class="stripes">

			<div id="content">
			<h1><fmt:message key="monitor.sessions" /></h1>
			<c:choose>
				<c:when test="${not empty sessions}">
					<c:forEach var="session" items="${sessions}">
						<p>
						<a target="_blank" href="${session.sessionName}">${session.sessionName}</a>
						</p>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<p>
					<fmt:message key="exportPortfolio.message.nosessions" />
					</p>
				</c:otherwise>
			</c:choose>
					
			</div>
			<!--closes content-->

			<div id="footer">
			</div>
			<!--closes footer-->
	</body>
</html>

