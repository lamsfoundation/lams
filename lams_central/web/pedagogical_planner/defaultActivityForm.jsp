<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%-- Page that is displayed if activity does not support the pedagogical planner. --%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<lams:html>
<lams:head>
	<lams:css style="core" />
</lams:head>
<body>	
	<div style="width: 100%; margin-top: 90px; text-align: center; vertical-align: middle; font-weight: bold;">
		<h3>
			<c:choose>
				<c:when test="${empty param.formMessage}">
					<fmt:message key="label.planner.not.supported" />
				</c:when>
				<c:otherwise>
					${param.formMessage}
				</c:otherwise>
			</c:choose>
		</h3>
	</div>
</body>
</lams:html>