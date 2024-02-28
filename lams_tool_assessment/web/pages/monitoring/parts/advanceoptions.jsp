<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">     
	<div class="ltable table-striped table-sm no-header mb-0">
		<div class="row">
			<div class="col">
				<fmt:message key="label.use.select.leader.tool.output" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.useSelectLeaderToolOuput}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.questions.per.page" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.questionsPerPage == -1}">
						<fmt:message key="label.authoring.advance.sections" />
					</c:when>
					<c:when test="${assessment.questionsPerPage == 0}">
						<fmt:message key="label.authoring.advance.all.in.one.page" />
					</c:when>
					<c:otherwise>
						<c:out value="${assessment.questionsPerPage}" escapeXml="true"/>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.shuffle.questions" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.shuffled}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>

		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.shuffle.answers" />
			</div>

			<div class="col">
				<c:choose>
					<c:when test="${assessment.shuffledAnswers}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>

		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.numbered.questions" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.numbered}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.attempts.allowed" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.attemptsAllowed == 0}">
						<fmt:message key="label.authoring.advance.unlimited" />
					</c:when>
					<c:otherwise>
						<c:out value="${assessment.attemptsAllowed}" escapeXml="true"/>
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.passing.mark" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.passingMark == 0}">
						-
					</c:when>
					<c:otherwise>
						<c:out value="${assessment.passingMark}" escapeXml="true"/>
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.allow.students.overall.feedback" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.allowOverallFeedbackAfterQuestion}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.allow.students.question.feedback" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.allowQuestionFeedback}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.disclose.answers" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.allowDiscloseAnswers}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.rate.answers" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.allowAnswerRating}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>

		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.allow.students.right.answers" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.allowRightAnswersAfterQuestion}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.allow.students.wrong.answers" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.allowWrongAnswersAfterQuestion}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>	
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.allow.students.grades" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.allowGradesAfterAttempt}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.allow.students.history.responses" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.allowHistoryResponses}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.answer.justification" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.allowAnswerJustification}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advance.discussion" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.allowDiscussionSentiment}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		<div class="row">
			<div class="col">
				<fmt:message key="label.enable.confidence.levels" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.enableConfidenceLevels}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
		<div class="row">
			<div class="col">
				<fmt:message key="label.authoring.advanced.notify.on.attempt.completion" />
			</div>
			
			<div class="col">
				<c:choose>
					<c:when test="${assessment.notifyTeachersOnAttemptCompletion}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</div>
		</div>
	</div>
</lams:AdvancedAccordian>