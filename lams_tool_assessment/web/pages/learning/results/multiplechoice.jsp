<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<c:choose>
		<c:when test="${question.multipleAnswersAllowed}">
			<fmt:message key="label.learning.choose.at.least.one.answer" />
		</c:when>
		<c:otherwise>
			<fmt:message key="label.learning.choose.one.answer" />
		</c:otherwise>
	</c:choose>
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}">
			<c:set var="isCorrect" 
				   value="${(assessment.allowDiscloseAnswers 
				   			 ? question.correctAnswersDisclosed : assessment.allowRightAnswersAfterQuestion)
				    		 && (option.grade == 1)}" />
			<c:set var="isWrong"
				   value="${(assessment.allowDiscloseAnswers 
				   			 ? question.correctAnswersDisclosed : assessment.allowWrongAnswersAfterQuestion)
				    		&& (option.grade < 1)}" />
			<tr ${isCorrect ? 'class="bg-success"' : '' }>
				<td class="complete-item-gif">
				    <c:if test="${option.answerBoolean && isCorrect}">
					    <i class="fa fa-check text-success"></i>
		            </c:if>
				    <c:if test="${option.answerBoolean && isWrong}">
					    <i class="fa fa-times text-danger"></i>	
				    </c:if>			
                </td>
				<td class="has-radio-button">
					<c:choose>
						<c:when test="${question.multipleAnswersAllowed}">
							<input type="checkbox" name="question${status.index}_${option.sequenceId}" value="${true}"
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
								disabled="disabled"
							/>
						</c:when>
						<c:otherwise>
							<input type="radio" name="question${status.index}" value="${option.sequenceId}"
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
		 						disabled="disabled"
							/>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:out value="${option.optionString}" escapeXml="false" />
				</td>
				
				<c:if test="${assessment.allowQuestionFeedback}">
	
					<td width=30%;">
						<c:if test="${option.answerBoolean}">
							<c:out value="${option.feedback}" escapeXml="false" />
						</c:if>
					</td>		
				</c:if>
				
			</tr>
		</c:forEach>
	</table>
</div>	

<c:if test="${assessment.allowDiscloseAnswers && fn:length(sessions) > 1}">
	<table class="table table-responsive table-striped table-bordered table-hover table-condensed">
		<tr role="row">
			<td colspan="2" class="text-center"><b><fmt:message key="label.learning.summary.other.team.answers"/></b></td>
		</tr>
		<c:forEach var="session" items="${sessions}" varStatus="status">
			<%-- Now groups other than this one --%>
			<c:if test="${sessionMap.toolSessionID != session.sessionId}">
				<tr role="row">
					<td class="text-center" style="width: 33%">
						<%-- Sessions are named after groups --%>
						<c:out value="${session.sessionName}" escapeXml="true"/> 
					</td>
					<c:set var="cssClass" value="" />
					<c:set var="answer" value="?" />
					<c:if test="${question.groupsAnswersDisclosed}">
						<%-- Get the needed piece of information from a complicated questionSummaries structure --%>
						<c:set var="sessionResults" 
							value="${questionSummaries[question.uid].questionResultsPerSession[status.index]}" />
						<c:set var="sessionResults" value="${sessionResults[fn:length(sessionResults)-1]}" />
						<c:forEach var="sessionOption" items="${sessionResults.optionAnswers}">
							<%-- This option was chosen by the user --%>
							<c:if test="${sessionOption.answerBoolean}">
								<%-- Find the matching option to check if it is correct and get its text --%>
								<c:forEach var="option" items="${question.optionDtos}">
									<c:if test='${option.uid == sessionOption.optionUid}'>
										<c:if test="${question.correctAnswersDisclosed && option.grade == 1}">
											<c:set var="cssClass" value="bg-success" />
										</c:if>
										<c:set var="answer" value="${option.optionString}" />
									</c:if>
								</c:forEach>
							</c:if>
						</c:forEach>
					</c:if>
					<td class="text-center ${cssClass}">
						<c:out value="${answer}" escapeXml="false" /> 
					</td>
				</tr>
			</c:if>
		</c:forEach>
	</table>
</c:if>

<c:if test="${assessment.allowQuestionFeedback}">
	<div class="feedback">
		<c:choose>
			<c:when test="${question.answerTotalGrade >= 1}">
				<c:out value="${question.feedbackOnCorrect}" escapeXml="false" />
			</c:when>
			<c:when test="${question.answerTotalGrade > 0}">
				<c:out value="${question.feedbackOnPartiallyCorrect}" escapeXml="false" />
			</c:when>
			<c:otherwise>
				<c:out value="${question.feedbackOnIncorrect}" escapeXml="false" />
			</c:otherwise>		
		</c:choose>
	</div>
</c:if>
