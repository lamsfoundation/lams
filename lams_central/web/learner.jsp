<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>

<HTML>
<HEAD>
	<META HTTP-EQUIV="Refresh" CONTENT="0;URL=learning/main.jsp?lessonID=<c:out value="${lessonID}"/>">
	<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<TITLE><fmt:message key="title.learner.window"/></TITLE>
	<lams:css/>
</HEAD>

<BODY>
<p><fmt:message key="msg.loading.learner.window"/></p>
</BODY>
	
</HTML>
