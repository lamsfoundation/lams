<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>

<html>
	<tiles:insert attribute="header" />
	<body onLoad="init()">
		<tiles:useAttribute name="pageTitleKey" scope="request" />
		<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
		<logic:notEmpty name="pTitleKey">
			<h1>
				<bean:message key="<%=pTitleKey %>" />
			</h1>
		</logic:notEmpty>

		<tiles:insert attribute="body" />
		<tiles:insert attribute="footer" />
	</body>
</html>
