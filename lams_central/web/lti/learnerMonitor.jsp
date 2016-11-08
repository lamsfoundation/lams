<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-function" prefix="fn"%>
<c:set var="lams" ><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="title.author.window"/></title>
	
	<lams:css/>
	<style type="text/css">
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
		#sequence-preview {
			padding: 10px; 
			text-align: center;
		}
	</style>
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="loadVars.jsp"></script>
	<script type="text/javascript" src="includes/javascript/openUrls.js"></script>
	
</lams:head>

<body class="stripes">
	
	<lams:Page type="learner" title="${title}">

		<c:if test="${not empty description}">
			<p>
				<c:out value="${description}" escapeXml="false"/>
			</p>
		</c:if>
    
		<c:if test="${isDisplayDesignImage}">
			<div id="sequence-preview">
				<img src="<lams:LAMSURL/>home.do?method=getLearningDesignThumbnail&ldId=${ldId}" alt="Sequence Preview" />
			</div>
		</c:if>
    
		<c:if test="${fn:length(learnerProgressDto.attemptedActivities) > 0 || learnerProgressDto.lessonComplete}">
			<lams:SimplePanel titleKey="label.your.progress">
		    	
		    	<c:choose>    	
			    	<c:when test="${!learnerProgressDto.lessonComplete}">
				    	<p>
				    		<fmt:message key="label.lesson.not.completed"/>
				    	</p>
				    	
				    	<p>
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
		    </lams:SimplePanel>
		</c:if>	
		
		<div class="voffset10 pull-right">
	        <c:if test="${isMonitor}">
	        	<a class="btn btn-default" href="javascript:openMonitorLesson(${lessonId});" title="<fmt:message key='label.open.monitor'/>">
					<fmt:message key="label.open.monitor" />
				</a>
	        </c:if>
	        
	        <a class="btn btn-default" href="javascript:openLearner(${lessonId});" title="<fmt:message key='label.open.lesson'/>">
				<fmt:message key="label.open.lesson" />
			</a>
        </div>
        
		<div id="footer"></div>

	</lams:Page>

</body>	
</lams:html>
