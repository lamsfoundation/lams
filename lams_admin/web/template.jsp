<%@ include file="/taglibs.jsp"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<c:set var="title" scope="session"><tiles:getAsString name="title"/></c:set>
	<title><fmt:message key="${title}"/></title>
	<lams:css style="learner"/>
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" type="text/css" media="screen">
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
