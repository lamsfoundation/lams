<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
            
<html:html locale="true">
<lams:head>
	<title><fmt:message key="label.author.title" /></title>

	<%@ include file="/common/header.jsp"%>
</lams:head>
<body class="stripes">

	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page title="${title}" type="learner">
		<lams:Alert id="errorMessages" type="danger" close="false">
			<fmt:message key="message.monitoring.edit.activity.not.editable" />
		</lams:Alert>
	</lams:Page>
</body>
</html:html>
