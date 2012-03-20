<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-html" prefix="html" %>
<c:set var="lams" >
	<lams:LAMSURL/>
</c:set>

<c:set var="startLessonHref">learner.jsp?mode=${mode}&portfolioEnabled=${portfolioEnabled}&presenceEnabledPatch=${presenceEnabledPatch}&presenceImEnabled=${presenceImEnabled}&title=${title}&createDateTime=${createDateTime}&serverUrl=${serverUrl}&presenceUrl=${presenceUrl}&lessonID=${lessonID}</c:set>
<c:set var="pngImageSrc" value="${lams}www/secure/learning-design-images/${learningDesignID}.png" />
<c:set var="svgImageSrc" value="${lams}www/secure/learning-design-images/${learningDesignID}.svg" />

<lams:html>
<lams:head>
	<TITLE><fmt:message key="title.learner.window"/></TITLE>
	
	<lams:css/>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/css.browser.selector.js"></script>	
	<style media="screen,projection" type="text/css">
		div.svgobject { display: none }
		.ie div.svgobject { display: inline }
		.ie img.svg { display: none }
		.gecko div.svgobject { display: inline }
		.gecko img.svg { display: none }
		
		#sequencePreview {padding: 10px; text-align: center;}
	</style>	
	
</lams:head>

<body class="stripes">
	
	<div id="content">
	
		<h1>${lessonName}</h1>
	
		<p>${lessonDescription}</p>	
	
		<c:if test="${displayDesignImage}">
			<div id="sequencePreview">
				<img src="${pngImageSrc}" alt="Sequence Preview" class="svg" />
				<div class="svgobject">
					<object data="${svgImageSrc}" type="image/svg+xml">
			 			<img src="${pngImageSrc}" alt="Sequence Preview"/>
					</object>
				</div>
			</div>
		</c:if>
			
		<div class="space-bottom-top align-right">
			<html:link href="${startLessonHref}" styleClass="button">
				<span class="nextActivity"><fmt:message key="label.start.lesson" /></span>
			</html:link>
		</div>
			
	</div>
	   
	<div id="footer"></div>

</BODY>
	
</lams:html>
