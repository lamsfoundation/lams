<div id="attempt-summary" class="alert alert-success shadow mx-auto col-sm-8">
	<div class="row">
		<div class="col-sm-5">
			<fmt:message key="label.learning.summary.started.on" />
		</div>
		<div class="col-sm-7">
			<lams:Date value="${result.startDate}" />
		</div>
	</div>

	<div class="row">
		<div class="col-sm-5">
			<fmt:message key="label.learning.summary.completed.on" />
		</div>
		<div class="col-sm-7">
			<lams:Date value="${result.finishDate}" />
		</div>
	</div>

	<div class="row">
		<div class="col-sm-5">
			<fmt:message key="label.learning.summary.time.taken" />
		</div>
		<div class="col-sm-7">
			<fmt:formatDate value="${result.timeTaken}" pattern="H" timeZone="GMT" />
			&nbsp;
			<fmt:message key="label.learning.summary.hours" />
			&nbsp;
			<fmt:formatDate value="${result.timeTaken}" pattern="m" timeZone="GMT" />
			&nbsp;
			<fmt:message key="label.learning.summary.minutes" />
		</div>
	</div>

	<c:if test="${assessment.allowGradesAfterAttempt}">
		<div class="row">
			<div class="col-sm-5">
				<fmt:message key="label.learning.summary.grade" />
			</div>
			<div class="col-sm-7">
				<c:choose>
					<c:when test="${result.maximumGrade == 0}">
						<c:set var="resultPercentage" value="0" />
					</c:when>
					<c:otherwise>
						<c:set var="resultPercentage">
							<fmt:formatNumber
								value="${result.grade * 100 / result.maximumGrade}"
								maxFractionDigits="2" />
						</c:set>
					</c:otherwise>
				</c:choose>

				<fmt:message key='label.learning.summary.out.of.maximum'>
					<fmt:param>
						<fmt:formatNumber value="${result.grade}" maxFractionDigits="3" />
					</fmt:param>
					<fmt:param>${result.maximumGrade}</fmt:param>
					<fmt:param>${resultPercentage}</fmt:param>
				</fmt:message>
			</div>
		</div>
	</c:if>

	<c:if test="${assessment.allowOverallFeedbackAfterQuestion && (result.overallFeedback != null)}">
		<div class="row">
			<div class="col-sm-5">
				<fmt:message key="label.learning.summary.feedback" />
			</div>
			<div class="col-sm-7">
				<c:out value="${result.overallFeedback}" escapeXml="true" />
			</div>
		</div>
	</c:if>
</div>
