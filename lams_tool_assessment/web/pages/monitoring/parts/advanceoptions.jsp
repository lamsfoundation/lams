<%@ include file="/common/taglibs.jsp"%>

<c:set var="adTitle"><fmt:message key="monitor.summary.th.advancedSettings" /></c:set>
<lams:AdvancedAccordian title="${adTitle}">     
	<table class="table table-striped table-condensed">

		<tr>
			<td>
				<fmt:message key="label.use.select.leader.tool.output" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.useSelectLeaderToolOuput}">
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
				<fmt:message key="label.authoring.advance.time.limit" />
			</td>
			
			<td>
				<c:out value="${assessment.questionsPerPage}" escapeXml="true"/>
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.shuffle.questions" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.shuffled}">
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
				<fmt:message key="label.authoring.advance.numbered.questions" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.numbered}">
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
				<fmt:message key="label.authoring.advance.attempts.allowed" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.attemptsAllowed == 0}">
						<fmt:message key="label.authoring.advance.unlimited" />
					</c:when>
					<c:otherwise>
						<c:out value="${assessment.attemptsAllowed}" escapeXml="true"/>
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.passing.mark" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.passingMark == 0}">
						-
					</c:when>
					<c:otherwise>
						<c:out value="${assessment.passingMark}" escapeXml="true"/>
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<tr>
			<td>
				<fmt:message key="label.authoring.advance.allow.students.overall.feedback" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.allowOverallFeedbackAfterQuestion}">
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
				<fmt:message key="label.authoring.advance.allow.students.question.feedback" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.allowQuestionFeedback}">
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
				<fmt:message key="label.authoring.advance.allow.students.right.answers" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.allowRightAnswersAfterQuestion}">
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
				<fmt:message key="label.authoring.advance.allow.students.wrong.answers" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.allowWrongAnswersAfterQuestion}">
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
				<fmt:message key="label.authoring.advance.allow.students.grades" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.allowGradesAfterAttempt}">
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
				<fmt:message key="label.authoring.advance.allow.students.history.responses" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.allowHistoryResponses}">
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
				<fmt:message key="label.authoring.advanced.notify.on.attempt.completion" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.notifyTeachersOnAttemptCompletion}">
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
				<fmt:message key="monitor.summary.td.addNotebook" />
			</td>
			
			<td>
				<c:choose>
					<c:when test="${assessment.reflectOnActivity}">
						<fmt:message key="label.on" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.off" />
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		
		<c:choose>
			<c:when test="${assessment.reflectOnActivity}">
				<tr>
					<td>
						<fmt:message key="monitor.summary.td.notebookInstructions" />
					</td>
					<td>
						<lams:out value="${assessment.reflectInstructions}" escapeHtml="true"/>
					</td>
				</tr>
			</c:when>
		</c:choose>
		
	</table>
</lams:AdvancedAccordian>
