<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean-el" prefix="bean-el" %>
<%@ taglib uri="tags-core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<html:base/>
	<c:set var="title" scope="session"><tiles:getAsString name="title"/></c:set>
	<title>LAMS :: <bean-el:message key="${title}"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<lams:css/>
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</head>
    
<body>
<div id="page">
	<div id="content">
			<tiles:insert attribute="content" />
	</div>
</div>
</body>
</html>
