<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<tr>
			<td>
				${question.answer}
			</td>
		</tr>
	</table>
</div>

<c:if test="${assessment.allowDiscloseAnswers && fn:length(sessions) > 1}">
	<table class="table table-responsive table-striped table-bordered table-hover table-condensed">
		<tr role="row">
			<td colspan="2" class="text-center"><b><fmt:message key="label.learning.summary.other.team.answers"/></b></td>
		</tr>
		<c:forEach var="session" items="${sessions}" varStatus="status">
			<%-- Default answer value, when answers are not disclosed yet --%>
			<c:set var="answer" value="?" />
			
			<c:if test="${question.groupsAnswersDisclosed}">
				<%-- Get the needed piece of information from a complicated questionSummaries structure --%>
				<c:set var="sessionResults" 
					value="${questionSummaries[question.uid].questionResultsPerSession[status.index]}" />
				<c:set var="sessionResults" value="${sessionResults[fn:length(sessionResults)-1]}" />
				<c:set var="answer" value="${sessionResults.answer}" />
				<c:set var="itemRatingDto" value="${itemRatingDtos[sessionResults.uid]}" />
			</c:if>
			
			<%-- Show answers for all other teams, and just rating if someone has already commented on this team's answer --%>
			<c:if test="${sessionMap.toolSessionID != session.sessionId or not empty itemRatingDto.commentDtos}">
				<tr role="row">
					<td class="text-center" style="width: 33%" ${sessionMap.toolSessionID == session.sessionId ? '' : 'rowspan="2"'}>
						<%-- Sessions are named after groups --%>
						<lams:Portrait userId="${session.groupLeader.userId}"/>&nbsp;
						<c:out value="${session.sessionName}" escapeXml="true"/> 
					</td>
					
					<%-- Do not show your own answer --%> 
					<c:if test="${sessionMap.toolSessionID != session.sessionId}">
							<td>
								<c:out value="${answer}" escapeXml="false" /> 
							</td>
						</tr>
						<tr>
					</c:if>
					
					<c:if test="${question.groupsAnswersDisclosed}">
							<td>
								<lams:Rating itemRatingDto="${itemRatingDto}"
											 isItemAuthoredByUser="${sessionMap.toolSessionID == session.sessionId}"
											 showAllComments="true" />
							</td>
					</c:if>
				</tr>
			</c:if>
		</c:forEach>
	</table>
</c:if>