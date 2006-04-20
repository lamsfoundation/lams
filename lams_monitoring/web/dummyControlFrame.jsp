<!--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
-->

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
      <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	  <lams:css/>

<title>Lessons</title>
</head>
  <body>
  
	<H2>Select a lesson</H2>
	
	<p>Note: If you archive a lesson, it can no longer be accessed. This is a temporary restriction.</p>
		
	<table width="100%" border="0" cellspacing="2" cellpadding="2" summary="This table is being used for layout purposes only">
		<tr><td><A HREF="<lams:WebAppURL/>dummy.do?method=initStartScreen" target="contentFrame">Start new lesson</a></td></tr>	
		<tr><td><A HREF="<lams:WebAppURL/>dummy.do">Refresh List</a></td></tr>	
		<TR><TD><HR></TD></TR>
		<TR><TD>Current Lessons</TD></TR>
		<c:forEach var="lesson" items="${lessons}">
			<c:if test="${lesson.lessonStateId<6}">
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
				<BR><A HREF="<lams:WebAppURL/>/dummy.do?method=archiveLesson&lessonID=<c:out value="${lesson.lessonId}"/>">Archive Lesson</A>
				</TD></TR>
			<TR><TD><HR></TD></TR>
			</c:if>
		</c:forEach>
		<TR><TD><HR></TD></TR>
		<TR><TD>Archived Lessons</TD></TR>
		<c:forEach var="lesson" items="${lessons}">
			<c:if test="${lesson.lessonStateId==6}">
				<TR><TD><STRONG><c:out value="${lesson.lessonName}"/></STRONG>
				<BR><c:out value="${lesson.lessonDescription}"/>
				<BR><A HREF="<lams:WebAppURL/>/dummy.do?method=getLesson&lessonID=<c:out value="${lesson.lessonId}"/>" target="contentFrame">Monitor Lesson</A>
				<BR><A HREF="<lams:WebAppURL/>/dummy.do?method=removeLesson&lessonID=<c:out value="${lesson.lessonId}"/>">Remove Lesson</A>
				</TD></TR>
			<TR><TD><HR></TD></TR>
			</c:if>
		</c:forEach>
		<!-- Don't display the removed lessons ie lessonStateId=7 -->
	</table>
	
	</body>
</html>
