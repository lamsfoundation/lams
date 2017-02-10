<c:if test="${finishedLock}">
	<table class="forum">
<%-- *LKC* commented out the following paragraph --%>
<!--
		<tr>
			<th style="width: 130px;">
				<fmt:message key="label.learning.summary.started.on" />
			</th>
			<td >
				<lams:Date value="${result.startDate}"/>
			</td>
		</tr>
		
		<tr>
			<th>
				<fmt:message key="label.learning.summary.completed.on" />
			</th>
			<td>
				<lams:Date value="${result.finishDate}" />
			</td>
		</tr>
		<tr>
			<th>
				<fmt:message key="label.learning.summary.time.taken" />
			</th>
			<td>
				<fmt:formatDate value="${result.timeTaken}" pattern="H" timeZone="GMT" /> <fmt:message key="label.learning.summary.hours" />
				<fmt:formatDate value="${result.timeTaken}" pattern="m" timeZone="GMT" /> <fmt:message key="label.learning.summary.minutes" />
			</td>
		</tr>
-->
		<c:if test="${assessment.allowGradesAfterAttempt}">
			<tr>
				<th>
					<fmt:message key="label.learning.summary.grade" />
				</th>
				<td>
					<fmt:formatNumber value="${result.grade}" maxFractionDigits="3"/>
					<fmt:message key="label.learning.summary.out.of.maximum" />
					${result.maximumGrade} (<fmt:formatNumber value="${result.grade * 100 / result.maximumGrade}" maxFractionDigits="2"/>%)
				</td>
			</tr>
		</c:if>
		<c:if test="${assessment.allowOverallFeedbackAfterQuestion && (result.overallFeedback != null)}">
			<tr>
				<th>
					<fmt:message key="label.learning.summary.feedback" />
				</th>
				<td>
					<c:out value="${result.overallFeedback}" escapeXml="true"/>
				</td>
			</tr>			
		</c:if>				
	</table>
	<br><br>
</c:if>
