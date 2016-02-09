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
	<html:base />
	<lams:css />
	<title><fmt:message key="activity.title" />
	</title>
</lams:head>

<body class="stripes">
	<div id="content">
		<h1>
			<fmt:message key="activity.title" />
		</h1>
		<lams:DefineLater defineLaterMessageKey="error.defineLater" />
	</div>
	<div id="footer"></div>
</body>
</lams:html>








