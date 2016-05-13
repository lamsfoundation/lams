<%@ include file="/common/taglibs.jsp"%>
<c:set var="dto" value="${gmapDTO}" />

<c:if test="${empty dto.sessionDTOs}">
	<lams:Alert type="info" id="no-session-summary" close="false">
		<fmt:message key="label.nogroups" />
	</lams:Alert>
</c:if>

<c:forEach var="session" items="${dto.sessionDTOs}">

    <div class="panel panel-default" >
		<div class="panel-heading" id="heading${toolSessionDto.sessionID}">
		<span class="panel-title">
			${session.sessionName}
		</span>
		</div>

		<div class="panel-body">

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
		</div>
	</div>
	
</c:forEach>
