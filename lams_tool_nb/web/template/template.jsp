<!DOCTYPE html>

<%@ include file="/includes/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title"/></title>
</lams:head>

<body class="stripes">

	<tiles:insert attribute="content" />
	
</lams:html>