<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-function" prefix="fn" %>
<lams:html>
<lams:head>
	<title><fmt:message key="title.lams"/> :: <fmt:message key="planner.title" /></title>
	<lams:css style="main" />
	<link href="<lams:LAMSURL />css/pedagogicalPlanner.css" rel="stylesheet" type="text/css">
	  
	  <script type="text/javascript">
	   var activityCount = ${fn:length(planner.activities)}; //How many activities are there in the sequence
	   var activitySupportingPlannerCount = ${planner.activitySupportingPlannerCount}; //How many of activities support the planner (their data will be submitted)
	   var sendInPortions = ${planner.sendInPortions}; //Should the forms be send all at once or rather in parts
	   var activitiesInPortion =  ${planner.activitiesInPortion}; //After how many submitted forms the script should pause
	   var submitDelay =  ${planner.submitDelay}; //How many miliseconds should the script wait before sending another portion of forms
	   var saveDetailsUrl = "<c:url value='/pedagogicalPlanner.do'/>";
	   var errorPlannerNotSaved = '<fmt:message key="planner.not.saved" />';
	  </script>
	  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/getSysInfo.js"></script>
	  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>loadVars.jsp"></script>
	  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/openUrls.js"></script>
	  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
	  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
	  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/pedagogicalPlanner.js"></script>
</lams:head>
<body class="stripes">
<div id="page">
<div id="content">
	<h1 style="text-align: center" class="small-space-top"><fmt:message key="planner.title" /></h1>
	
	<div class="shading-bg" style="width: 97%">
		<!-- A small form for learning design title only -->
		<form id="sequenceDetailsForm">
			<table cellspacing="0" cellpadding="0" class="pedagogicalPlannerTable">
				<tr>
					<td class="titleCell">
						<span class="field-name"><fmt:message key="label.title" />:</span>
					</td>
					<td>
						<input type="text" name="sequenceTitle" size="80" value="<c:out value='${planner.sequenceTitle}'/>" />
					</td>
				</tr>
			</table>
			<input type="hidden" id="callAttemptedID" name="callAttemptedID" />
			<input type="hidden" name="ldId" value="${planner.learningDesignID}" />
			<input type="hidden" name="method" value="saveSequenceDetails" />
		</form>
	</div>
	
	<!-- IFrames with activities. -->
	<table cellspacing="0" cellpadding="0" class="pedagogicalPlannerTable">
		<c:forEach var="activity" varStatus="activityStatus" items="${planner.activities}">
			<tr>
				<td class="titleCell">
					<span>${activity.type}<br /></span>
					<c:choose>
						<c:when test="${fn:endsWith(activity.toolIconUrl,'.swf')}">
							<object width="35" height="35">
								<param name="movie" value="<lams:LAMSURL/>${activity.toolIconUrl}" ></param>
								<embed bgcolor="#ffffff" type="application/x-shockwave-flash" src="<lams:LAMSURL/>${activity.toolIconUrl}" width="35" height="35">
								</embed>
							</object>
						</c:when>
						<c:otherwise>
							<img src="<lams:LAMSURL/>${activity.toolIconUrl}" />
						</c:otherwise>
					</c:choose>
	
					<h4>${activity.title}</h4>
				</td>
				<td class="formCell">
				<iframe frameborder="0" marginheight="0" marginwidth="0" name="activity${activityStatus.index+1}" id="activity${activityStatus.index+1}" class="toolFrame"
					src="<lams:LAMSURL/>${activity.pedagogicalPlannerUrl}toolContentID=${activity.toolContentID}">
				</iframe>
				</td>
			</tr>
			<c:if test="${not activityStatus.last}">
				<tr>
					<td class="titleCell">
						<img src="<lams:LAMSURL/>images/pedag_down_arrow.gif" />
					</td>
					<td>
					</td>
				</tr>
			</c:if>
		</c:forEach>
	</table>
	
	<div  id="pedagogicalPlannerErrorArea" style="display:none;" class="warning" >
	</div>
	
	<div  id="pedagogicalPlannerInfoArea" style="display:none;" class="info" >
		<fmt:message key="planner.saved" />
	</div>
	
	<div id="pedagogicalPlannerBusy" style="display:none;">
		<img src="<lams:LAMSURL />images/loadingAnimation.gif"  />
	</div>
	
	<c:url var="startPreviewUrl" value="/pedagogicalPlanner.do">
		<c:param name="method" value="startPreview" />
		<c:param name="ldId" value="${planner.learningDesignID}" />
	</c:url>
	<div id="buttonArea">
		<a class="button pedagogicalPlannerButtons" href="javascript:window.close()"><fmt:message key="button.close" /></a>
		<a class="button pedagogicalPlannerButtons" href="javascript:submitAll(ACTION_OPEN_AUTHOR,${planner.learningDesignID})"><fmt:message key="button.planner.view.full.author" /></a>
		<a class="button pedagogicalPlannerButtons" href="javascript:submitAll(ACTION_PREVIEW,'${startPreviewUrl}')"><fmt:message key="button.planner.preview" /></a>
		<a class="button pedagogicalPlannerButtons" href="javascript:submitAll(ACTION_DO_NOTHING,null)"><fmt:message key="button.planner.save" /></a>
	</div>
</div>
<div id="footer"></div>
</div>
</body>
</lams:html>