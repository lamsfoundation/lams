<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<c:set var="timeLimitPanelUrl"><lams:LAMSURL/>monitoring/timeLimit5.jsp</c:set>
<c:url var="timeLimitPanelUrl" value="${timeLimitPanelUrl}">
	<c:param name="toolContentId" value="${assessment.contentId}"/>
	<c:param name="absoluteTimeLimit" value="${assessment.absoluteTimeLimitSeconds}"/>
	<c:param name="relativeTimeLimit" value="${assessment.relativeTimeLimit}"/>
	<c:param name="isTbl" value="true" />
	<c:param name="controllerContext" value="tool/laasse10/monitoring" />
</c:url>

<style>

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
		// do not refresh charts automatically
		// they will be redrawn on page auto reload
		COMPLETION_CHART_UPDATE_INTERVAL = 0;
	
	$(document).ready(function(){
		openEventSource('<lams:WebAppURL />monitoring/getCompletionChartsData.do?toolContentId=${toolContentID}', function(event) {
			if (!event.data) {
				return;
			}
			var data = JSON.parse(event.data);
			drawActivityCompletionChart(data, true);
			drawAnsweredQuestionsChart(data, ${groupsInAnsweredQuestionsChart}, true);

			$('#student-choices-table').load('<lams:WebAppURL />tblmonitoring/iraAssessmentStudentChoicesTable.do?toolContentID=${toolContentID}');
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
	<div class="row">
		<div class="col-8 offset-2 text-center">
			<h3>
				<fmt:message key="label.ira.questions.marks"/>
			</h3>
		</div>
	</div>
	
	<!-- Notifications -->  
	<div class="row">
		<div class="col-10 offset-1 text-end">
			<a href="#nogo" type="button" class="btn btn-secondary buttons_column"
					onclick="javascript:loadTab('irat', $('#load-irat-tab-btn'))">
				<i class="fa fa-undo"></i>
				<fmt:message key="label.hide.students.choices"/>
			</a>
			<a href="#nogo" onclick="javascript:printTable(); return false;" type="button" class="btn btn-secondary buttons_column">
				<i class="fa fa-print"></i>
				<fmt:message key="label.print"/>
			</a>
			<a href="#nogo" onclick="javascript:exportExcel(); return false;" type="button" class="btn btn-secondary buttons_column">
				<i class="fa fa-file"></i>
				<fmt:message key="label.excel.export"/>
			</a>
			<a class="btn btn-secondary buttons_column d-none" target="_blank" id="allocate-vsas-button"
			   href='<lams:LAMSURL />qb/vsa/displayVsaAllocate.do?toolContentID=${toolContentID}'>
				<fmt:message key="label.vsa.allocate.button" />
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
			<div class="monitoring-panel position-relative">
				<h4 id="answered-questions-chart-none" class="position-absolute top-50 w-100 text-center">No students have answered questions yet</h4>
				<canvas id="answered-questions-chart"></canvas>
			</div>
		</div>
	</div>
	
	<%-- Include student's choices part --%>
	<div class="row">
		<div class="col-10 offset-1" id="student-choices-table">
			<%@ include file="/pages/monitoring/parts/mcqStudentChoices5.jsp" %>
		</div>
	</div>
	
	<div class="row">
		<div class="col-10 offset-1" id="time-limit-panel-placeholder">
		</div>
	</div>
</div>