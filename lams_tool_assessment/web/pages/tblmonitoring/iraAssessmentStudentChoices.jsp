<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

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
			pageInitialised = false;

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

			let data = JSON.parse(decodeURIComponent(event.data));
			drawActivityCompletionChart(data, true);
			drawAnsweredQuestionsChart(data, true);

			// if in student choice table a question modal is open, postpone the table update until the modal is closed
			let openQuestionModal = $('.iraQuestionModal.show');
			if (openQuestionModal.length == 0) {
				$('#student-choices-table').load('<lams:WebAppURL />tblmonitoring/iraAssessmentStudentChoicesTable.do?toolContentID=${toolContentID}');
				return;
			}

			openQuestionModal.one('hidden.bs.modal', function() {
				$('#student-choices-table').load('<lams:WebAppURL />tblmonitoring/iraAssessmentStudentChoicesTable.do?toolContentID=${toolContentID}');
			});
		});
	});

	function exportExcel(){
		//dynamically create a form and submit it
		var exportExcelUrl = "<lams:LAMSURL/>tool/laasse10/monitoring/exportSummary.do?toolContentID=${toolContentID}&downloadTokenValue=dummy&fileName=assessment_export.xlsx&reqID="
				+ (new Date()).getTime();
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
	<div class="row">
		<div class="col-10 offset-1 text-right">
			<!-- Notifications -->
			<div class="float-end">
				<a href="#nogo" onclick="javascript:openActivityMonitoring(); return false;" type="button" class="btn btn-secondary buttons_column">
					<i class="fa-solid fa-circle-info"></i>
					<fmt:message key="label.activity.monitoring"/>
				</a>
				<a href="#nogo" type="button" class="btn btn-secondary buttons_column"
				   onclick="javascript:loadTab('irat', $('#load-irat-tab-btn'))">
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
				<c:if test="${vsaPresent}">
					<a class="btn btn-secondary buttons_column" target="_blank" id="allocate-vsas-button"
					   href='<lams:LAMSURL />qb/vsa/displayVsaAllocate.do?toolContentID=${toolContentID}'>
						<fmt:message key="label.vsa.allocate.button" />
					</a>
				</c:if>
			</div>
			<!-- End notifications -->
			<h3>
				<fmt:message key="label.ira.questions.marks"/>
			</h3>
		</div>
	</div>

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

	<%-- Include student's choices part --%>
	<div class="row">
		<div class="col-10 offset-1" id="student-choices-table">
		</div>
	</div>

	<div class="row">
		<div class="col-10 offset-1" id="time-limit-panel-placeholder">
		</div>
	</div>
</div>