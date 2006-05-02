<%-- 
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
--%>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="<lams:LAMSURL/>/css/default.css" rel="stylesheet" type="text/css"/>
		<title>Learner :: LAMS</title>
		<script language="JavaScript" type="text/JavaScript">
			<!--
			
			var thePopUp = null;
			
			function openPopUp(args){
				if(thePopUp && thePopUp.open && !thePopUp.closed){		
						thePopUp.focus();	
				}else{
					//alert('opening:'+args);
					thePopUp = window.open(args,"learnerPop","HEIGHT=400,WIDTH=550,resizable,scrollbars");
				}
			}
			
			//-->
		</script>

	</head>
	<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

	   <H2>Flash Interface</H2>
		<p>Switch to the <A HREF="controlFrame.jsp">Flash interface<a/> (under development).</p>

		<h2>Available Lessons</H2>
		<table width="100%" border="0" cellspacing="2" cellpadding="2" summary="This table is being used for layout purposes only">
			<TR><TD><A HREF="<lams:WebAppURL/>dummylearner.do?method=getActiveLessons">Refresh List</A></TD></TR>
		<c:forEach var="lesson" items="${lessons}">
		<c:if test="${lesson.lessonStateID<6}">
			<TR><TD>
				<STRONG><c:out value="${lesson.lessonName}"/></STRONG>
			<c:choose>
				<c:when test="${lesson.lessonStateID==1}">[Created]</c:when>
				<c:when test="${lesson.lessonStateID==2}">[Scheduled]</c:when>
				<c:when test="${lesson.lessonStateID==3}">[Started]</c:when>
				<c:when test="${lesson.lessonStateID==4}">[Suspended]</c:when>
				<c:when test="${lesson.lessonStateID==5}">[Finished]</c:when>
			</c:choose>
			<c:if test="${lesson.lessonDescription}">
				<BR><c:out value="${lesson.lessonDescription}"/>
			</c:if>
			<BR><A HREF="<lams:WebAppURL/>dummylearner.do?method=joinLesson&userId=<lams:user property="userID"/>&lessonId=<c:out value="${lesson.lessonID}"/>" target="contentFrame">Participate</A>
			<BR><A HREF=javascript:openPopUp('<lams:WebAppURL/>exportWaitingPage.jsp?mode=learner&lessonID=<c:out value="${lesson.lessonID}"/>');>Export Portfolio</A>
			</TD></TR>
			<TR><TD><HR></TD></TR>
		</c:if>
		</c:forEach>
		</table>

	</body>
</html:html>
