<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<script type="text/javascript">
	$(window).load(function(){
		<c:forEach var="currentDto" items="${voteGeneralMonitoringDTO.sessionDTOs}">
			<c:set var="chartURL" value="${tool}chartGenerator.do?currentSessionId=${currentDto.sessionId}&toolContentID=${toolContentID}" />
			drawChart('pie', 'chartDiv${currentDto.sessionId}', '${chartURL}');
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
 
<div class="panel">
	<h4>
	    <c:out value="${voteGeneralMonitoringDTO.activityTitle}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${voteGeneralMonitoringDTO.activityInstructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${useSelectLeaderToolOuput}">
		<lams:Alert type="info" id="use-leader" close="false">
			<fmt:message key="label.info.use.select.leader.outputs" />
		</lams:Alert>
	</c:if>
	
	<c:if test="${empty sessionDTOs}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="error.noLearnerActivity"/>
		</lams:Alert>
	</c:if>
	
 </div>
 
<jsp:include page="/monitoring/AllSessionsSummary.jsp" />
 
<%@include file="AdvanceOptions.jsp"%>
 
<%@include file="daterestriction.jsp"%>
 