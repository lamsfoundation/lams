<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<style>
	.question-title {
		overflow: auto;
		min-width: 150px;
	}
</style>

<lams:JSImport src="includes/javascript/chart5.js" relative="true" />

<script>
	var WEB_APP_URL = '<lams:WebAppURL />',

			LABELS = $.extend(LABELS, {
				ACTIVITY_COMPLETION_CHART_TITLE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion" /></spring:escapeBody>',
				ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion.possible" /></spring:escapeBody>',
				ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion.started" /></spring:escapeBody>',
				ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.completion.completed" /></spring:escapeBody>',
				ANSWERED_QUESTIONS_CHART_TITLE : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions" /></spring:escapeBody>',
				ANSWERED_QUESTIONS_CHART_TITLE_GROUPS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.groups" /></spring:escapeBody>',
				ANSWERED_QUESTIONS_CHART_X_AXIS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.x.axis" /></spring:escapeBody>',
				ANSWERED_QUESTIONS_CHART_Y_AXIS_STUDENTS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.y.axis.students" /></spring:escapeBody>',
				ANSWERED_QUESTIONS_CHART_Y_AXIS_GROUPS : '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.monitoring.summary.answered.questions.y.axis.groups" /></spring:escapeBody>'
			}),

			activityCompletionChart = null,
			answeredQuestionsChart = null,
			COMPLETION_CHART_UPDATE_INTERVAL = 10 * 1000;

	$(document).ready(function(){
		openEventSource('<lams:WebAppURL />monitoring/getTimeLimitPanelUpdateFlux.do?toolContentId=${toolContentID}', function(event) {
			if (!event.data) {
				return;
			}

			// destroy existing absolute time limit counter before refresh
			$('.absolute-time-limit-counter').countdown('destroy');
			let data = JSON.parse(event.data);
			$('#time-limit-panel-placeholder').load('<lams:LAMSURL/>monitoring/timeLimit5.jsp?toolContentId=${toolContentID}&absoluteTimeLimitFinish=' + data.absoluteTimeLimitFinish
					+ '&relativeTimeLimit=' + data.relativeTimeLimit + '&absoluteTimeLimit=' + data.absoluteTimeLimit
					+ '&isTbl=true&controllerContext=tool/laasse10/monitoring');
		});

		openEventSource('<lams:WebAppURL />monitoring/getCompletionChartsData.do?toolContentId=${toolContentID}', function(event) {
			if (!event.data) {
				return;
			}
			var data = JSON.parse(decodeURIComponent(event.data));
			drawActivityCompletionChart(data, true);
			drawAnsweredQuestionsChart(data, true);

			$('#student-choices-table').load('<lams:WebAppURL />tblmonitoring/aesStudentChoicesTable.do?toolContentID=${toolContentID}');
		});
	});

	function exportExcel(){
		//dynamically create a form and submit it
		var exportExcelUrl = "<lams:LAMSURL/>tool/laasse10/monitoring/exportSummary.do?toolContentID=${toolContentID}&downloadTokenValue=dummy&fileName=assessment_export.xlsx&reqID=" + (new Date()).getTime();
		var form = $('<form method="post" action="' + exportExcelUrl + '"></form>');
		var hiddenInput = $('<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"></input>');
		form.append(hiddenInput);
		$(document.body).append(form);
		form.submit();
	};


	function openActivityMonitoring(){
		openPopUp('<lams:WebAppURL />monitoring/summary.do?toolContentID=${toolContentID}&contentFolderID='
				+ contentFolderId, "MonitorActivity", popupHeight, popupWidth, true, true);
	}
</script>

<div class="container-fluid">

	<!-- Notifications -->
	<div class="row">
		<div class="col-10 offset-1 text-end">
			<a href="#nogo" onclick="javascript:openActivityMonitoring(); return false;" type="button" class="btn btn-secondary buttons_column">
				<i class="fa-solid fa-circle-info"></i>
				<fmt:message key="label.activity.monitoring"/>
			</a>
			<a href="#nogo" type="button" class="btn btn-secondary buttons_column"
			   onclick="javascript:loadAePane(${toolContentID}, 'default'); return false;">
				<i class="fa fa-clipboard-question"></i>
				<fmt:message key="label.hide.students.choices"/>
			</a>
			<a href="#nogo" onclick="javascript:printTable(); return false;" type="button" class="btn btn-secondary buttons_column">
				<i class="fa fa-print"></i>
				<fmt:message key="label.print"/>
			</a>
			<a href="#nogo" onclick="javascript:exportExcel(); return false;" type="button" class="btn btn-secondary buttons_column">
				<i class="fa fa-file-excel"></i>
				<fmt:message key="label.excel.export"/>
			</a>
		</div>
	</div>
	<!-- End notifications -->

	<div class="row" id="completion-charts-container">
		<div class="col-md-5 col-sm-12 offset-md-1 me-2 my-4">
			<div class="monitoring-panel">
				<div style="max-width: 400px; margin: auto">
					<canvas id="activity-completion-chart"></canvas>
				</div>
			</div>
		</div>

		<div class="col-md-5 col-sm-12 ms-2 my-4">
			<div class="monitoring-panel">
				<h4 id="answered-questions-chart-none" class="text-center position-relative top-50">
					<fmt:message key="label.monitoring.student.choices.none" />
				</h4>
				<canvas id="answered-questions-chart"></canvas>
			</div>
		</div>
	</div>

	<!-- Table -->
	<div class="row">
		<div class="col-10 offset-1">
			<div class="card">
				<div class="card-body table-responsive pb-0">

					<table  id="questions-data" class="table table-responsive table-bordered table-hover table-condensed">
						<thead>
						<tr role="row" class="border-top-0">
							<th></th>
							<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
								<th class="text-center">
									<div class="question-title">
										<c:if test="${assessment.numbered}">
											${i.count}.
										</c:if>
											${tblQuestionDto.title}
									</div>
								</th>
							</c:forEach>
						</tr>
						</thead>
						<tbody>

						<tr role="row">
							<th><b>Question type</b></th>
							<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
								<td class="text-center">
										${tblQuestionDto.questionTypeLabel}
								</td>
							</c:forEach>
						</tr>

						<tr>
							<td><b>Correct answer</b></td>
							<c:forEach var="tblQuestionDto" items="${questionDtos}" varStatus="i">
								<td class="text-center">
									<c:out value="${tblQuestionDto.correctAnswer}" escapeXml="false" />
								</td>
							</c:forEach>
						</tr>

						<tr>
							<td colspan="${fn:length(questionDtos) + 1}" class="fw-bold"><fmt:message key="label.teams"/></td>
						</tr>
						</tbody>
						<tbody id="student-choices-table">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-10 offset-1" id="time-limit-panel-placeholder">
		</div>
	</div>
</div>