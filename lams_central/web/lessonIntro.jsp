<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-function" prefix="fn"%>
<c:set var="lams" ><lams:LAMSURL/></c:set>
<c:set var="pngImageSrc" value="${lams}www/secure/learning-design-images/${learningDesignID}.png" />
<c:set var="svgImageSrc" value="${lams}www/secure/learning-design-images/${learningDesignID}.svg" />

<lams:html>
<lams:head>
	<TITLE><fmt:message key="title.learner.window"/></TITLE>
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/thickbox.js"></script>
	
	<lams:css/>
	<link rel="stylesheet" href="css/thickbox.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/javascript" src="includes/javascript/css.browser.selector.js"></script>	
	<style media="screen,projection" type="text/css">
		div.svg-object { display: none }
		.ie div.svg-object { display: inline }
		.ie img.svg { display: none }
		.gecko div.svg-object { display: inline }
		.gecko img.svg { display: none }
		
		#sequence-preview {padding: 10px; text-align: center;}
	</style>	
	
</lams:head>

<body class="stripes">
	
	<div id="content">
		
		<c:if test="${isMonitor}">
			 <div class="float-right small-space-top">
			 	<a class="thickbox button" href="editLessonIntro.do?method=edit&lessonID=${lesson.lessonId}&KeepThis=true&TB_iframe=true&height=600&width=850" title="<fmt:message key='label.edit'/>">
					<fmt:message key="label.edit"/>
				</a>
			</div>
		</c:if>
	
		<h1><c:out value="${lesson.lessonName}" escapeXml="true"/></h1>
	
		<p><c:out value="${lesson.lessonDescription}" escapeXml="false"/></p>	
	
		<c:if test="${displayDesignImage}">
			<div id="sequence-preview">
				<img src="${pngImageSrc}" alt="Sequence Preview" class="svg" />
				<div class="svg-object">
					<object data="${svgImageSrc}" type="image/svg+xml">
			 			<img src="${pngImageSrc}" alt="Sequence Preview"/>
					</object>
				</div>
			</div>
		</c:if>
			
		<div class="space-bottom-top align-right">
			<html:link href="${fn:escapeXml(learnerURL)}" styleClass="button">
				<span class="nextActivity"><fmt:message key="label.start.lesson" /></span>
			</html:link>
		</div>

	</div>
	   
	<div id="footer"></div>

</BODY>
	
</lams:html>
