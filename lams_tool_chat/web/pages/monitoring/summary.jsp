<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
        <lams:LAMSURL />
</c:set>
<c:set var="dto" value="${requestScope.monitoringDTO}" />

<link type="text/css" href="${lams}/css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">

<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '<c:out value="${param.toolContentID}" />',
		messageNotification: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.notification" /></spring:escapeBody>',
		messageRestrictionSet: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.set" /></spring:escapeBody>',
		messageRestrictionRemoved: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.removed" /></spring:escapeBody>'
	};
	
	$(document).ready(function(){
		initializePortraitPopover("${lams}");
	});

</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>  
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="${lams}/includes/javascript/portrait5.js" ></script>

<div class="panel">
	<h4>
	    <c:out value="${monitoringDTO.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${monitoringDTO.instructions}" escapeXml="false"/>
	</div>
</div>

<c:if test="${isGroupedActivity}">
<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="session" items="${dto.sessionDTOs}" varStatus="status">

	<c:if test="${isGroupedActivity}">	
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${session.sessionID}">
        	<span class="panel-title collapsable-icon-left">
        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${session.sessionID}" 
					aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${session.sessionID}" >
			<c:out value="${session.sessionName}" /></a>
			</span>
        </div>
        
        <div id="collapse${session.sessionID}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${session.sessionID}">
	</c:if>

		<table class="table table-condensed table-no-border">
			<tr>
				<td class="field-name" style="width: 30%;">
					<fmt:message>heading.totalLearners</fmt:message>
				</td>
				<td>
					${session.numberOfLearners}
				</td>
				<td rowspan="2">
					<div class="pull-right">
						<a href="#" onClick="javascript:launchPopup('openChatHistory.do?toolSessionID=${session.sessionID}')" class="btn btn-default btn-sm">
							<fmt:message>summary.editMessages</fmt:message></a>
						<a href="#" onClick="javascript:launchPopup('../learning/learning.do?toolSessionID=${session.sessionID}&mode=teacher')" class="btn btn-default btn-sm">
							<fmt:message>summary.openChat</fmt:message></a>
					</div>			
				</td>
			</tr>
	
			<tr>
				<td class="field-name" style="width: 30%;">
					<fmt:message>heading.totalMessages</fmt:message>
				</td>
				<td>
					${session.numberOfPosts}
				</td>
			</tr>
		</table>			

		<!--  View the Messages -->
		<hr/>
		<div class="loffset5">
			<strong><fmt:message>heading.recentMessages</fmt:message></strong>
			<c:choose>
				<c:when test="${empty session.messageDTOs}">
					<fmt:message>message.noChatMessages</fmt:message>
				</c:when>
				<c:otherwise>
					<c:forEach var="message" items="${session.messageDTOs}">
						<div class="message">
							<div class="messageFrom">
								<lams:Portrait userId="${message.fromUserId}" hover="true">${message.from}</lams:Portrait>
							</div>
							<lams:out escapeHtml="true" value="${message.body}"></lams:out>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
		<!--  End of the Messages -->
	
	<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
	
</c:forEach>

<c:if test="${isGroupedActivity}">
</div> 
</c:if>

<%@include file="advanceOptions.jsp"%>
<%@include file="daterestriction.jsp"%>
