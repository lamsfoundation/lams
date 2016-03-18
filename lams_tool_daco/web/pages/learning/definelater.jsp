<!DOCTYPE html>
        

<%@include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<meta http-equiv="refresh" content="60">
</lams:head>

<body class="stripes">
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	<lams:Page type="learner" title="${title}">
		<lams:DefineLater />
	</lams:Page>
</body>
</lams:html>






