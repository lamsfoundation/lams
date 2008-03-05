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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<lams:html>
	
	<lams:head>
		<lams:css/>
        <link rel="stylesheet" href="<lams:LAMSURL/>/css/thickbox.css" type="text/css" media="screen">
		
		<title><fmt:message key="learner.title"/></title>

        <script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-latest.pack.js"></script>
        <script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/thickbox-compressed.js"></script>
        
	</lams:head>

	<c:set var="enableFlash"><lams:LearnerFlashEnabled/></c:set>
	
	<c:choose>
		<c:when test="${enableFlash}">
			<c:choose>
				<c:when test="${page_direction == 'RTL'}">
					<frameset rows="*" cols="*,160">
						<frame src="content.do?lessonID=<c:out value="${param.lessonID}"/>" name="contentFrame" scrolling="YES">
						<frame src="controlFrame.jsp?lessonID=<c:out value="${param.lessonID}"/><c:if test="${param.mode != null}">&mode=<c:out value="${param.mode}"/></c:if>" name="controlFrame" scrolling="NO">
					</frameset>	
				</c:when>
				<c:otherwise>
					<frameset rows="*" cols="160,*">
						<frame src="controlFrame.jsp?lessonID=<c:out value="${param.lessonID}"/><c:if test="${param.mode != null}">&mode=<c:out value="${param.mode}"/></c:if>" name="controlFrame" scrolling="NO">
						<frame src="content.do?lessonID=<c:out value="${param.lessonID}"/>" name="contentFrame" scrolling="YES">
					</frameset>
				</c:otherwise>
			</c:choose>
			<noframes>
				<body>
					<fmt:message key="message.activity.parallel.noFrames" />
				</body>
			</noframes>
		</c:when>
		<c:otherwise>
			<c:set var="joinLessonURL"></c:set>
			<body>
				<div style="float: top">
				<a target='_new' href="learner.do?method=displayProgress&lessonID=<c:out value="${param.lessonID}"/>&keepThis=true&TB_iframe=true&height=300&width=400" title="<fmt:message key="label.my.progress"/>" class="thickbox"><fmt:message key="label.my.progress"/></a>
				<a href="#" onclick="location.reload()"/><fmt:message key="label.resume"/></a>
				<a target='_new' href="exportWaitingPage.jsp?mode=learner&lessonID=<c:out value="${param.lessonID}"/>&hideClose=true&keepThis=true&TB_iframe=true&height=300&width=400" title="<fmt:message key="label.export.portfolio"/>" class="thickbox"><fmt:message key="label.export.portfolio"/></a>
				<a href="#" onclick="javascript:window.open('notebook.do?method=viewAll&lessonID=<c:out value="${param.lessonID}"/>')"><fmt:message key="mynotes.title"/></a>
				</div> 
				
				<iframe onload="javascript:resizeIframe()" id="contentFrame" name="contentFrame"  frameborder="no" scrolling="no"  src="learner.do?method=joinLesson&lessonID=<c:out value="${param.lessonID}"/>" width="100%" ></iframe>
				<script type="text/javascript">
				function resizeIframe() {
				    var height = document.documentElement.clientHeight;
				    height -= document.getElementById('contentFrame').offsetTop;
				    document.getElementById('contentFrame').style.height = height +"px";
				};
				window.onresize = resizeIframe;
				</script>
			</body>
		</c:otherwise>
	</c:choose>
	

</lams:html>
