<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<tiles:insert attribute="header" />
	<body onload="init();">
		<tiles:useAttribute name="pageTitleKey" />
		<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
		<h1>
			<bean:message key="${pTitleKey}" />
		</h1>
		<tiles:insert attribute="body" />
	</body>
</html>
