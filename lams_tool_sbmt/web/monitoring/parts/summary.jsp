<%@include file="/common/taglibs.jsp"%>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<link type="text/css" href="${lams}/css/jquery-ui-1.8.11.flick-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui-timepicker-addon.css" rel="stylesheet">

<script type="text/javascript" src="${lams}includes/javascript/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui-1.8.11.custom.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>	
<script type="text/javascript">
	// pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring.do?method=setSubmissionDeadline"/>',
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<script type="text/javascript">
	function launchPopup(url, title) {
		var wd = null;
		if (wd && wd.open && !wd.closed) {
			wd.close();
		}
		wd = window.open(url, title,
				'resizable,width=796,height=570,scrollbars');
		wd.window.focus();
	}

	function viewMark(userId, sessionId) {
		var act = "<c:url value="/monitoring.do"/>";
		launchPopup(act + "?method=listMark&userID=" + userId
				+ "&toolSessionID=" + sessionId, "mark");
	}
	function viewAllMarks(sessionId) {
		var act = "<c:url value="/monitoring.do"/>";
		launchPopup(act + "?method=listAllMarks&toolSessionID=" + sessionId,
				"mark");
	}

	var messageTargetDiv = "#messageArea";
	function releaseMarks(sessionId) {
		var url = "<c:url value="/monitoring.do"/>";
		var reqIDVar = new Date();
		$(messageTargetDiv).load(
			url,
			{
				method: "releaseMarks",
				toolSessionID: sessionId, 
				reqID: reqIDVar.getTime()
			}
		);
	}

	function downloadMarks(sessionId) {
		var url = "<c:url value="/monitoring.do"/>";
		var reqIDVar = new Date();
		var param = "?method=downloadMarks&toolSessionID=" + sessionId
				+ "&reqID=" + reqIDVar.getTime();
		url = url + param;
		location.href = url;
	}
</script>


<h1 style="padding-bottom: 10px;">
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

	<tr>
		<td>
			<fmt:message key="label.authoring.advance.lock.on.finished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.lockOnFinished}">
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
			<fmt:message key="label.limit.number.upload" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.limitUpload}">
					<fmt:message key="label.on" />, ${authoring.limitUploadNumber}
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.authoring.advanced.notify.mark.release" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.notifyLearnersOnMarkRelease}">
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
			<fmt:message key="label.authoring.advanced.notify.onfilesubmit" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${authoring.notifyTeachersOnFileSubmit}">
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
				<c:when test="${authoring.reflectOnActivity}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${authoring.reflectOnActivity}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					${authoring.reflectInstructions}	
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</div>

<%@include file="daterestriction.jsp"%>

<table cellpadding="0">
<tr><td>
	<img src="${tool}/images/indicator.gif" style="display:none" id="messageArea_Busy" />
	<span id="messageArea"></span>
</td></tr>
</table>
<c:forEach var="element" items="${sessionUserMap}">
	<c:set var="sessionDto" value="${element.key}" />
	<c:set var="userlist" value="${element.value}" />
	<table cellpadding="0">
		<c:if test="${isGroupedActivity}">
			<tr>
				<th colspan="3">
					<h2>
						<fmt:message key="label.session.name" /> : 
						<c:out value="${sessionDto.sessionName}" />
					</h2>
				</th>
			</tr>
		</c:if>
		
		<c:forEach var="user" items="${userlist}" varStatus="status">
			<c:if test="${status.first}">
				<tr>
					<th>
						<fmt:message key="monitoring.user.fullname"/>
					</th>
					<th>
						<fmt:message key="monitoring.user.loginname"/>
					</th>
					<c:if test="${user.hasRefection}">
						<th>
							<fmt:message key="monitoring.user.reflection"/>
						</th>
					</c:if>
					<th>
						<fmt:message key="monitoring.marked.question"/>
					</th>
					<th>&nbsp;</th>
				</tr>
			</c:if>		
			<tr>
				<td><c:out value="${user.firstName}" /> 
					<c:out value="${user.lastName}" />
				</td>
				<td><c:out value="${user.login}" /></td>
				<c:if test="${user.hasRefection}">
				<td>
						<c:set var="viewReflection">
							<c:url value="/monitoring.do?method=viewReflection&toolSessionID=${sessionDto.sessionID}&userUid=${user.userUid}"/>
						</c:set>
						<html:link href="javascript:launchPopup('${viewReflection}')">
							<fmt:message key="label.view" />
						</html:link>
				</td>
				</c:if>				
				<td>
					<c:choose>
					<c:when test="${user.anyFilesMarked}">
						<fmt:message key="label.yes"/>
					</c:when>
					<c:otherwise>
						<fmt:message key="label.no"/>
					</c:otherwise>
					</c:choose>
				</td>
				<td><html:link
					href="javascript:viewMark(${user.userID},${sessionDto.sessionID});"
					property="Mark" styleClass="button">
					<fmt:message key="label.monitoring.Mark.button" />
				</html:link></td>
			</tr>
		</c:forEach>
		<c:if test="${empty userlist}">
			<tr>
				<td colspan="3"><fmt:message key="label.no.user.available" />
				</td>
			</tr>
		</c:if>
		<tr>
			<td><html:link href="javascript:viewAllMarks(${sessionDto.sessionID});"
				property="viewAllMarks" styleClass="button">
				<fmt:message key="label.monitoring.viewAllMarks.button" />
			</html:link></td>
			<td><html:link href="javascript:releaseMarks(${sessionDto.sessionID});"
				property="releaseMarks" styleClass="button">
				<fmt:message key="label.monitoring.releaseMarks.button" />
			</html:link></td>
			<td><html:link href="javascript:downloadMarks(${sessionDto.sessionID});"
				property="downloadMarks" styleClass="button">
				<fmt:message key="label.monitoring.downloadMarks.button" />
			</html:link></td>
		</tr>
	</table>
</c:forEach>
