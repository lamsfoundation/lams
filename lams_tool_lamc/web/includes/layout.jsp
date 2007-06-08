<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<body class="stripes">
		<div id="page">
			<tiles:useAttribute name="pageTitleKey" scope="request" />
			<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
			<logic:notEmpty name="pTitleKey">
				<h1 class="no-tabs-below">
					<fmt:message key="<%=pTitleKey %>" />
				</h1>
			</logic:notEmpty>

			<div id="header"></div>

			<div id="content">
				<tiles:insert attribute="body" />
			</div>

			<div id="footer">
			</div>
		</div>
	</body>
</lams:html>
