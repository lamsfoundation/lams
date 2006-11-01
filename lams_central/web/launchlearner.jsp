<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>

<lams:html>
<head>
	<title><fmt:message key="index.welcome" /></title>
	<lams:css style="tabbed"/>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/openUrls.js"></script>
</head>

<body class="stripes">
	
	<div id="page">
	
	<div id="header-no-tabs">
	</div>

	<div id="content">
		<h1><c:out value="${name}"/></h1>
		<p><c:out value="${description}" escapeXml="false"/></h1>
		<p>Status: <c:out value="${status}"/></p>
		<p><a href='javascript:openLearner(<c:out value="${lessonID}"/>)'>Start Lesson</a></p>
	</div>
	   
	<div id="footer">
	</div><!--closes footer-->
		
	</div>
</body>


</lams:html>