<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<c:set var="timeLimitPanelUrl"><lams:LAMSURL/>monitoring/timeLimit5.jsp</c:set>
<c:url var="timeLimitPanelUrl" value="${timeLimitPanelUrl}">
	<c:param name="toolContentId" value="${assessment.contentId}"/>
	<c:param name="absoluteTimeLimitFinish" value="${assessment.absoluteTimeLimitFinishSeconds}"/>
	<c:param name="relativeTimeLimit" value="${assessment.relativeTimeLimit}"/>
	<c:param name="absoluteTimeLimit" value="${assessment.absoluteTimeLimit}"/>
	<c:param name="isTbl" value="true" />
	<c:param name="controllerContext" value="tool/laasse10/monitoring" />
</c:url>

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
		<fmt:message key="label.monitoring.summary.completion" var="ACTIVITY_COMPLETION_CHART_TITLE_VAR"/>
		ACTIVITY_COMPLETION_CHART_TITLE : '<c:out value="${ACTIVITY_COMPLETION_CHART_TITLE_VAR}" />',
		<fmt:message key="label.monitoring.summary.completion.possible" var="ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS_VAR"/>
		ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS : '<c:out value="${ACTIVITY_COMPLETION_CHART_POSSIBLE_LEARNERS_VAR}" />',
		<fmt:message key="label.monitoring.summary.completion.started" var="ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS_VAR"/>
		ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS : '<c:out value="${ACTIVITY_COMPLETION_CHART_STARTED_LEARNERS_VAR}" />',	
		<fmt:message key="label.monitoring.summary.completion.completed" var="ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS_VAR"/>
		ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS : '<c:out value="${ACTIVITY_COMPLETION_CHART_COMPLETED_LEARNERS_VAR}" />',	
		<fmt:message key="label.monitoring.summary.answered.questions" var="ANSWERED_QUESTIONS_CHART_TITLE_VAR"/>
		ANSWERED_QUESTIONS_CHART_TITLE : '<c:out value="${ANSWERED_QUESTIONS_CHART_TITLE_VAR}" />',
		<fmt:message key="label.monitoring.summary.answered.questions.groups" var="ANSWERED_QUESTIONS_CHART_TITLE_GROUPS_VAR"/>
		ANSWERED_QUESTIONS_CHART_TITLE_GROUPS : '<c:out value="${ANSWERED_QUESTIONS_CHART_TITLE_GROUPS_VAR}" />',
		<fmt:message key="label.monitoring.summary.answered.questions.x.axis" var="ANSWERED_QUESTIONS_CHART_X_AXIS_VAR"/>
		ANSWERED_QUESTIONS_CHART_X_AXIS : '<c:out value="${ANSWERED_QUESTIONS_CHART_X_AXIS_VAR}" />',
		<fmt:message key="label.monitoring.summary.answered.questions.y.axis.students" var="ANSWERED_QUESTIONS_CHART_Y_AXIS_STUDENTS_VAR"/>
		ANSWERED_QUESTIONS_CHART_Y_AXIS_STUDENTS : '<c:out value="${ANSWERED_QUESTIONS_CHART_Y_AXIS_STUDENTS_VAR}" />',
		<fmt:message key="label.monitoring.summary.answered.questions.y.axis.groups" var="ANSWERED_QUESTIONS_CHART_Y_AXIS_GROUPS_VAR"/>
		ANSWERED_QUESTIONS_CHART_Y_AXIS_GROUPS : '<c:out value="${ANSWERED_QUESTIONS_CHART_Y_AXIS_GROUPS_VAR}" />'
	}),

	activityCompletionChart = null,
	answeredQuestionsChart = null,
	COMPLETION_CHART_UPDATE_INTERVAL = 10 * 1000;

	$(document).ready(function(){
		openEventSource('<lams:WebAppURL />monitoring/getCompletionChartsData.do?toolContentId=${toolContentID}', function(event) {
			if (!event.data) {
				return;
			}
			var data = JSON.parse(decodeURIComponent(event.data));
			drawActivityCompletionChart(data, true);
			drawAnsweredQuestionsChart(data, true);

			$('#student-choices-table').load('<lams:WebAppURL />tblmonitoring/aesStudentChoicesTable.do?toolContentID=${toolContentID}');
		});

		$('#time-limit-panel-placeholder').load('${timeLimitPanelUrl}');
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
</script>

<div class="container-fluid">

	<!-- Notifications -->
	<div class="row">
		<div class="col-10 offset-1 text-end">
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
				<canvas id="activity-completion-chart"></canvas>
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