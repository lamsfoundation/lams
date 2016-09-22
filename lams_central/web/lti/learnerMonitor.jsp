<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-function" prefix="fn"%>
<c:set var="lams" ><lams:LAMSURL/></c:set>
<c:set var="pngImageSrc" value="${lams}www/secure/learning-design-images/${ldId}.png" />
<c:set var="svgImageSrc" value="${lams}www/secure/learning-design-images/${ldId}.svg" />

<lams:html>
<lams:head>
	<title><fmt:message key="title.author.window"/></title>
	
	<lams:css/>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/css.browser.selector.js"></script>
	<style type="text/css"> 
		#progress-area {
			text-align: center; 
			border: #d3d3d3 1px solid;
			margin: 10px;
			padding: 10px 10px 2px;
		}
		.smalltext {
			text-align:right;
			font-size: 0.75em;
		}
		.super {
			position:relative;
			bottom:0.5em;
			color:red;
			font-size:0.8em;
		}
		.progress-header {
			text-align:center;
			font-weight: bold;
			padding-top: 5px; 
			padding-bottom:5px;
		}
		.left-text-align{
			text-align: left;
		}
		#buttons {
		    height: 30px;
		    padding-top: 10px;
		}
		
		div.svg-object { display: none }
		.ie div.svg-object { display: inline }
		.ie img.svg { display: none }
		.gecko div.svg-object { display: inline }
		.gecko img.svg { display: none }
		
		#sequence-preview {padding: 10px; text-align: center;}
	</style>
	
	<script type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="loadVars.jsp"></script>
	<script type="text/javascript" src="includes/javascript/openUrls.js"></script>
	
</lams:head>

<body class="stripes">
	
	<div id="content">
	
		<h1>
			${title}
		</h1>

		<c:if test="${not empty description}">
		    <div class="vtbegenerated"> 
		    	${description} 
		    </div>
		</c:if>
    
		<c:if test="${isDisplayDesignImage}">
			<div id="sequence-preview">
				<img src="${pngImageSrc}" alt="Sequence Preview" class="svg" />
				<div class="svg-object">
					<object data="${svgImageSrc}" type="image/svg+xml">
			 			<img src="${pngImageSrc}" alt="Sequence Preview"/>
					</object>
				</div>
			</div>
		</c:if>
    
		<c:if test="${fn:length(learnerProgressDto.attemptedActivities) > 0 || learnerProgressDto.lessonComplete}">
		    <div id="progress-area">
		    	<div class="progress-header">
		    		<fmt:message key="label.your.progress"/>
		    	</div>
		    	
		    	<c:choose>    	
			    	<c:when test="${!learnerProgressDto.lessonComplete}">
				    	<p class="left-text-align">
				    		<fmt:message key="label.lesson.not.completed"/>
				    	</p>
				    	
				    	<p class="left-text-align">
				    		<fmt:message key="label.you.completed.activities">
				    			<fmt:param>
				    				${fn:length(learnerProgressDto.completedActivities)}
				    			</fmt:param>
				    		</fmt:message> 
				    		<span class="super">[*]</span>
				    	</p>
				    	
				    	<div class="smalltext">
				    		<span class="super">*</span>
				    		<fmt:message key="label.total.activities.depend.on.path"/>
				    	</div>
				    </c:when>
			    	<c:otherwise>
			    	 	<p>
				    		<fmt:message key="label.you.completed.this.lesson"/>
				    	</p>
			    	 </c:otherwise>
		    	 </c:choose>
		    </div>
		</c:if>	
		
		<div class="" id="buttons">
	        <c:if test="${isMonitor}">
		        <input type="button" onclick="javascript:openMonitorLesson(${lessonId}); return false;" 
						class="button float-right" value="<fmt:message key="label.open.monitor" />" >
	        </c:if>
	        
			<input type="button" onclick="javascript:openLearner(${lessonId}); return false;" 
					class="button float-right space-right" value="<fmt:message key="label.open.lesson" />" >
        </div>

	</div>
	   
	<div id="footer"></div>

</body>	
</lams:html>
