<%@include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-12 --%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">

<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
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

	function releaseMarks(sessionId) {
		var url = "<c:url value="/monitoring.do"/>";
		
		$("#messageArea_Busy").show();
		$("#messageArea").load(
			url,
			{
				method: "releaseMarks",
				toolSessionID: sessionId, 
				reqID: (new Date()).getTime()
			},
			function() {
				$("#messageArea_Busy").hide();
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

<h1>
	<c:out value="${authoring.title}" escapeXml="true" />
</h1>
<div class="instructions space-top">
	<c:out value="${authoring.instruction}" escapeXml="false" />
</div>

<table cellpadding="0">
<tr><td>
	<img src="${tool}/images/indicator.gif" style="display:none" id="messageArea_Busy" />
	<span id="messageArea"></span>
</td></tr>
</table>
<c:forEach var="element" items="${sessionUserMap}">
	<c:set var="sessionDto" value="${element.key}" />
	<c:set var="userlist" value="${element.value}" />

		<c:if test="${isGroupedActivity}">
			<h1>
				<fmt:message key="label.session.name" />: 
				<c:out value="${sessionDto.sessionName}" />
			</h1>
		</c:if>
		<br/>
		
		<table cellpadding="0" class="alternative-color">
		<c:forEach var="user" items="${userlist}" varStatus="status">
			
			<c:if test="${status.first}">
				<tr>
					<th>
						<fmt:message key="monitoring.user.fullname"/>
					</th>
					<th align="center">
						<fmt:message key="monitoring.user.submittedFiles"/>
					</th>
					<c:if test="${user.hasRefection}">
						<th align="center">
							<fmt:message key="monitoring.user.reflection"/>
						</th>
					</c:if>
					<th align="center">
						<fmt:message key="monitoring.marked.question"/>
					</th>
				</tr>
			</c:if>		
			<tr>
				<td><c:out value="${user.firstName}" /> 
					<c:out value="${user.lastName}" />
				</td>
				<!-- LDEV-2194 -->
				<td align="center"><c:out value="${fn:length(user.filesUploaded)}" /></td>
				
				<c:if test="${user.hasRefection}">
				<td align="center">
						<c:set var="viewReflection">
							<c:url value="/monitoring.do?method=viewReflection&toolSessionID=${sessionDto.sessionID}&userUid=${user.userUid}"/>
						</c:set>
						<html:link href="javascript:launchPopup('${viewReflection}')">
							<fmt:message key="label.view" />
						</html:link>
				</td>
				</c:if>				
				<td align="center">
					<c:choose>
					<c:when test="${user.anyFilesMarked}">
						<fmt:message key="label.yes"/>
					</c:when>
					<c:otherwise>
						<fmt:message key="label.no"/>
					</c:otherwise>
					</c:choose>
				[<html:link
					href="javascript:viewMark(${user.userID},${sessionDto.sessionID});"
					property="Mark" >
					<fmt:message key="label.monitoring.Mark.button" />
				</html:link>]</td>
			</tr>
		</c:forEach>
		<c:if test="${empty userlist}">
			<tr>
				<td colspan="3"><fmt:message key="label.no.user.available" />
				</td>
			</tr>
		</c:if>
		</table>
		<table>
		  <tr>
			<td align="center"><html:link href="javascript:viewAllMarks(${sessionDto.sessionID});"
				property="viewAllMarks" styleClass="button">
				<fmt:message key="label.monitoring.viewAllMarks.button" />
			</html:link></td>
			<td align="center"><html:link href="javascript:releaseMarks(${sessionDto.sessionID});"
				property="releaseMarks" styleClass="button">
				<fmt:message key="label.monitoring.releaseMarks.button" />
			</html:link></td>
			<td align="center" style="border-bottom: 12px;border-bottom-color: gray;"><html:link href="javascript:downloadMarks(${sessionDto.sessionID});"
				property="downloadMarks" styleClass="button">
				<fmt:message key="label.monitoring.downloadMarks.button" />
			</html:link></td>
		</tr>
	</table>
	<br/>
</c:forEach>

<br/>

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
					<lams:out value="${authoring.reflectInstructions}" escapeHtml="true"/>	
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</div>

<%@include file="daterestriction.jsp"%>