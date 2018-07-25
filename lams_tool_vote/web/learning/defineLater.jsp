<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams">
	<lams:LAMSURL />
</c:set>
<c:set scope="request" var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<title><fmt:message key="activity.title" /></title>
</lams:head>

<body class="stripes">
	<c:set var="title" scope="request">
		<fmt:message key="activity.title" />
	</c:set>
	<lams:Page type="learner" title="${title}">
		<lams:DefineLater defineLaterMessageKey="error.defineLater" />
		<div id="footer"></div>
	</lams:Page>
</body>
</lams:html>








