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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<lams:html>
	<lams:head>
		<lams:css/>
        <link rel="stylesheet" href="<lams:LAMSURL/>css/thickbox.css" type="text/css" media="screen">
        
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/thickbox.patched.js"></script>
		
		<script type="text/javascript">
			var tb_pathToImage = "<lams:LAMSURL/>/images/loadingAnimation.gif";

			function resizeIframe() {
			    var height = $(window).height();

			    height -= $('#contentFrame').offset().top;
			    $('#contentFrame').css({'height': height + "px"});
			    
			    <c:if test="${param.presenceEnabledPatch}">
			    	resizeChat();
			    </c:if>
			}
			
			window.onresize = resizeIframe;
		</script>
		
		<title><fmt:message key="learner.title"/></title>
	</lams:head>

	<body>
		<c:set var="joinLessonURL"></c:set>
		<div style="float:bottom">
		<span class="h2font"><c:out value="${param.title}"/></span>&nbsp;&nbsp; &nbsp;&nbsp;
		<a target='_new' href="learner.do?method=displayProgress&lessonID=<c:out value="${param.lessonID}"/>&keepThis=true&TB_iframe=true&height=300&width=400" title="<c:out value="${param.title}"/> - <fmt:message key="label.my.progress"/>" class="thickbox"><fmt:message key="label.my.progress"/></a> &nbsp;&nbsp;
		<a href="#" onclick="javascript:window.location.href='<lams:LAMSURL/>/home.do?method=learner&lessonID=<c:out value="${param.lessonID}"/>'"/><fmt:message key="label.resume"/></a> &nbsp;&nbsp;
		<c:if test="${param.portfolioEnabled}"><a target='_new' href="exportWaitingPage.jsp?mode=learner&lessonID=<c:out value="${param.lessonID}"/>&hideClose=true&keepThis=true&TB_iframe=true&height=300&width=400" title="<fmt:message key="label.export.portfolio"/>" class="thickbox"><fmt:message key="label.export.portfolio"/></a> &nbsp;&nbsp;</c:if>
		<a href="#" onclick="javascript:window.open('notebook.do?method=viewAll&lessonID=<c:out value="${param.lessonID}"/>')"><fmt:message key="mynotes.title"/></a> &nbsp;&nbsp;
		<lams:help style="small" page="learner"/>
		</div>
		
		<c:if test="${param.presenceEnabledPatch}">
		    <%@ include file="/includes/presenceChat.jsp" %>
		</c:if>
		
		<iframe onload="javascript:resizeIframe()" id="contentFrame" name="contentFrame" frameborder="no" scrolling="auto"  src="learner.do?method=joinLesson&lessonID=<c:out value="${param.lessonID}"/>" width="100%" ></iframe>
	</body>

</lams:html>
