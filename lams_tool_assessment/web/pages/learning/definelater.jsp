<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>
<c:set var="title" scope="request"><fmt:message key="activity.title" /></c:set>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<meta http-equiv="refresh" content="60">
</lams:head>

<body class="stripes">
	<lams:Page type="learner" title="${title}">
	
		<lams:DefineLater />
		
		<div id="footer"></div>
		
	</lams:Page>
</body>
</lams:html>
