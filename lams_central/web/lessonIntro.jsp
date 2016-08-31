<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-function" prefix="fn"%>
<c:set var="lams" ><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<TITLE><fmt:message key="title.learner.window"/></TITLE>
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/thickbox.js"></script>
	
	<lams:css/>
	<link rel="stylesheet" href="css/thickbox.css" type="text/css" media="screen">
	<style media="screen,projection" type="text/css">
		#sequence-preview {padding: 10px; text-align: center;}
		#TB_iframeContent {width: 820px !important}
	</style>	
	
</lams:head>

<body class="stripes">
	
	<c:set var="title"><c:out value="${lesson.lessonName}" escapeXml="true"/></c:set>
	<lams:Page type="learner" title="${title}">
		
		<p><c:out value="${lesson.lessonDescription}" escapeXml="false"/></p>	
	
		<c:if test="${displayDesignImage}">
			<div id="sequence-preview">
				<img src="<lams:LAMSURL/>home.do?method=getLearningDesignThumbnail&ldId=${learningDesignID}" alt="Sequence Preview" />
			</div>
		</c:if>
			
		<div class="voffset10 pull-right">

			<c:if test="${isMonitor}">
			 	<a class="thickbox btn btn-default" href="editLessonIntro.do?method=edit&lessonID=${lesson.lessonId}&KeepThis=true&TB_iframe=true&height=600&width=800" title="<fmt:message key='label.edit'/>">
					<fmt:message key="label.edit"/>
				</a>
			</c:if>

			<html:link href="${lams}home.do?method=learner&lessonID=${lesson.lessonId}&isLessonIntroWatched=true" styleClass="btn btn-primary na">
				<span class="nextActivity"><fmt:message key="label.start.lesson" /></span>
			</html:link>
		</div>
	
		<div id="footer"></div>
	
	</lams:Page>

</BODY>
	
</lams:html>
