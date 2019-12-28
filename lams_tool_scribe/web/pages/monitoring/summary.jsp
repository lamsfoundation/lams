<%@ include file="/common/taglibs.jsp"%>
<c:set var="dto" value="${requestScope.monitoringDTO}" />

<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/portrait.js" ></script>
<script type="text/javascript">
	function confirmForceComplete() {
		var message = "<fmt:message key='message.confirmForceComplete'/>";
		if (confirm(message)) {
			return true;			
		} else {
			return false;
		}
	}
	$(document).ready(function(){
	 	initializePortraitPopover('<lams:LAMSURL/>');
	});
</script>

<form:form action="/lams/tool/lascrb11/monitoring.do" modelAttribute="monitoringForm">
	<form:hidden path="toolContentID" value="${dto.toolContentID}" />
	<form:hidden path="contentFolderID" />
	<p>
		<button class="btn btn-default pull-right">
			<fmt:message key="button.refresh" />
		</button>
	</p>
</form:form>


<div class="panel">
	<h4>
	    <c:out value="${monitoringDTO.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${monitoringDTO.instructions}" escapeXml="false"/>
	</div>
</div>


<c:if test="${isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="session" items="${dto.sessionDTOs}" varStatus="status">

	<c:set var="subpanelHeadingClass"></c:set>
		
	<c:if test="${isGroupedActivity}">
		<c:set var="subpanelHeadingClass">panel-heading-sm</c:set>
		<div class="panel panel-default" >
        <div class="panel-heading" id="heading${session.sessionID}">
   	    	<span class="panel-title collapsable-icon-left">
       		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${session.sessionID}" 
				aria-expanded="false" aria-controls="collapse${session.sessionID}" >
			${session.sessionName}</a>
			</span>
       	</div>
       
        <div id="collapse${session.sessionID}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${session.sessionID}">
		<div class="loffset5 voffset10 roffset5" >
	</c:if>

	<div class="loffset5">
		<c:choose>
			<c:when test="${not empty session.userDTOs and (not dto.autoSelectScribe or session.appointedScribe != null)}">
            <c:set var="csrfToken"><csrf:token/></c:set>
				<form:form action="/lams/tool/lascrb11/monitoring/appointScribe.do?${csrfToken}" modelAttribute="monitoringForm" method="post">
		
					<form:hidden path="toolSessionID" value="${session.sessionID}" />
					<form:hidden path="contentFolderID" />
					<form:hidden path="currentTab" id="currentTab" />
		
					<div class="form-group form-inline">
						<fmt:message key="heading.selectScribe" />
						<form:select path="appointedScribeUID" cssClass="form-control input-sm loffset5">
							<c:forEach var="user" items="${session.userDTOs}">
 								<form:option value="${user.uid}">
 									<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
								</form:option>
 							</c:forEach>
						</form:select>
						<button class="btn btn-default btn-sm loffset5">
							<fmt:message key="button.select" />
						</button>
					</div>	
				</form:form>
			</c:when>
		
			<c:otherwise>
				<p>
					<fmt:message key="message.noLearners" />
				</p>
			</c:otherwise>
		</c:choose>
	
 		<c:if test="${session.appointedScribe != null}">
			<strong>
				<fmt:message key="heading.appointedScribe" />: <lams:Portrait userId="${session.appointedScribeUserId}" hover="true"><c:out value="${session.appointedScribe}" escapeXml="true"/></lams:Portrait>
			</strong>
		</c:if>
 	</div>
		
	<c:if test="${session.appointedScribe != null}">
		<c:set var="scribeSessionDTO" value="${session}" scope="request"/>

		<div class="panel panel-default voffset10" style="margin-bottom:5px" >
        <div class="panel-heading ${subpanelHeadingClass}"><span class="panel-title"><fmt:message key="heading.report" /></span></div>
        <div class="panel-body">
			<c:forEach var="report" items="${session.reportDTOs}">
 				<p>
					<c:out value="${report.headingDTO.headingText}" escapeXml="false" />
				</p>
				<p>
					<lams:out value="${report.entryText}" escapeHtml="true"/>
 				</p>
				<hr />
 			</c:forEach>
	
			<%@include file="/pages/parts/voteDisplay.jsp"%>
	
			<c:if test="${session.forceComplete eq false}">
				<form:form action="monitoring/forceCompleteActivity.do" modelAttribute="monitoringForm" onsubmit="return confirmForceComplete();">
					<form:hidden path="toolSessionID" value="${session.sessionID}" />
					<form:hidden path="contentFolderID" />
					<button class="btn btn-default btn-sm">
						<fmt:message key="button.forceComplete" />
					</button>
				</form:form>
			</c:if>
		</div>
		</div>
	</c:if>

	<c:if test="${dto.reflectOnActivity}">
		<div class="panel panel-default voffset10" style="margin-bottom:5px" >
        <div class="panel-heading  ${subpanelHeadingClass}"><span class="panel-title"><fmt:message key="heading.reflections" /></span></div>
		<table class="table table-condensed table-striped">
			<tr>
				<th>
					<fmt:message>heading.learner</fmt:message>
				</th>

				<th style="text-align: right">
					<c:choose>
						<c:when test="${dto.reflectOnActivity}">
							<fmt:message key="heading.reflection" />
						</c:when>
						<c:otherwise>
							&nbsp;
						</c:otherwise>
					</c:choose>
				</th>
			</tr>

			<c:forEach var="user" items="${session.userDTOs}">
				<tr>
					<td>
						<lams:Portrait userId="${user.userID}" hover="true"><c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/></lams:Portrait>
					</td>
					<c:if test="${dto.reflectOnActivity}">
						<td style="text-align: right">
							<c:if test="${user.finishedReflection}">
								<c:url value="monitoring/openNotebook.do" var="openNotebook">
									<c:param name="uid" value="${user.uid}" />
								</c:url>
								<a href="javascript:launchPopup('${openNotebook}','<fmt:message key="heading.reflection" />');" class="btn btn-default btn-sm" >
									<fmt:message key="link.view" />
								</a>
							</c:if>
						</td>
					</c:if>
				</tr>
			</c:forEach>
		</table>
		</div>
	</c:if>


	<c:if test="${isGroupedActivity}">
		</div>
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
	
</c:forEach>

<c:if test="${isGroupedActivity}">
</div>
</c:if>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
<table class="table table-striped table-condensed">	

	<tr>
		<td>
			<fmt:message key="advanced.showAggregatedReports" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.showAggregatedReports}">
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
			<fmt:message key="advanced.selectScribe" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${dto.autoSelectScribe}">
					<fmt:message key="advanced.firstLearner" />
				</c:when>
				<c:otherwise>
					<fmt:message key="advanced.selectInMonitor" />
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
				<c:when test="${dto.reflectOnActivity}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${dto.reflectOnActivity}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					<lams:out value="${dto.reflectInstructions}" escapeHtml="true" />	
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</lams:AdvancedAccordian>
