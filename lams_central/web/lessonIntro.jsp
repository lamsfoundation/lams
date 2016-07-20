<!DOCTYPE html>

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
		
		#TB_iframeContent {width: 820px !important}
	</style>	
	
</lams:head>

<body class="stripes">
	
	<c:set var="title"><c:out value="${lesson.lessonName}" escapeXml="true"/></c:set>
	<lams:Page type="learner" title="${title}">
		
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
			
		<div class="voffset10 pull-right">

			<c:if test="${isMonitor}">
				 <div class="btn btn-default">
				 	<a class="thickbox" href="editLessonIntro.do?method=edit&lessonID=${lesson.lessonId}&KeepThis=true&TB_iframe=true&height=600&width=800" title="<fmt:message key='label.edit'/>">
						<fmt:message key="label.edit"/>
					</a>
				</div>
			</c:if>

			<html:link href="${lams}home.do?method=learner&lessonID=${lesson.lessonId}&isLessonIntroWatched=true" styleClass="btn btn-primary na">
				<span class="nextActivity"><fmt:message key="label.start.lesson" /></span>
			</html:link>
		</div>
	
		<div id="footer"></div>
	
	</lams:Page>

</BODY>
	
</lams:html>
