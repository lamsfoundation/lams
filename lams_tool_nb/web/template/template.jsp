<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/includes/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<html:html>
<head>
	<html:base />
	<lams:headItems />
	<title><fmt:message key="activity.title" /></title>
</head>

<body>
	<div id="page">
		<c:set var="pageheader" scope="session">
			<tiles:getAsString name="pageHeader" />
		</c:set>

		<h1 class="no-tabs-below">
			<c:out value="${sessionScope.pageheader}" />
		</h1>

		<div id="header-no-tabs"></div>

		<div id="content">
			<tiles:insert attribute="content" />
		</div>

		<div id="footer">
		</div>
	</div>
</body>
</html:html>
