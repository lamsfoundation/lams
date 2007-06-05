<%@ include file="/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
<lams:head>
	<html:base/>
	<c:set var="title" scope="session"><tiles:getAsString name="title"/></c:set>
	<title><fmt:message key="${title}"/></title>
	<lams:css style="learner"/>
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
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
