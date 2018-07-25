<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<body class="stripes">
			
			<c:set var="title">
			<tiles:useAttribute name="pageTitleKey" scope="request" />
			<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
			<logic:notEmpty name="pTitleKey">
					<fmt:message key="<%=pTitleKey %>" />
			</logic:notEmpty>
			</c:set>

			<lams:Page type="monitor" title="${title}">

			<tiles:insert attribute="body" />

			<div id="footer">
			</div>

			</lams:Page>
	</body>
</lams:html>
