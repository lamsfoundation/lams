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

	<tiles:insert attribute="content" />
	
</lams:html>
