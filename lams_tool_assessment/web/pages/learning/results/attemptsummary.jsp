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
<lams:Alert id="attempt-summary" type="info" close="false">
	<table class="table table-hover">
			<tr>
				<th>
					<fmt:message key="label.learning.summary.grade" />:&thinsp;
				</th>
				<td>
					<c:choose>
						<c:when  test="${result.maximumGrade == 0}">
							<c:set var="resultPercentage" value="0"/>
						</c:when>
						<c:otherwise>
							<c:set var="resultPercentage">
								<fmt:formatNumber value="${result.grade * 100 / result.maximumGrade}" maxFractionDigits="2"/>
							</c:set>
						</c:otherwise>
					</c:choose>
				
					<fmt:formatNumber value="${result.grade}" maxFractionDigits="3"/>
					&thinsp;<fmt:message key="label.learning.summary.out.of.maximum" />
					&thinsp;${result.maximumGrade} (${resultPercentage}%)
				</td>
			</tr>
        </table>
        <br><br>
</lams:Alert>
</c:if>
<c:if test="${assessment.allowOverallFeedbackAfterQuestion && (result.overallFeedback != null)}">
<lams:Alert id="attempt-summary" type="info" close="false">
        <table class="table table-hover">
		<tr>
			<th>
				<fmt:message key="label.learning.summary.feedback" />
			</th>
			<td>
				<c:out value="${result.overallFeedback}" escapeXml="true"/>
			</td>
		</tr>			
	</table>
	<br><br>
</lams:Alert>
</c:if>
