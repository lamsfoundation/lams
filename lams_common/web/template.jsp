
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html:html locale = "true">
<head>
	<html:base/>
	<title><tiles:getAsString name="title"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/style.css'/>" >
	<script language="JavaScript" type="text/JavaScript" src="changeStyle.js"></script>
</head>
    
<body bgcolor="#9DC5EC" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="95%" height="95%" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td>
	        <c:set var="pageHeader" scope="session"><tiles:getAsString name="pageHeader"/></c:set>
			<tiles:insert attribute="header" />
		</td>
	</tr>
	<tr>
		<td width="100%" height="100%" align="center" valign="middle" bgcolor="#FFFFFF">	
			<tiles:insert attribute="content" />
		</td>
	</tr>
	<tr>
		<td>		
			<tiles:insert attribute="footer" />
		</td>
	</tr>		
</table>
</body>
</html:html>
