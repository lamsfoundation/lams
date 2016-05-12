<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
            
<html:html locale="true">
<lams:head>
	<title><fmt:message key="label.authoring.title" /></title>

	<%@ include file="/common/header.jsp"%>
</lams:head>
<body class="stripes">
	<c:set var="title"><fmt:message key="activity.title"/></c:set>
	<lams:Page title="${title}" type="learner">
		<B><fmt:message key="message.monitoring.edit.activity.not.editable" /></B>
	</lams:Page>
</body>
</html:html>
