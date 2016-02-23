<!DOCTYPE html>

<%@include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
</lams:head>

<body class="stripes">

	<c:set var="title" scope="request">
		<fmt:message key="activity.title"></fmt:message>
	</c:set>

	<lams:Page type="learner" title="${title}">
		<lams:DefineLater />
		<div id="footer"></div>
	</lams:Page>

</body>
</lams:html>


