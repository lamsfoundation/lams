<!DOCTYPE html>
<%@ include file="/includes/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title"/></title>
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/bootstrap.min.js" type="text/javascript"></script>
</lams:head>

<body class="stripes">

	<c:set var="title" scope="request">
	<fmt:message key="activity.title" />
	</c:set>
		<lams:Page type="learner" title="${title}">
					<lams:DefineLater defineLaterMessageKey="message.defineLaterSet" />
	</lams:Page>
	
</lams:html>
