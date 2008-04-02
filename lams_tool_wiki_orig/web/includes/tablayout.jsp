<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		<%@ include file="/common/tabbedheader.jsp"%>

		<tiles:insert attribute="header" />
	</lams:head>

	<body class="stripes" onLoad="init()">
		<div id="page">
	
				<tiles:useAttribute name="pageTitleKey" scope="request" />
				<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
				<logic:notEmpty name="pTitleKey">
					<h1>
						<fmt:message key="<%=pTitleKey %>" />
					</h1>
				</logic:notEmpty>

				<tiles:insert attribute="body" />

			<div id="footer"></div>
		</div>
	</body>
</lams:html>
