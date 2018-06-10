<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${contentDTO}" />

<div class="panel">
	<h4>
	    <c:out value="${contentDTO.title}" escapeXml="true"/>
	</h4>
	<div class="instructions voffset5">
	    <c:out value="${contentDTO.instructions}" escapeXml="false"/>
	</div>
	
 	<c:if test="${empty dto.sessionDTOs}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>

</div>

<c:set var="usePanel" value="${dto.reflectOnActivity && dto.groupedActivity}"/>

<c:if test="${usePanel}">
<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

	<c:forEach var="session" items="${dto.sessionDTOs}" varStatus="status">

		<html:form action="monitoring"  target="zoomMonitor${session.sessionID}" onsubmit="window.open('', 'zoomMonitor${session.sessionID}', 'resizable=yes,scrollbars=yes')"> 
			<html:hidden property="dispatch" value="startMeeting" />
			<html:hidden property="toolSessionID" value="${session.sessionID}" />

		<c:choose>
		<c:when test="${usePanel}">	
		    <div class="panel panel-default" >
	        <div class="panel-heading" id="heading${session.sessionID}">
	        	<span class="panel-title collapsable-icon-left">
	        	<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapse${session.sessionID}" 
						aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapse${session.sessionID}" >
				<c:out value="${session.sessionName}" />: <fmt:message key="heading.totalLearners" />&nbsp;${session.numberOfLearners}</a>
				</span>
				<span class="pull-right btn-group">
				<html:submit styleClass="btn btn-primary btn-xs">
				<fmt:message key="label.monitoring.startConference" />
				</html:submit>
				</span>
	        </div>
	        <div id="collapse${session.sessionID}" class="panel-collapse collapse ${status.first ? 'in' : ''}" role="tabpanel" aria-labelledby="heading${session.sessionID}">
		</c:when>

		<c:otherwise>
			<h4><fmt:message key="heading.totalLearners" />&nbsp;${session.numberOfLearners}</h4>
			<html:submit styleClass="btn btn-primary"><fmt:message key="label.monitoring.startConference" /></html:submit>
			<div class="voffset5">&nbsp;</div>
		</c:otherwise>
		</c:choose>
		
		<c:if test="${dto.reflectOnActivity}">
			<table class="table table-striped">
		
				<tr>
					<th style="width: 30%;">
						<fmt:message key="heading.learner" />
					</th>
					<th style="width: 70%;">
						<fmt:message key="heading.notebookEntry" />
					</th>
				</tr>
		
		
				<c:forEach var="user" items="${session.userDTOs}">
					<tr>
						<td>
							<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
						</td>
						<td>
							<c:choose>
								<c:when test="${user.notebookEntryUID == null}">
									<fmt:message key="label.notAvailable" />
								</c:when>
		
								<c:otherwise>
									<a
										href="./monitoring.do?dispatch=openNotebook&amp;userUID=${user.uid}">
										<fmt:message key="label.view" /> </a>
								</c:otherwise>
							</c:choose>
		
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		
		</html:form>
		
	<c:if test="${usePanel}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !usePanel || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}

</c:forEach>

<c:if test="${usePanel}">
	</div> <!--  end panel group -->
</c:if>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">
	<table class="table table-striped table-condensed">

	</table>
</lams:AdvancedAccordian>



