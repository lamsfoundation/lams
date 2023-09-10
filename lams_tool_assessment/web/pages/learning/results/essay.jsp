<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty toolSessionID}">
	<div class="card-subheader">
		<fmt:message key="label.learning.short.answer.answer" />
	</div>

	<c:choose>
		<c:when test="${empty question.codeStyle}">
			${question.answer}
		</c:when>
		<c:otherwise>
			<pre class="code-style" data-lang="${question.codeStyleMime}">${question.answer}</pre>
		</c:otherwise>
	</c:choose>
</c:if>

<c:if test="${assessment.allowDiscloseAnswers && fn:length(sessions) > 1}">
	<div class="card-subheader mt-3">
		<fmt:message key="label.learning.summary.other.team.answers"/>
	</div>
	
	<div class="table-responsive table-sm div-table mt-2" id="rating-table-${question.uid}">
		<div class="row">
			<div class="w-25">
				<fmt:message key="monitoring.label.group" />
			</div>
			<div class="col">
				<fmt:message key="label.learning.short.answer.answer" />
			</div>
		</div>

		<c:forEach var="session" items="${sessions}" varStatus="status">
			<%-- Default answer value, when answers are not disclosed yet --%>
			<c:set var="answer"><i><fmt:message key="label.not.yet.disclosed"/></i></c:set>
			
			<%-- Reset variable value --%>
			<c:set var="showRating" value="false" />
			<c:set var="sessionResults" value="" />
			
			<c:if test="${question.groupsAnswersDisclosed}">
				<%-- Get the needed piece of information from a complicated questionSummaries structure --%>
				<c:set var="questionSummary" value="${questionSummaries[question.uid]}" />
				<c:set var="sessionResults" value="${questionSummary.questionResultsPerSession[status.index]}" />
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
				<c:set var="showRating" value="${canRate or (not empty itemRatingDto.commentDtos and (toolSessionID != session.sessionId or questionSummary.showOwnGroupRating))}" />
			</c:if>
			
			<%-- Show answers for all other teams, and just rating if someone has already commented on this team's answer --%>
			<c:if test="${not empty sessionResults and (toolSessionID != session.sessionId or showRating)}">
				<div class="row ${toolSessionID == session.sessionId ? 'bg-success' : ''}">
					<div class="w-25">
						<lams:Portrait userId="${session.groupLeader.userId}"/>
						
						<c:choose>
							<c:when test="${toolSessionID == session.sessionId}">
								<b><fmt:message key="label.your.team"/></b>
							</c:when>
							
							<c:otherwise>
								<%-- Sessions are named after groups --%>
								<c:out value="${session.sessionName}" escapeXml="true"/> 
							</c:otherwise>
						</c:choose>
					</div>
					
					<div class="col">
						<%-- Do not show your own answer --%> 
						<c:if test="${toolSessionID != session.sessionId}">	
							<c:choose>
								<c:when test="${empty question.codeStyle}">
									<c:out value="${answer}" escapeXml="false" /> 
								</c:when>
								<c:otherwise>
									<pre class="code-style" data-lang="${question.codeStyleMime}">${answer}</pre>
								</c:otherwise>
							</c:choose>
						</c:if>
				
						<c:if test="${showRating}">
							<%-- Do not allow voting for own answer, and for non-leaders if leader is enabled --%>
							<lams:Rating itemRatingDto="${itemRatingDto}"
										 isItemAuthoredByUser="${not canRate}"
										 showAllComments="true"
										 refreshOnComment="rating-table-${question.uid}" />
						</c:if>	
					</div>
				</div>
			</c:if>
		</c:forEach>
		
		<c:if test="${question.groupsAnswersDisclosed and not empty toolSessionID}">
			<div>
				<button type="button"
					class="btn btn-sm btn-secondary btn-icon-refresh float-end mt-2"
					onClick="javascript:refreshToRating(${question.uid})">
					<fmt:message key="label.refresh" />
				</button>
			</div>
		</c:if>
	</div>

</c:if>