<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<tiles:insert attribute="header" />
	<body class="stripes" onload="init();">
	
		<tiles:useAttribute name="pageTitleKey" />
		<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
		<c:set var="title"><fmt:message key="${pTitleKey}" /></c:set>
		<lams:Page title="${title}" type="navbar">
			<tiles:insert attribute="body" />
		</lams:Page></div>
	</body>
</lams:html>
