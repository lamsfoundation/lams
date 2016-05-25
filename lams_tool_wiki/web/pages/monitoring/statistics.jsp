<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${wikiDTO}" />

<c:if test="${empty dto.sessionDTOs}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="monitor.nosessions"></fmt:message> 
	</lams:Alert>
</c:if>

<c:forEach var="session" items="${dto.sessionDTOs}">

	<c:if test="${isGroupedActivity}">
		 <div class="panel panel-default" >
	        <div class="panel-heading" id="heading${session.sessionID}">
				<span class="panel-title">
					<c:out value="${session.sessionName}" />
				</span>
	        </div>
	</c:if>

	<table class="table table-condensed table-no-border">
		<tr>
			<td class="field-name" width="40%">
				<fmt:message>heading.totalLearnersInGroup</fmt:message>
			</td>
			<td width="70%">
				${session.numberOfLearners}
			</td>
		</tr>
		<tr>
			<td class="field-name" width="40%">
				<fmt:message>heading.totalFinishedLearnersInGroup</fmt:message>
			</td>
			<td width="70%">
				${session.numberOfFinishedLearners}
			</td>
		</tr>
	</table>
	
	<c:if test="${isGroupedActivity}">
		</div>
	</c:if>
	
</c:forEach>
