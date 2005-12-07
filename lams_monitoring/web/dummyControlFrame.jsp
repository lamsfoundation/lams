<%@ page language="java" import="java.util.*" %>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
	  <lams:css/>

<title>Lessons</title>
</head>
  <body>
  
	<H2>Select a lesson</H2>
		
	<table width="100%" border="0" cellspacing="2" cellpadding="2" summary="This table is being used for layout purposes only">
		<tr><td><A HREF="<lams:WebAppURL/>dummy.do?method=initStartScreen" target="contentFrame">Start new lesson</a></td></tr>	
		<tr><td><A HREF="<lams:WebAppURL/>dummy.do">Refresh List</a></td></tr>	
		<TR><TD><HR></TD></TR>
		<c:forEach var="lesson" items="${lessons}">
			<TR><TD><STRONG><c:out value="${lesson.lessonName}"/></STRONG>
			<c:choose>
				<c:when test="${lesson.lessonStateId==1}">[Created]</c:when>
				<c:when test="${lesson.lessonStateId==2}">[Scheduled]</c:when>
				<c:when test="${lesson.lessonStateId==3}">[Started]</c:when>
				<c:when test="${lesson.lessonStateId==4}">[Suspended]</c:when>
				<c:when test="${lesson.lessonStateId==5}">[Finished]</c:when>
			</c:choose>
			<BR><c:out value="${lesson.lessonDescription}"/>
			<BR><A HREF="<lams:WebAppURL/>/dummy.do?method=getLesson&lessonID=<c:out value="${lesson.lessonId}"/>" target="contentFrame">Monitor Lesson</A>
			</TD></TR>
			<TR><TD><HR></TD></TR>
		</c:forEach>
	</table>
	
	</body>
</html>
