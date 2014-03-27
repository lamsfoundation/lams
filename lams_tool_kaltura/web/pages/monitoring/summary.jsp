<%@ include file="/common/taglibs.jsp"%>
<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<link rel="stylesheet" href="${lams}/css/thickbox.css" type="text/css" media="screen">
<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">

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
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
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

<h1 style="padding-bottom: 10px;">
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

	<tr>
		<td>
			<fmt:message key="advanced.lockOnFinished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${kaltura.lockOnFinished}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.allowContributeVideos" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${kaltura.allowContributeVideos}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.allowSeeingOtherUsersRecordings" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${kaltura.allowSeeingOtherUsersRecordings}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.learnerContributionLimit" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${kaltura.learnerContributionLimit == -1}">
					<fmt:message key="advanced.unlimited" />
				</c:when>
				<c:otherwise>
					${kaltura.learnerContributionLimit}
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.allowComments" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${kaltura.allowComments}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="advanced.allowRatings" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${kaltura.allowRatings}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	<tr>
		<td>
			<fmt:message key="monitor.summary.td.addNotebook" />
		</td>
			
		<td>
			<c:choose>
				<c:when test="${kaltura.reflectOnActivity == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
		
	<c:choose>
		<c:when test="${kaltura.reflectOnActivity == true}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					<lams:out value="${kaltura.reflectInstructions}" escapeHtml="true"/>
				</td>
			</tr>
		</c:when>
	</c:choose>

</table>
</div>

<%@include file="daterestriction.jsp"%>


