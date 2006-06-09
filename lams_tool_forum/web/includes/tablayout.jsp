<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>

<html>
	<head>
		<title>
			<bean:message key="activity.title" />
	  	</title>
	  	<%@ include file="/common/header.jsp"%>
	  	
		<tiles:insert attribute="header" />
	</head>
	
	<body onLoad="init()">
		<tiles:useAttribute name="pageTitleKey" scope="request" />
		<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
		<logic:notEmpty name="pTitleKey">
			<h1>
				<bean:message key="<%=pTitleKey %>" />
			</h1>
		</logic:notEmpty>

		<tiles:insert attribute="body" />
	</body>
</html>
