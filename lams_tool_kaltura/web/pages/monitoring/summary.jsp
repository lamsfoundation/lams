<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<link type="text/css" href="${lams}/css/thickbox.css" rel="stylesheet">
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
<script type="text/javascript" src="${lams}/includes/javascript/thickbox.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript">
	var evalcomixWindow = null;
	function openEvalcomixWindow(url) {
    	evalcomixWindow=window.open(url,'evalcomixWindow','width=800,height=600,scrollbars=yes,resizable=yes');
		if (window.focus) {evalcomixWindow.focus()}
	}
</script>

<div class="panel">
	<h4>
	  <c:out value="${kaltura.title}" escapeXml="true"/>
	</h4>
	
	<div class="instructions voffset5">
	  <c:out value="${kaltura.instructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty sessionDTOs}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="label.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
</div>

<c:if test="${!empty sessionDTOs}">

	<c:if test="${isGroupedActivity}">	
		<h5>
			<fmt:message key="label.select.group.name" />
		</h5>
	</c:if>
	
	<table class="<c:if test="${isGroupedActivity}">table</c:if> voffset10">
		<c:forEach var="session" items="${sessionDTOs}">
			<tr>
				<td>
					<a href="<c:url value="/monitoring.do"/>?dispatch=showGroupLearning&toolSessionID=${session.sessionID}&TB_iframe=true" class="btn btn-default btn-sm thickbox" title="${session.sessionName}">
						${session.sessionName}
					</a>
				</td>
			</tr>
		</c:forEach>
	</table>
	<br/>
	
</c:if>

<%@include file="reflections.jsp"%>

<%@include file="advanceOptions.jsp"%>

<%@include file="daterestriction.jsp"%>
