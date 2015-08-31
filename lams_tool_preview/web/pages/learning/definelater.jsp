<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
		<c:choose>
		<c:when test="${creatingUsers == 'true'}"><meta http-equiv="refresh" content="3"></c:when>
		<c:otherwise><meta http-equiv="refresh" content="60"></c:otherwise>
		</c:choose>
</lams:head>

<body class="stripes">
	<div id="content">
		<h1>
			<fmt:message key="activity.title" />
		</h1>
		<c:choose>
		<c:when test="${creatingUsers == 'true'}"><lams:DefineLater defineLaterMessageKey="creating.users.message"></lams:DefineLater></c:when>
		<c:otherwise><lams:DefineLater /></c:otherwise>
		</c:choose>
	</div>
	<div id="footer"></div>
</body>
</lams:html>
