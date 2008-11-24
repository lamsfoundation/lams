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
				
        <link rel="stylesheet" href="<lams:LAMSURL/>/css/thickbox.css" type="text/css" media="screen">
		
		<title><fmt:message key="learner.title"/></title>

        <script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-latest.pack.js"></script>
        <script type="text/javascript">
			var tb_pathToImage = "<lams:LAMSURL/>/images/loadingAnimation.gif";
		</script>
        <script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/thickbox-compressed.js"></script>
        
        <script type="text/javascript" src="/lams/learning/includes/jsjac-1.3.1/jsjac.js"></script>
		<script type="text/javascript" src="/lams/learning/includes/presence.js"></script>

      	<c:if test="${param.presenceEnabledPatch}"> 		 
        <script type="text/javascript">
     		var HTTPBASE = "<lams:LAMSURL/>JHB";
     		var presenceLabel = "<fmt:message key='label.presence'/>";
     		
	    	window.onload=function(){
				// if presence is enabled, attempt to login once the window is loaded
				attemptLogin();
			}
			
			function attemptLogin(){
				doLogin("${param.presenceUrl}", "<lams:user property="userID"/>", "<lams:user property="userID"/>", "<lams:user property="userID"/>", "${param.lessonID}", "<lams:user property="firstName"/>" + " " + "<lams:user property="lastName"/>", false, false);
			}
			
			function attemptRegistration(){
				$.get("<lams:LAMSURL/>Presence.do", {method: "createXmppId"}, handlePresenceRegistration);
			}
			
			function handlePresenceRegistration(registrationInfo){
				// if registrationInfo exists, registration worked
				if (registrationInfo) {
					// attempt to re-login
					attemptLogin();
				}
			}
			
			function doPresenceClick() {
				$("#roster").slideToggle("slow");
			}
		</script>
		</c:if>
		
	</lams:head>

	<body>
		<c:set var="joinLessonURL"></c:set>
		<div style="float:top">
		<span class="h2font"><c:out value="${param.title}"/></span>&nbsp;&nbsp; &nbsp;&nbsp;
		<a target='_new' href="learner.do?method=displayProgress&lessonID=<c:out value="${param.lessonID}"/>&keepThis=true&TB_iframe=true&height=300&width=400" title="<c:out value="${param.title}"/> - <fmt:message key="label.my.progress"/>" class="thickbox"><fmt:message key="label.my.progress"/></a> &nbsp;&nbsp;
		<a href="#" onclick="javascript:window.location.href='<lams:LAMSURL/>/home.do?method=learner&lessonID=<c:out value="${param.lessonID}"/>'"/><fmt:message key="label.resume"/></a> &nbsp;&nbsp;
		<c:if test="${param.portfolioEnabled}"><a target='_new' href="exportWaitingPage.jsp?mode=learner&lessonID=<c:out value="${param.lessonID}"/>&hideClose=true&keepThis=true&TB_iframe=true&height=300&width=400" title="<fmt:message key="label.export.portfolio"/>" class="thickbox"><fmt:message key="label.export.portfolio"/></a> &nbsp;&nbsp;</c:if>
		<c:if test="${param.presenceEnabledPatch}"><a href=javascript:doPresenceClick()><fmt:message key="label.presence"/></a>&nbsp;&nbsp;</c:if>
		<a href="#" onclick="javascript:window.open('notebook.do?method=viewAll&lessonID=<c:out value="${param.lessonID}"/>')"><fmt:message key="mynotes.title"/></a> &nbsp;&nbsp;
		<lams:help style="small" page="learner"/>
		</div> 
		
		<div id="roster" style="position: absolute; width: 150px; right: 18px; bottom: 0;background: #ffffff; display: none; padding: 5px; border: 1px #000000 solid;"></div>
		
		<iframe onload="javascript:resizeIframe()" id="contentFrame" name="contentFrame"  frameborder="no" scrolling="yes"  src="learner.do?method=joinLesson&lessonID=<c:out value="${param.lessonID}"/>" width="100%" ></iframe>
		<script type="text/javascript">
		
		function resizeIframe() {
		    var height = top.window.innerHeight;
		    if ( height == undefined || height == 0 ) {
		    	// IE doesn't use window.innerHeight.
		    	height = document.documentElement.clientHeight;
		    	// alert("using clientHeight");
		    }
			// alert("doc height "+height);
		    height -= document.getElementById('contentFrame').offsetTop;
		    document.getElementById('contentFrame').style.height = height +"px";
		};
		window.onresize = resizeIframe;
		</script>
	</body>

</lams:html>
