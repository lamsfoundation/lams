<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>

<HTML>
<HEAD>
	<META HTTP-EQUIV="Refresh" CONTENT="0;URL=monitoring/addLesson.jsp?courseID=<c:out value="${courseID}"/>&classID=<c:out value="${classID}"/>"/>
	<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<TITLE><fmt:message key="title.add.lesson.window"/></TITLE>
	<lams:css/>
</HEAD>

<BODY>
<p><fmt:message key="msg.loading.add.lesson.window"/></p>
</BODY>
	
</HTML>
