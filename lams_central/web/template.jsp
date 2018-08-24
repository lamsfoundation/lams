<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>


<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 

<!DOCTYPE html>
<lams:html>
<lams:head>
	<base href="<lams:WebAppURL />"/>
	<title><fmt:message><tiles:getAsString name="titleKey"/></fmt:message></title>
	<lams:css/>
	<script language="JavaScript" type="text/JavaScript" src="<lams:WebAppURL />page='/includes/javascript/changeStyle.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />
</lams:head>
    
<body class="stripes">
<lams:Page type="learner" title="">
			<tiles:insert attribute="content" />
</lams:Page>
</body>
</lams:html>
