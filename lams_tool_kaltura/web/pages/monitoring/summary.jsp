<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<link rel="stylesheet" href="${lams}/css/thickbox.css" type="text/css" media="screen">
<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">

<script type="text/javascript">
<!--
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
//-->	
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/thickbox.js"></script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript">
<!--
	var evalcomixWindow = null;
	
	function openEvalcomixWindow(url) {
    	evalcomixWindow=window.open(url,'evalcomixWindow','width=800,height=600,scrollbars=yes,resizable=yes');
		if (window.focus) {evalcomixWindow.focus()}
	}
//-->
</script>

<c:choose>
	<c:when test="${empty sessionDTOs}">
		<div align="center">
			<b> <fmt:message key="label.monitoring.summary.no.session" /> </b>
		</div>	
	</c:when>
	
	<c:otherwise>
	
		<table class="space-left space-top">
			<tr>
				<th class="field-name">
					<fmt:message key="label.select.group.name" />
				</th>
			</tr>
		
			<c:forEach var="session" items="${sessionDTOs}">
				<tr>
					<td>
					<a href="<c:url value="/monitoring.do"/>?dispatch=showGroupLearning&toolSessionID=${session.sessionID}&keepThis=true&TB_iframe=true&height=630&width=800" class="button thickbox" title="<fmt:message key='heading.notebookEntry' />">
						${session.sessionName}
					</a>
					</td>
				</tr>
			</c:forEach>
		</table>	
	
	</c:otherwise>
</c:choose>

<%@include file="reflections.jsp"%>

<%@include file="advanceOptions.jsp"%>

<%@include file="daterestriction.jsp"%>


