<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/includes/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<head>
	<lams:css />
	<title><fmt:message key="activity.title"/></title>
</head>

<body>
<div id="page-learner"><!--main box 'page'-->

	<tiles:insert attribute="content" />
	
</body>
</lams:html>
