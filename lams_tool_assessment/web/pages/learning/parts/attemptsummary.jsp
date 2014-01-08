<c:if test="${finishedLock}">
	<table class="forum" style="background:none; border: 1px solid #cacdd1; margin-bottom:60px; padding-top:0px; margin-bottom: 10px;" cellspacing="0">
		<tr>
			<th style="width: 130px; border-left: none; padding-top:0px; " >
				<fmt:message key="label.learning.summary.started.on" />
			</th>
			<td >
				<lams:Date value="${result.startDate}"/>
			</td>
		</tr>
		
		<tr>
			<th style="width: 130px;" >
				<fmt:message key="label.learning.summary.completed.on" />
			</th>
			<td>
				<lams:Date value="${result.finishDate}" />
			</td>
		</tr>
		<tr>
			<th style="width: 130px;" >
				<fmt:message key="label.learning.summary.time.taken" />
			</th>
			<td>
				<fmt:formatDate value="${result.timeTaken}" pattern="H" timeZone="GMT" /> <fmt:message key="label.learning.summary.hours" />
				<fmt:formatDate value="${result.timeTaken}" pattern="m" timeZone="GMT" /> <fmt:message key="label.learning.summary.minutes" />
			</td>
		</tr>
		<c:if test="${assessment.allowGradesAfterAttempt}">
			<tr>
				<th style="width: 130px;" >
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
				<th style="width: 130px;" >
					<fmt:message key="label.learning.summary.feedback" />
				</th>
				<td>
					${result.overallFeedback}
				</td>
			</tr>			
		</c:if>				
	</table>
	<br><br>
</c:if>
