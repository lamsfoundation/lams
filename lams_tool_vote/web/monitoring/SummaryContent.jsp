<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<script type="text/javascript">
	$(window).on('load', function(){
		<c:forEach var="currentDto" items="${voteGeneralMonitoringDTO.sessionDTOs}">
			<c:set var="chartURL" value="${tool}chartGenerator.do?currentSessionId=${currentDto.sessionId}&toolContentID=${toolContentID}" />
			drawChart('pie', 'chartDiv${currentDto.sessionId}', '${chartURL}');
		</c:forEach>
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
 
<div id="change-leader-modals"></div>