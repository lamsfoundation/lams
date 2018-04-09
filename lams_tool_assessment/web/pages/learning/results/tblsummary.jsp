<script>
	// command websocket stuff for refreshing
	// trigger is an unique ID of page and action that command websocket code in Page.tag recognises
	commandWebsocketHookTrigger = 'assessment-results-refresh-${assessment.contentId}';
	// if the trigger is recognised, the following action occurs
	commandWebsocketHook = function() {
		location.reload();
	};
</script>


<div class="panel">
<div class="panel-body table-responsive">
<table id="questions-data" class="table table-responsive table-striped table-bordered table-hover table-condensed">
	<%-- Header: questions numbered  --%>
	<thead>
		<tr role="row">
			<th></th>
			<c:forEach items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">
				<th class="text-center">
					<fmt:message key="label.authoring.basic.list.header.question"/>&nbsp;${status.index + 1}
				</th>
			</c:forEach>
		</tr>
	</thead>
	<tbody>
		<tr role="row">
			<%-- Write out type of each question
				 We only support Essay and Multiple Choice here
			--%>
			<td><b><fmt:message key="label.authoring.basic.list.header.type"/></b></td>
			<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}">
				<td class="text-center">
					<c:choose>
						<c:when test="${question.type == 1}">
							<fmt:message key="label.authoring.basic.type.multiple.choice"/>
						</c:when>
						<c:when test="${question.type == 6}">
							<fmt:message key="label.authoring.basic.type.essay"/>
						</c:when>
					</c:choose>
				</td>
			</c:forEach>
		</tr>
		
		<tr role="row">
			<td>
				<b>
					<fmt:message key="label.learning.summary.team.answers"/>
				</b>
			</td>
			<%-- Go through each question and write out this user's answers,
				 which are the same as the leader's answers
			--%>
			<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}">
				<c:set var="cssClass" value="" />
				<c:set var="answer" value="" />
				<c:choose>
					<c:when test="${question.type == 1}">
						<c:forEach var="option" items="${question.optionDtos}">
							<%-- This option was chosen by the user --%>
							<c:if test='${option.answerBoolean}'>
								<%-- Grade 1 means this is the correct answer, so hightlight it, if it has been disclosed --%>
								<c:if test="${question.correctAnswersDisclosed && option.grade == 1}">
									<c:set var="cssClass" value="bg-success" />
								</c:if>
								<c:set var="answer" value="${option.optionString}" />
							</c:if>
						</c:forEach>
					</c:when>
					<c:when test="${question.type == 6}">
						<%-- For essay question just display the answer --%>
						<c:set var="answer" value="${question.answerString}" />
					</c:when>
				</c:choose>
				<td class="text-center ${cssClass}">
					<c:out value="${answer}" escapeXml="false" /> 
				</td>
			</c:forEach>
		</tr>
		
		<c:if test="${fn:length(sessions) > 1}">
			<tr role="row">
				<td colspan="7" style="font-weight: bold;">
					<fmt:message key="label.learning.summary.other.team.answers"/>
				</td> 
			</tr>
			
			<c:forEach var="session" items="${sessions}" varStatus="status">
				<%-- Now groups other than this one --%>
				<c:if test="${sessionMap.toolSessionID != session.sessionId}">
					<tr role="row">
						<td class="text-center">
							<%-- Sessions are named after groups --%>
							<c:out value="${session.sessionName}" escapeXml="true"/> 
						</td>
						<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}">
							<c:set var="cssClass" value="" />
							<c:set var="answer" value="?" />
							<c:if test="${question.groupsAnswersDisclosed}">
								<%-- Get the needed piece of information from a complicated questionSummaries structure --%>
								<c:set var="sessionResults" 
									value="${questionSummaries[question.uid].questionResultsPerSession[status.index]}" />
								<c:set var="sessionResults" value="${sessionResults[fn:length(sessionResults)-1]}" />
								<c:choose>
									<c:when test="${question.type == 1}">
										<c:forEach var="sessionOption" items="${sessionResults.optionAnswers}">
											<%-- This option was chosen by the user --%>
											<c:if test="${sessionOption.answerBoolean}">
												<%-- Find the matching option to check if it is correct and get its text --%>
												<c:forEach var="option" items="${question.optionDtos}">
													<c:if test='${option.uid == sessionOption.optionUid}'>
														<c:if test="${option.grade == 1}">
															<c:set var="cssClass" value="bg-success" />
														</c:if>
														<c:set var="answer" value="${option.optionString}" />
													</c:if>
												</c:forEach>
											</c:if>
										</c:forEach>
									</c:when>
									<c:when test="${question.type == 6}">
										<c:set var="answer" value="${sessionResults.answerString}" />
									</c:when>
								</c:choose>
							</c:if>
							<td class="text-center ${cssClass}">
								<c:out value="${answer}" escapeXml="false" /> 
							</td>
						</c:forEach>
					</tr>
				</c:if>
			</c:forEach>
		</c:if>
	</tbody>
</table>
</div>
</div>