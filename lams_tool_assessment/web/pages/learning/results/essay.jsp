<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty toolSessionID}">
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
</c:if>

<c:if test="${assessment.allowDiscloseAnswers && fn:length(sessions) > 1}">
	<table class="table table-responsive table-striped table-bordered table-hover table-condensed" id="rating-table-${question.uid}">
		<tr role="row">
			<td colspan="2" class="text-center">
				<b><fmt:message key="label.learning.summary.other.team.answers"/></b>
				<c:if test="${question.groupsAnswersDisclosed and not empty toolSessionID}">
					<button type="button" class="btn btn-xs btn-default pull-right" onClick="javascript:refreshToRating(${question.uid})">
						<fmt:message key="label.refresh"/>
					</button>
				</c:if>
			</td>
		</tr>
		<c:forEach var="session" items="${sessions}" varStatus="status">
			<%-- Default answer value, when answers are not disclosed yet --%>
			<c:set var="answer"><i><fmt:message key="label.not.yet.disclosed"/></i></c:set>
			
			<%-- Reset variable value --%>
			<c:set var="showRating" value="false" />
			<c:set var="sessionResults" value="" />
			
			<c:if test="${question.groupsAnswersDisclosed}">
				<%-- Get the needed piece of information from a complicated questionSummaries structure --%>
				<c:set var="questionSummary" value="${questionSummaries[question.uid]}" />
				<c:set var="sessionResults" 
					value="${questionSummary.questionResultsPerSession[status.index]}" />
				<c:set var="sessionResults" value="${sessionResults[fn:length(sessionResults)-1]}" />
				<c:choose>
					<%-- If uid is NULL then it is a dummy session result, for a group that has not provided answers
						 Do not display the group's rating then --%>
					<c:when test="${empty sessionResults.uid}">
						<c:set var="sessionResults" value="" />
					</c:when>
					<c:otherwise>
						<c:set var="answer" value="${sessionResults.answer}" />
						<c:set var="itemRatingDto" value="${itemRatingDtos[sessionResults.uid]}" />
					</c:otherwise>
				</c:choose>
				<c:set var="canRate" value="${toolSessionID != session.sessionId and (!isLeadershipEnabled or isUserLeader)}" />
				<c:set var="showRating" 
					value="${canRate or (not empty itemRatingDto.commentDtos and (toolSessionID != session.sessionId or questionSummary.showOwnGroupRating))}" />
			</c:if>
			
			<%-- Show answers for all other teams, and just rating if someone has already commented on this team's answer --%>
			<c:if test="${not empty sessionResults and (toolSessionID != session.sessionId or showRating)}">
				<tr role="row" ${toolSessionID == session.sessionId ? 'class="bg-success"' : ''}>
					<td class="text-center" style="width: 20%" ${showRating ? 'rowspan="2"' : ''}>
						<lams:Portrait userId="${session.groupLeader.userId}"/>&nbsp;
						<c:choose>
							<c:when test="${toolSessionID == session.sessionId}">
								<b><fmt:message key="label.your.team"/></b>
							</c:when>
							<c:otherwise>
								<%-- Sessions are named after groups --%>
								<c:out value="${session.sessionName}" escapeXml="true"/> 
							</c:otherwise>
						</c:choose>
					</td>
					
					<%-- Do not show your own answer --%> 
					<c:if test="${toolSessionID != session.sessionId}">
							<td>
								<c:out value="${answer}" escapeXml="false" /> 
							</td>
						</tr>
					</c:if>

					<c:if test="${showRating}">
						<tr>
							<td>
								<%-- Do not allow voting for own answer, and for non-leaders if leader is enabled --%>
								<lams:Rating itemRatingDto="${itemRatingDto}"
											 isItemAuthoredByUser="${not canRate}"
											 showAllComments="true"
											 refreshOnComment="rating-table-${question.uid}" />
							</td>
						</tr>
					</c:if>
				
			</c:if>
		</c:forEach>
	</table>
</c:if>