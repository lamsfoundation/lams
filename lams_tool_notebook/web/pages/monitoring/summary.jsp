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

<c:set var="dto" value="${notebookDTO}" />

<h1><c:out value="${notebookDTO.title}" escapeXml="true"/></h1>

<div class="instructions space-top">
<c:out value="${notebookDTO.instructions}" escapeXml="false"/>
</div>

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
				<c:when test="${dto.lockOnFinish}">
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
			<fmt:message key="advanced.allowRichEditor" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowRichEditor}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
</table>
</div>

<%@include file="daterestriction.jsp"%>

<c:forEach var="session" items="${dto.sessionDTOs}">

	<c:if test="${isGroupedActivity}">
		<h2>
			${session.sessionName}
		</h2>
	</c:if>

	<table cellpadding="0">
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="heading.totalLearners" />
			</td>
			<td width="70%">
				${session.numberOfLearners}
			</td>
		</tr>
	</table>

	<table cellpadding="0">

		<tr>
			<th>
				<fmt:message key="heading.learner" />
			</th>
			<th>
				<fmt:message key="heading.notebookEntry" />
			</th>
		</tr>


		<c:forEach var="user" items="${session.userDTOs}">
			<tr>
				<td width="30%">
					${user.firstName} ${user.lastName}
				</td>
				<td width="70%">
					<c:choose>
						<c:when test="${user.entryUID == null}">
							<fmt:message key="label.notAvailable" />
						</c:when>

						<c:otherwise>
							<a href="./monitoring.do?dispatch=showNotebook&amp;userUID=${user.uid}&keepThis=true&TB_iframe=true&height=260&width=800" class="thickbox" title="<fmt:message key='heading.notebookEntry' />">
								<fmt:message key="label.view" /> 
							</a>
						</c:otherwise>
					</c:choose>

				</td>
			</tr>
		</c:forEach>
	</table>
</c:forEach>
