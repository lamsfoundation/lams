<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
<lams:head>
		<%@ include file="/common/header.jsp"%>
		<title>
			<fmt:message key="tab.monitoring.statistics" />
		</title>
</lams:head> 
<body class="stripes">
<div id="page">
	<div id="header-no-tabs">
	</div>
	<div id="content">
		<c:if test="${not empty param.includeMode}">
			<c:set var="includeMode" value="${param.includeMode}" />
		</c:if>
		<c:if test="${empty includeMode}">
			<c:set var="includeMode" value="monitoring" />
		</c:if>
		<%@ include file="/pages/learning/questionSummaries.jsp" %>
	</div>
	<div id="footer">
	</div>
</div>
</body>
</lams:html>
	