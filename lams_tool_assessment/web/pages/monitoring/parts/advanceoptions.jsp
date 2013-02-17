<h1>
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">
	<tr>
		<td>
			<fmt:message key="label.authoring.advance.time.limit" />
		</td>
		
		<td>
			${assessment.questionsPerPage}
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.authoring.advance.shuffle.questions" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${assessment.shuffled == true}">
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
					${assessment.attemptsAllowed}
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
					${assessment.passingMark}
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
				<c:when test="${assessment.allowOverallFeedbackAfterQuestion == true}">
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
				<c:when test="${assessment.allowQuestionFeedback == true}">
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
				<c:when test="${assessment.allowRightAnswersAfterQuestion == true}">
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
				<c:when test="${assessment.allowWrongAnswersAfterQuestion == true}">
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
				<c:when test="${assessment.allowGradesAfterAttempt == true}">
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
				<c:when test="${assessment.allowHistoryResponses == true}">
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
				<c:when test="${assessment.notifyTeachersOnAttemptCompletion == true}">
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
				<c:when test="${assessment.reflectOnActivity == true}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${assessment.reflectOnActivity == true}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>
					${assessment.reflectInstructions}	
				</td>
			</tr>
		</c:when>
	</c:choose>
	
</table>
</div>