<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">

<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/raphael.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.raphael.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.pie.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.bar.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/chart.js"></script>
<script type="text/javascript">
	var chartDataUrl = '<lams:WebAppURL />chartGenerator.do';
	
	$(document).ready(function(){
		<c:forEach var="currentDto" items="${voteGeneralMonitoringDTO.sessionDTOs}">
			drawChart('pie', ${currentDto.sessionId}, {'currentSessionId' : '${currentDto.sessionId}', 'toolContentID' : '${toolContentID}'});
		</c:forEach>
	});

	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring.do?dispatch=setSubmissionDeadline"/>',
		toolContentID: '${toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<h1>
	<c:out value="${voteGeneralMonitoringDTO.activityTitle}" escapeXml="true"/>
</h1>

<div class="instructions small-space-top">
	<c:out value="${voteGeneralMonitoringDTO.activityInstructions}" escapeXml="false"/>
</div>
<br/>

<c:if test="${(voteGeneralMonitoringDTO.userExceptionNoToolSessions == 'true')}"> 	
	<c:if test="${notebookEntriesExist != 'true' }"> 			
		<table align="center">
			<tr> 
				<td NOWRAP valign=top align=center> 
					<b>  <fmt:message key="error.noLearnerActivity"/> </b>
				</td> 
			<tr>
		</table>
	</c:if>							
</c:if>

<c:if test="${(voteGeneralMonitoringDTO.userExceptionNoToolSessions != 'true') }">

	<c:if test="${useSelectLeaderToolOuput}">
		<div class="info space-bottom">
			<fmt:message key="label.info.use.select.leader.outputs" />
		</div>
	</c:if>

	<jsp:include page="/monitoring/AllSessionsSummary.jsp" />
					
	<jsp:include page="/monitoring/Reflections.jsp" />
</c:if>								

<c:if test="${noSessionsNotebookEntriesExist == 'true'}">
	<jsp:include page="/monitoring/Reflections.jsp" />
</c:if>
		
<%@include file="AdvanceOptions.jsp"%>

<%@include file="daterestriction.jsp"%>
