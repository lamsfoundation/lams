<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<html:base />
	<lams:css />
	<title><fmt:message key="label.learner.error" /></title>
</lams:head>

<c:set var="title" scope="request">
	<fmt:message key="activity.title" />
</c:set>

<body class="stripes">
	<lams:Page type="learner" title="${title}">
		<div class="voffset10 pull-right">
			<%@ include file="/common/messages.jsp"%>
		</div>
		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>