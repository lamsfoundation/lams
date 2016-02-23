<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<html:base />
	<lams:css />
	<title><fmt:message key="activity.title" /></title>
</lams:head>
<body class="stripes">
	<c:set scope="request" var="title">
		<fmt:message key="activity.title" />
	</c:set>

	<lams:Page type="learner" title="${title}">

		<lams:DefineLater defineLaterMessageKey="error.defineLater" />

	<div id="footer"></div>
	</lams:Page>
	
</body>
</lams:html>