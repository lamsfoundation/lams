<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">

<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>  
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/raphael.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.raphael.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.pie.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.bar.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/chart.js"></script>
<script type="text/javascript">
	var chartDataUrl = '<lams:WebAppURL />chartGenerator.do';
	
	$(document).ready(function(){
		<c:forEach var="currentDto" items="${voteGeneralMonitoringDTO.sessionDTOs}">
			<c:if test="${currentDto.sessionId != 0}">
			drawChart('bar', ${currentDto.sessionId}0, {'currentSessionId' : '${currentDto.sessionId}'});
			drawChart('pie', ${currentDto.sessionId}1, {'currentSessionId' : '${currentDto.sessionId}'});
			</c:if>
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
	<jsp:include page="/monitoring/AllSessionsSummary.jsp" />
					
	<jsp:include page="/monitoring/Reflections.jsp" />
</c:if>								

<c:if test="${noSessionsNotebookEntriesExist == 'true'}">
	<jsp:include page="/monitoring/Reflections.jsp" />
</c:if>						
		
<%@include file="AdvanceOptions.jsp"%>

<%@include file="daterestriction.jsp"%>
