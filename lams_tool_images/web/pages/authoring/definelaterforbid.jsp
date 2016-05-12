<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<html:html locale="true">
<lams:head>
	<title><fmt:message key="label.author.title" /></title>

	<%@ include file="/common/header.jsp"%>
</lams:head>
<body class="stripes">
	<c:set var="title"><fmt:message key="label.authoring.heading" /></c:set>
	<lams:Page title="${title}" type="navbar">
		<B><fmt:message key="message.monitoring.edit.activity.not.editable" /></B>
	</lams:Page>
</body>
</html:html>
