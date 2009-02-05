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
	
	  <script type="text/javascript">
	   //First, set some JavaScript constants so included scripts can use them
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
	  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.dimensions.pack.js"></script>
	  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.cluetip.js"></script>
	  <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/pedagogicalPlanner.js"></script>
	  <script language="JavaScript" type="text/javascript">
	  /* Add tooltips for Editing advice. First we make a call to tool's Action and check if there is any Editing advice.
	  	 If yes, we show the previously hidden link and add clueTip functionality to it. */
	  	$(document).ready(function() {
	  		var activityCount = ${fn:length(planner.activities)};
	  		for (var activityIndex = 1;activityIndex<=activityCount;activityIndex++){
	  			var editingAdviceLink = $('#editingAdvice'+activityIndex);
	  			if (editingAdviceLink.length > 0){
	  				var checkEditingAdviceUrl = $(editingAdviceLink).attr("rel");
	  				//Ajax call to get the answer: do we show the link or no
	  				$.get(
	  					checkEditingAdviceUrl,
	  					function(responseText){
	  						//After response we parse the message: <answer>&<activity index>
	  						var responseParts = responseText.split("&");
    						var activityInnerIndex = responseParts[1];
    						var editingAdviceExists = responseParts[0]=="OK";
	  						if (editingAdviceExists){
	  							$('#editingAdvice'+activityInnerIndex).show().cluetip({
	  								width: 480,
									cluetipClass: 'jtip',
									arrows: true,
									dropShadow: false,
						 			sticky: true,
									waitImage: true,
									closeText: '<fmt:message key="label.authoring.close" />',
									closePosition: 'title',
									attribute: 'href',
									activation: 'click',
									fx: {             
                      					open: 'slideDown'
    								}
								});
	  						}
	  					},
	  					"html"
	  				);
	  			}
	  		}

		});
	  </script>
	  
	  <lams:css style="main" />
	  <link href="<lams:LAMSURL />css/pedagogicalPlanner.css" rel="stylesheet" type="text/css">
	  <link href="<lams:LAMSURL />css/jquery.cluetip.css" rel="stylesheet" type="text/css" />
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
		<c:set var="lastParentActivityTitle" />
		<c:set var="lastGroup" />
		<c:forEach var="activity" varStatus="activityStatus" items="${planner.activities}">
			<c:if test="${not empty activity.complexActivityType and not (activity.parentActivityTitle eq lastParentActivityTitle)}">
				<c:set var="lastParentActivityTitle" value="${activity.parentActivityTitle}" />
				<tr>
					<td colspan="2" class="
						<c:choose>
							<c:when test="${activity.complexActivityType eq 1}">
								branchingFirstActivity
							</c:when>
							<c:when test="${activity.complexActivityType eq 2}">
								optionsFirstActivity
							</c:when>
						</c:choose>
					">
						<h2>${lastParentActivityTitle}</h2>
					</td>
				</tr>
			</c:if>
			<tr>
				<td class="titleCell
				<c:choose>
					<c:when test="${activity.complexActivityType eq 1}">
						branch
					</c:when>
					<c:when test="${activity.complexActivityType eq 2}">
						option
					</c:when>
				</c:choose>
				<c:if test="${not empty activity.group}">
					group${activity.group}
				</c:if>
				<c:if test="${activity.lastNestedActivity}">
					<c:choose>
						<c:when test="${activity.complexActivityType eq 1}">
							branchingLastActivity
						</c:when>
						<c:when test="${activity.complexActivityType eq 2}">
							optionsLastActivity
						</c:when>
					</c:choose>
				</c:if>
				">
					<c:if test="${not empty activity.group and not (activity.group eq lastGroup)}">
						<c:set var="lastGroup" value="${activity.group}" />
						<h3 class="group${lastGroup}">
							<c:choose>
								<c:when test="${activity.complexActivityType eq 1}">
									<fmt:message key="label.planner.branch" />${lastGroup}
									<c:if test="${activity.defaultBranch}">
										<br /><fmt:message key="label.planner.branch.default" />
									</c:if>
								</c:when>
								<c:when test="${activity.complexActivityType eq 2}">
									<fmt:message key="label.planner.option" />${lastGroup}
								</c:when>
							</c:choose>
						</h3>
					</c:if>
					<c:if test="${not empty activity.type}">
						<span>${activity.type}<br /></span>
					</c:if>
					<c:choose>
						<c:when test="${empty activity.toolIconUrl}">
						</c:when>
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
					<c:if test="${not empty activity.title}">
						<h4>${activity.title}</h4>
					</c:if>
				</td>
				<td class="formCell
				<c:if test="${activity.lastNestedActivity}">
					<c:choose>
						<c:when test="${activity.complexActivityType eq 1}">
							branchingLastActivity
						</c:when>
						<c:when test="${activity.complexActivityType eq 2}">
							optionsLastActivity
						</c:when>
					</c:choose>
				</c:if>
				">
					<iframe frameborder="0" marginheight="0" marginwidth="0" name="activity${activityStatus.index+1}" id="activity${activityStatus.index+1}" class="toolFrame"
						src="<lams:LAMSURL/>${activity.pedagogicalPlannerUrl}">
					</iframe>
					<c:if test="${not empty activity.checkEditingAdviceUrl}">
						<a href="<lams:LAMSURL/>${activity.editingAdviceUrl}" title=""
							rel="<lams:LAMSURL/>${activity.checkEditingAdviceUrl}" 
							class="editingAdvice"
							id="editingAdvice${activityStatus.index+1}"><fmt:message key="label.planner.editing.advice" /></a>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="titleCell
				<c:if test="${not empty activity.group and not activity.lastNestedActivity}">
					<c:choose>
						<c:when test="${activity.complexActivityType eq 1}">
							branch
						</c:when>
						<c:when test="${activity.complexActivityType eq 2}">
							option
						</c:when>
					</c:choose>
					group${activity.group}
				</c:if>
				"
				<c:if test="${activity.lastNestedActivity}">
					style="padding-top: 5px;"
				</c:if>
				>
				
					<c:if test="${not activityStatus.last}">
						<img src="<lams:LAMSURL/>images/pedag_down_arrow.gif" />
					</c:if>
				</td>
				<td>
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<div id="pedagogicalPlannerErrorArea" style="display:none;" class="warning" >
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
		<a class="button pedagogicalPlannerButtons" href="javascript:closePlanner('<fmt:message key="msg.planner.not.saved" />');"><fmt:message key="button.close" /></a>
		<a class="button pedagogicalPlannerButtons" href="javascript:submitAll(ACTION_OPEN_AUTHOR,${planner.learningDesignID})"><fmt:message key="button.planner.view.full.author" /></a>
		<a class="button pedagogicalPlannerButtons" href="javascript:submitAll(ACTION_PREVIEW,'${startPreviewUrl}')"><fmt:message key="button.planner.preview" /></a>
		<a class="button pedagogicalPlannerButtons" href="javascript:submitAll(ACTION_DO_NOTHING,null)"><fmt:message key="button.planner.save" /></a>
	</div>
</div>
<div id="footer"></div>
</div>
</body>
</lams:html>