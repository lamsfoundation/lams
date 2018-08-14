<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<html:base/>
	<title><fmt:message><tiles:getAsString name="titleKey"/></fmt:message></title>
	<lams:css/>
	<script type="text/JavaScript" src="<lams:WebAppURL/>includes/javascript/changeStyle.js"></script>
	<script type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</lams:head>
    
<body class="stripes">
<div id="page">
	<div id="content">
			<tiles:insert attribute="content" />
	</div>
</div>
</body>
</lams:html>
