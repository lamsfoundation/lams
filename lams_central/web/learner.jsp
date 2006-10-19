<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>

<lams:html>
<HEAD>
	<META HTTP-EQUIV="Refresh" CONTENT="0;URL=learning/main.jsp?lessonID=<c:out value="${lessonID}"/><c:if test="${mode != null}">&mode=<c:out value="${mode}"/></c:if>">
	<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<TITLE><fmt:message key="title.learner.window"/></TITLE>
	<lams:css/>
</HEAD>

<body class="stripes">
	
		<div id="content">
			<p><fmt:message key="msg.loading.learner.window"/></p>
		</div>
	   
		<div id="footer">
		</div><!--closes footer-->

</BODY>
	
</lams:html>
