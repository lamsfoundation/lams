<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<c:set var="lams">
        <lams:LAMSURL />
</c:set>

<c:set var="dto" value="${requestScope.monitoringDTO}" />

<link type="text/css" href="${lams}/css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">

<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '<c:out value="${param.toolContentID}" />',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>

<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>  
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<script type="text/javascript">
<!--
	var viewWindow = null;
	function openViewWindow(url)
	{
		if(viewWindow && viewWindow.open && !viewWindow.closed){
			viewWindow.close();
		}
		viewWindow = window.open(url,'viewWindow','resizable,width=1152,height=648,scrollbars');
		viewWindow.window.focus();
	}
	
	function showInFrame(url) {
		
		var area=document.getElementById("wikiArea");
		if(area != null){
			area.style.width="640px";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		location.hash = "wikiArea";
	}
//-->
</script>

<c:set var="dto" value="${wikiDTO}" />
 
<div class="panel">
	<h4>
	    <c:out value="${dto.title}" escapeXml="true"/>
	</h4>
 	<div class="instructions voffset5">
		<c:out value="${dto.instructions}" escapeXml="false" />
	</div>

	<c:if test="${empty dto.sessionDTOs}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="monitor.nosessions"></fmt:message> 
		</lams:Alert>
	</c:if> 
</div>

<c:if test="${not empty dto.sessionDTOs}">
		<table class="table table-striped">
			<tr>
				<th>
					<fmt:message key="monitor.th.sessions"></fmt:message>
				</th>	
				<th>
					<fmt:message key="monitor.th.numlearners"></fmt:message>
				</th>
			</tr>
			<c:forEach var="session" items="${dto.sessionDTOs}">
			<tr>
				<td>
					<a href='javascript:openViewWindow("<lams:WebAppURL />monitoring/showWiki.do?toolSessionID=${session.sessionID}&contentFolderID=${contentFolderID}");'>${session.sessionName}</a>
				</td>	
				<td>
					${session.numberOfLearners}
				</td>	
			</tr>
			</c:forEach>
		</table>
</c:if>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
             
<table class="table table-striped table-condensed">

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
			<fmt:message key="advanced.allowLearnerCreatePages" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowLearnerCreatePages}">
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
			<fmt:message key="advanced.allowLearnerInsertLinks" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowLearnerInsertLinks}">
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
			<fmt:message key="advanced.allowLearnerAttachImages" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowLearnerAttachImages}">
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
			<fmt:message key="advanced.allowLearnerAttachImages" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.allowLearnerAttachImages}">
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
			<fmt:message key="advanced.editingLimits.prompt" />
		</td>
		
		<td>
			<fmt:message key="advanced.editingLimits.minimum" />&nbsp; 
			<c:choose>
				<c:when test="${dto.minimumEdits == 0}">
					<fmt:message key="advanced.editingLimits.nominimum" />
				</c:when>
				<c:otherwise>
					${dto.minimumEdits}
				</c:otherwise>
			</c:choose>
			
			<br />
			
			<fmt:message key="advanced.editingLimits.maximum" /> &nbsp;
			<c:choose>
				<c:when test="${dto.maximumEdits == 0}">
					<fmt:message key="advanced.editingLimits.nomaximum" />
				</c:when>
				<c:otherwise>
					${dto.maximumEdits}
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	
</table>
</lams:AdvancedAccordian> 
<%@include file="daterestriction.jsp"%>