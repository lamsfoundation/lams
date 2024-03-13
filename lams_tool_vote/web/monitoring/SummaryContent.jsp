<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/monitorheader.jsp"%>

<script type="text/javascript">
	MONITORING_STATISTIC_URL = "<c:url value="/monitoring/statistics.do?toolContentID=${toolContentID}"/>";
	
	$(window).on('load', function(){
		<c:forEach var="currentDto" items="${voteGeneralMonitoringDTO.sessionDTOs}">
			<c:set var="chartURL" value="${tool}chartGenerator.do?currentSessionId=${currentDto.sessionId}&toolContentID=${toolContentID}" />
			drawChart('pie', 'chartDiv${currentDto.sessionId}', '${chartURL}');
		</c:forEach>
	});
	
	$(document).ready(function(){
		doStatistic();
	});

	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '${toolContentID}',
		messageNotification: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.notification" /></spring:escapeBody>',
		messageRestrictionSet: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.set" /></spring:escapeBody>',
		messageRestrictionRemoved: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.removed" /></spring:escapeBody>'
	};

	function showChangeLeaderModal(toolSessionId) {
		$('#change-leader-modals').empty()
		.load('<c:url value="/monitoring/displayChangeLeaderForGroupDialogFromActivity.do" />',{
			toolSessionID : toolSessionId
		});
	}

	function onChangeLeaderCallback(response, leaderUserId, toolSessionId){
        if (response.isSuccessful) {
            $.ajax({
    			'url' : '<c:url value="/monitoring/changeLeaderForGroup.do"/>',
    			'type': 'post',
    			'cache' : 'false',
    			'data': {
    				'toolSessionID' : toolSessionId,
    				'leaderUserId' : leaderUserId,
    				'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
    			},
    			success : function(){
    				alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.leader.successfully.changed'/></spring:escapeBody>");
    			},
    			error : function(){
    				alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.leader.not.changed'/></spring:escapeBody>");
        		}
            });
        	
		} else {
			alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='label.monitoring.leader.not.changed'/></spring:escapeBody>");
		}
	}
</script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<c:if test="${useSelectLeaderToolOuput}">
	<lams:Alert5 type="info" id="use-leader">
		<fmt:message key="label.info.use.select.leader.outputs" />
	</lams:Alert5>
</c:if>

<c:if test="${empty sessionDTOs}">
	<lams:Alert5 type="info" id="no-session-summary">
		<fmt:message key="error.noLearnerActivity"/>
	</lams:Alert5>
</c:if>
 
<h1>
    <c:out value="${voteGeneralMonitoringDTO.activityTitle}" escapeXml="true"/>
</h1>
	
<div class="instructions">
    <c:out value="${voteGeneralMonitoringDTO.activityInstructions}" escapeXml="false"/>
</div>
 
<jsp:include page="/monitoring/AllSessionsSummary.jsp" />

<h2 class="card-subheader fs-4" id="header-statistics">
	<fmt:message key="label.stats" />
</h2>
<%@ include file="Stats.jsp"%>
 
 <h2 class="card-subheader fs-4" id="header-settings">
	Settings
</h2>
<%@include file="AdvanceOptions.jsp"%>
<lams:RestrictedUsageAccordian submissionDeadline="${submissionDeadline}"/>
<div id="change-leader-modals"></div>
