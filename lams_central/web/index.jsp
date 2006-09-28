<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<lams:html>
<head>
	<meta HTTP-EQUIV="Refresh" CONTENT="0;URL=index.do?state=active">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><fmt:message key="index.welcome" /></title>
	<lams:css/>
</head>

<body>
	<div id="page-learner"><!--main box 'page'-->
	
		<div id="header-no-tabs-learner">
		</div><!--closes header-->

		<div id="content-learner"  valign="middle">
			<p align="center"><img src="images/loading.gif" /> <font color="gray" size="4"><fmt:message key="msg.loading"/></font></p>
		</div>
	   
		<div id="footer-learner">
		</div><!--closes footer-->

	</div><!--closes page-->
</body>
</lams:html>