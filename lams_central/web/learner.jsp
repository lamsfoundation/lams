<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>

<lams:html>
<HEAD>
	<META HTTP-EQUIV="Refresh" CONTENT="0;URL=learning/main.jsp?lessonID=<c:out value="${lessonID}"/>">
	<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<TITLE><fmt:message key="title.learner.window"/></TITLE>
	<lams:css/>
</HEAD>

<BODY>
	<div id="page-learner"><!--main box 'page'-->
	
		<div id="header-no-tabs-learner">
		</div><!--closes header-->

		<div id="content-learner">
			<p><fmt:message key="msg.loading.learner.window"/></p>
		</div>
	   
		<div id="footer-learner">
		</div><!--closes footer-->

	</div><!--closes page-->
</BODY>
	
</lams:html>
