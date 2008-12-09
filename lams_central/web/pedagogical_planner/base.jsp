<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-function" prefix="fn" %>


<style type="text/css">
    .toolFrame {
     width: 550px;
     border: 1px lightgrey solid;
    }
    
    .pedagogicalPlannerButtons {
      margin-right: 10px;
      float: right;
    }
    
    .pedagogicalPlannerTable{
      table-layout: fixed;
    }
    
    .titleCell {
    	padding: 0px;
    	width: 85px;
    	text-align: center;
    }
    
    .formCell {
    	width: 550px;
    }
    
    #pedagogicalPlannerInfoArea {
	color: #000000;
	padding:8px 10px 10px 40px;
	margin-top: 25px;
	margin-left: auto;
	margin-right: auto;
	margin-bottom: 20px;
	text-align: left;
	font-weight: normal;
	background: url('images/css/edit.gif') no-repeat #d8e4f1 10px 8px;
	width: 60%;
	border: 1px solid #3c78b5;
	float: none;
	}
  </style>
  
  <script type="text/javascript">
   var activityCount = ${fn:length(planner.activities)}; //How many activities are there in the sequence
   var activitySupportingPlannerCount = ${planner.activitySupportingPlannerCount}; //How many of activities support the planner (their data will be submitted)
   var sendInPortions = ${planner.sendInPortions}; //Should the forms be send all at once or rather in parts
   var activitiesInPortion =  ${planner.activitiesInPortion}; //After how many submitted forms the script should pause
   var submitDelay =  ${planner.submitDelay}; //How many miliseconds should the script wait before sending another portion of forms
   var saveDetailsUrl = "<c:url value='/pedagogicalPlanner.do'/>";
   var errorPlannerNotSaved = '<fmt:message key="planner.not.saved" />';
  </script>
  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-latest.pack.js"></script>
  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/pedagogicalPlanner.js"></script>

<div style="clear:both"></div>
<h2 class="small-space-top"><fmt:message key="planner.title" /></h2>

<div class="shading-bg">
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
				<span style="font-size: smaller;">${activity.type}<br /></span>
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

<div  id="pedagogicalPlannerErrorArea" style="display:none; margin-bottom: 20px; width: 60%;" class="warning" >
</div>

<div  id="pedagogicalPlannerInfoArea" style="display:none;" class="info" >
	<fmt:message key="planner.saved" />
</div>

<div id="pedagogicalPlannerBusy" style="display:none; margin: 0px 0px 20px 40%;">
	<img src="<lams:LAMSURL />images/loadingAnimation.gif"  />
</div>

<c:url var="startPreviewUrl" value="/pedagogicalPlanner.do">
	<c:param name="method" value="startPreview" />
	<c:param name="ldId" value="${planner.learningDesignID}" />
</c:url>

<a class="button pedagogicalPlannerButtons" href="javascript:submitAll(ACTION_OPEN_AUTHOR,null)"><fmt:message key="button.planner.view.full.author" /></a>
<a class="button pedagogicalPlannerButtons" href="javascript:submitAll(ACTION_PREVIEW,'${startPreviewUrl}')"><fmt:message key="button.planner.preview" /></a>
<a  class="button pedagogicalPlannerButtons" href="javascript:submitAll(ACTION_DO_NOTHING,null)"><fmt:message key="button.planner.save" /></a>