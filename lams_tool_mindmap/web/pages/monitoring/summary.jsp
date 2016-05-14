<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="dto" value="${mindmapDTO}" />

<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">

<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring.do?dispatch=setSubmissionDeadline"/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script> 
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript">
	var evalcomixWindow = null;
	function openEvalcomixWindow(url) {
    	evalcomixWindow=window.open(url, 'evalcomixWindow', 'width=800,height=600,scrollbars=yes,resizable=yes');
		if (window.focus) {evalcomixWindow.focus()}
	}
</script>

<div class="panel">
	<h4>
	  <c:out value="${mindmapDTO.title}" escapeXml="true"/>
	</h4>
	
	<div class="instructions voffset5">
	  <c:out value="${mindmapDTO.instructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty dto.sessionDTOs}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="label.nogroups" />
		</lams:Alert>
	</c:if>
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
						<fmt:message key="heading.group" >
							<fmt:param><c:out value="${session.sessionName}" /></fmt:param>
						</fmt:message>
					</a>
				</span>
	        </div>
        
			<div id="collapse${session.sessionID}" class="panel-collapse collapse ${status.first ? 'in' : ''}" 
        			role="tabpanel" aria-labelledby="heading${session.sessionID}">
	</c:if>
	
	<div class="loffset10">
		<fmt:message key="heading.totalLearners" />&nbsp;${session.numberOfLearners}
	</div>

	<table class="table table-centered voffset10">

		<tr>
			<th width="30%">
				<fmt:message key="heading.learner" />
			</th>
			<th width="40%">
				<fmt:message key="heading.mindmapEntry" />
			</th>
			<th width="40%">
				<fmt:message key="label.notebookEntry" />
			</th>
		</tr>

		<c:choose>
			<c:when test="${dto.multiUserMode}">
				
				<c:choose>
					<c:when test="${dto.reflectOnActivity == true}">
						<c:forEach var="user" items="${session.userDTOs}">
							<tr>
								<td>
									<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
								</td>
						
								<td >
									<a href="./learning.do?mode=learner&amp;toolSessionID=${session.sessionID}&amp;monitor=true">
										<fmt:message key="label.view" />
									</a>
								</td>
						
								<td width="30%">
									<a href="./monitoring.do?dispatch=reflect&amp;userUID=${user.uid}&amp;toolContentID=${dto.toolContentId}">
										<fmt:message key="label.view" />
									</a>
								</td>
							</tr>
						</c:forEach>
					</c:when>
					
					<c:otherwise>
						<tr>
							<td>
								<fmt:message key="label.multimode" />
							</td>
					
							<td>
								<a href="./learning.do?mode=learner&amp;toolSessionID=${session.sessionID}&amp;monitor=true">
									<fmt:message key="label.view" />
								</a>
							</td>
					
							<td>
								<fmt:message key="label.notAvailable" />
							</td>
						</tr>
					</c:otherwise>
					
				</c:choose>
				
			</c:when>
			
			<c:otherwise>
				<c:forEach var="user" items="${session.userDTOs}">
					<tr>
						<td>
							<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
						</td>
						
						<td>
							<c:choose>
								<c:when test="${user.finishedActivity != true}">
									<fmt:message key="label.notAvailable" />
								</c:when>
								<c:otherwise>
									<a href="./monitoring.do?dispatch=showMindmap&amp;userUID=${user.uid}&amp;toolContentID=${dto.toolContentId}&amp;contentFolderID=${contentFolderID}">
										<fmt:message key="label.view" />
									</a>									
								</c:otherwise>
							</c:choose>
						</td>
						
						<td>
							<c:choose>
								<c:when test="${dto.reflectOnActivity != true}">
									<fmt:message key="label.notAvailable" />
								</c:when>
								<c:otherwise>
									<a href="./monitoring.do?dispatch=reflect&amp;userUID=${user.uid}&amp;toolContentID=${dto.toolContentId}">
										<fmt:message key="label.view" />
									</a>
								</c:otherwise>
							</c:choose>
						</td>
						
					</tr>
				</c:forEach>
			</c:otherwise>
			
		</c:choose>

	</table>
	
		<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
	
</c:forEach>

<c:if test="${isGroupedActivity}">
	</div> <!--  end panel group -->
</c:if>

<%@ include file="advanceOptions.jsp"%>

<%@include file="dateRestriction.jsp"%>

