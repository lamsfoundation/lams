<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<tiles:insert attribute="header" />
<body class="stripes" onload="init();">

	<tiles:useAttribute name="pageTitleKey" />
	<bean:define name="pageTitleKey" id="pTitleKey" type="String" />

	<lams:Page title="${pTitleKey}" type="navbar">
		<tiles:insert attribute="body" />
	</lams:Page>
</body>
</lams:html>
