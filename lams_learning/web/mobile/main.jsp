<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<lams:html>
	<lams:head>
		<lams:css/>
		<link href="<c:url value="/"/>css/main.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" href="<lams:LAMSURL/>css/thickbox.css" type="text/css" media="screen">
        
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/thickbox.js"></script>
		
		<script type="text/javascript">

			function resizeIframe() {
			    var height = $(window).height();

			    height -= $('#contentFrame').offset().top;
			    $('#contentFrame').css({'height': height + "px"});
			    
			    <c:if test="${presenceEnabledPatch}">
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
		<span class="h2font"><c:out value="${title}"/></span>&nbsp;&nbsp; &nbsp;&nbsp;
		<a target='_new' href="<c:url value="/"/>learner.do?method=displayProgress&lessonID=<c:out value="${lessonID}"/>&keepThis=true&TB_iframe=true&height=300&width=400" title="<c:out value="${title}"/> - <fmt:message key="label.my.progress"/>" class="thickbox"><fmt:message key="label.my.progress"/></a> &nbsp;&nbsp;
		<a href="#" onclick="javascript:window.location.href='<lams:LAMSURL/>/home.do?method=learner&lessonID=<c:out value="${lessonID}"/>'"/><fmt:message key="label.resume"/></a> &nbsp;&nbsp;
		<a href="#" onclick="javascript:window.open('<c:url value="/"/>notebook.do?method=viewAll&lessonID=<c:out value="${lessonID}"/>')"><fmt:message key="mynotes.title"/></a> &nbsp;&nbsp;
		<lams:help style="small" page="learner"/>
		</div>

		<c:if test="${presenceEnabledPatch}">
		    <%@ include file="../presenceChat.jsp" %>
		</c:if>
		 
		<iframe onload="javascript:resizeIframe()" id="contentFrame" name="contentFrame" frameborder="no" scrolling="auto"  src="<c:url value="/"/>learner.do?method=joinLesson&lessonID=<c:out value="${lessonID}"/>" width="100%" ></iframe>
	</body>

</lams:html>