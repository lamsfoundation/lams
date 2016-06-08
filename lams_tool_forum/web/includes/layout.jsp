<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<body class="stripes">

			<div id="content">
				<tiles:useAttribute name="pageTitleKey" scope="request" />
				<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
				<logic:notEmpty name="pTitleKey">
					<h1>
						<fmt:message key="<%=pTitleKey %>" />
					</h1>
				</logic:notEmpty>

				<tiles:insert attribute="body" />
			</div>

			<div id="footer">
			</div>

	</body>
</lams:html>
